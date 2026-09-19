package com.company.licenseserver.domain.service

import com.company.licenseserver.domain.model.Customer
import com.company.licenseserver.domain.model.Device
import com.company.licenseserver.domain.model.License
import com.company.licenseserver.domain.model.LicenseStatus
import com.company.licenseserver.domain.model.Product

import com.company.licenseserver.domain.repository.CustomerRepository
import com.company.licenseserver.domain.repository.DeviceRepository
import com.company.licenseserver.domain.repository.LicenseRepository
import com.company.licenseserver.domain.repository.ProductRepository

import com.company.licenseserver.security.LicenseKeyGenerator

import java.time.LocalDate


data class ProvisionResult(
    val success: Boolean,
    val licenseKey: String?,
    val status: String?,
    val expireDate: String?,
    val message: String
)


class LicenseManager(

    private val licenseRepository: LicenseRepository,

    private val deviceRepository: DeviceRepository,

    private val customerRepository: CustomerRepository,

    private val productRepository: ProductRepository

) {


    companion object {

        private const val DEFAULT_MAX_DEVICES = 2

        private const val DEFAULT_LICENSE_DAYS = 365L
    }


    fun provision(
        customerName: String,
        customerEmail: String,
        productName: String,
        productVersion: String,
        device: Device
    ): ProvisionResult {


        val cleanCustomerName =
            customerName.trim()


        val cleanCustomerEmail =
            customerEmail
                .trim()
                .lowercase()


        val cleanProductName =
            productName.trim()


        val cleanProductVersion =
            productVersion.trim()


        val cleanFingerprint =
            device.hardwareFingerprint
                .trim()
                .lowercase()


        if (
            cleanCustomerName.isBlank() ||
            cleanCustomerEmail.isBlank() ||
            cleanProductName.isBlank() ||
            cleanProductVersion.isBlank()
        ) {

            return ProvisionResult(
                success = false,
                licenseKey = null,
                status = null,
                expireDate = null,
                message = "Customer and product information is required"
            )
        }


        if (cleanFingerprint.isBlank()) {

            return ProvisionResult(
                success = false,
                licenseKey = null,
                status = null,
                expireDate = null,
                message = "Hardware fingerprint is required"
            )
        }


        val normalizedDevice =
            device.copy(
                hardwareFingerprint = cleanFingerprint
            )


        /*
         * First we only SEARCH for the customer and product.
         *
         * We intentionally do not create anything yet.
         *
         * This prevents a user from entering another email
         * for a device that already belongs to another license.
         */

        val existingCustomer =
            customerRepository.findByEmail(
                cleanCustomerEmail
            )


        val existingProduct =
            productRepository.findByNameAndVersion(
                name = cleanProductName,
                version = cleanProductVersion
            )


        /*
         * Check whether this hardware fingerprint
         * has already been registered anywhere.
         */

        val fingerprintAlreadyRegistered =
            deviceRepository.existsByFingerprint(
                cleanFingerprint
            )


        if (fingerprintAlreadyRegistered) {

            /*
             * If the email/product already exist,
             * check whether this fingerprint belongs
             * to THEIR license.
             */

            val existingLicense =

                if (
                    existingCustomer != null &&
                    existingProduct != null
                ) {

                    licenseRepository.findByCustomerAndProduct(
                        customerId = existingCustomer.id,
                        productId = existingProduct.id
                    )

                } else {

                    null
                }


            val belongsToSameLicense =

                existingLicense != null &&
                        deviceRepository.exists(
                            licenseId = existingLicense.id,
                            hardwareFingerprint = cleanFingerprint
                        )


            /*
             * Fingerprint exists, but not under this
             * customer's license.
             *
             * Therefore activation with another email
             * is rejected.
             */

            if (!belongsToSameLicense) {

                return ProvisionResult(
                    success = false,
                    licenseKey = null,
                    status = null,
                    expireDate = null,
                    message = "This device belongs to another license"
                )
            }
        }


        /*
         * Now it is safe to create or reuse Customer.
         */

        val customer =

            existingCustomer
                ?: customerRepository.save(

                    Customer(
                        id = 0,
                        name = cleanCustomerName,
                        email = cleanCustomerEmail
                    )
                )


        /*
         * Create or reuse Product.
         */

        val product =

            existingProduct
                ?: productRepository.save(

                    Product(
                        id = 0,
                        name = cleanProductName,
                        version = cleanProductVersion
                    )
                )


        /*
         * Find existing license for this
         * customer + product.
         */

        var license =

            licenseRepository.findByCustomerAndProduct(
                customerId = customer.id,
                productId = product.id
            )


        /*
         * If there is no license,
         * automatically create one.
         */

        if (license == null) {


            val expireDate =
                LocalDate
                    .now()
                    .plusDays(
                        DEFAULT_LICENSE_DAYS
                    )


            val key =
                LicenseKeyGenerator.generate(
                    customerId = customer.id,
                    productId = product.id,
                    maxDevices = DEFAULT_MAX_DEVICES,
                    expireDate = expireDate.toString()
                )


            val newLicense =
                License(
                    id = 0,
                    key = key,
                    customer = customer,
                    product = product,
                    maxDevices = DEFAULT_MAX_DEVICES,
                    expireDate = expireDate,
                    initialStatus = LicenseStatus.ACTIVE
                )


            licenseRepository.save(
                newLicense
            )


            /*
             * Read it again so we get
             * the real database-generated ID.
             */

            license =
                licenseRepository.findByKey(
                    key
                )


            if (license == null) {

                return ProvisionResult(
                    success = false,
                    licenseKey = null,
                    status = null,
                    expireDate = null,
                    message = "Could not create license"
                )
            }
        }


        /*
         * Blocked license check.
         */

        if (
            license.getStatus() !=
            LicenseStatus.ACTIVE
        ) {

            return ProvisionResult(
                success = false,
                licenseKey = license.key,
                status = license.getStatus().name,
                expireDate = license.expireDate.toString(),
                message = "License is not active"
            )
        }


        /*
         * Expiration check.
         */

        if (license.isExpired()) {

            return ProvisionResult(
                success = false,
                licenseKey = license.key,
                status = license.getStatus().name,
                expireDate = license.expireDate.toString(),
                message = "License expired"
            )
        }


        /*
         * Same license + same hardware.
         *
         * This is NOT an error.
         * The client may be opened many times
         * on the same computer.
         */

        val alreadyActivated =
            deviceRepository.exists(
                licenseId = license.id,
                hardwareFingerprint = cleanFingerprint
            )


        if (alreadyActivated) {

            return ProvisionResult(
                success = true,
                licenseKey = license.key,
                status = license.getStatus().name,
                expireDate = license.expireDate.toString(),
                message = "Device already activated"
            )
        }


        /*
         * Device limit check.
         */

        val devices =
            deviceRepository.findByLicense(
                license.id
            )


        if (
            devices.size >=
            license.getMaxDevices()
        ) {

            return ProvisionResult(
                success = false,
                licenseKey = license.key,
                status = license.getStatus().name,
                expireDate = license.expireDate.toString(),
                message = "Maximum devices reached"
            )
        }


        /*
         * Register new hardware.
         */

        deviceRepository.save(
            licenseId = license.id,
            device = normalizedDevice
        )


        return ProvisionResult(
            success = true,
            licenseKey = license.key,
            status = license.getStatus().name,
            expireDate = license.expireDate.toString(),
            message = "License activated successfully"
        )
    }
}
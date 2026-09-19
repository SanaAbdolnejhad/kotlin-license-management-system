package com.company.licenseserver


import com.company.licenseserver.data.repository.PostgresCustomerRepository
import com.company.licenseserver.data.repository.PostgresDeviceRepository
import com.company.licenseserver.data.repository.PostgresLicenseRepository
import com.company.licenseserver.data.repository.PostgresProductRepository

import com.company.licenseserver.domain.service.LicenseManager


object AppContainer {


    private val licenseRepository =
        PostgresLicenseRepository()


    val deviceRepository =
        PostgresDeviceRepository()


    val customerRepository =
        PostgresCustomerRepository()


    val productRepository =
        PostgresProductRepository()


    val licenseManager =

        LicenseManager(

            licenseRepository =
                licenseRepository,

            deviceRepository =
                deviceRepository,

            customerRepository =
                customerRepository,

            productRepository =
                productRepository
        )
}
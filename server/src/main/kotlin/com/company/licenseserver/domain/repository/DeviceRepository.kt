package com.company.licenseserver.domain.repository

import com.company.licenseserver.domain.model.Device


interface DeviceRepository {

    fun exists(
        licenseId: Int,
        hardwareFingerprint: String
    ): Boolean


    fun existsByFingerprint(
        hardwareFingerprint: String
    ): Boolean


    fun save(
        licenseId: Int,
        device: Device
    )


    fun findByLicense(
        licenseId: Int
    ): List<Device>


    fun delete(
        licenseId: Int,
        hardwareFingerprint: String
    )
}
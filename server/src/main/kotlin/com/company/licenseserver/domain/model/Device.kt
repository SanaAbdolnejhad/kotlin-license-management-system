package com.company.licenseserver.domain.model


data class Device(

    val id: Int,

    val hardwareFingerprint: String,

    val cpuId: String? = null,

    val motherboardId: String? = null,

    val diskSerial: String? = null,

    val macAddress: String? = null,

    val operatingSystem: String? = null
)
package com.company.licenseclient.api.dto

data class ProvisionRequest(
    val customerName: String,
    val customerEmail: String,
    val productName: String,
    val productVersion: String,
    val hardwareFingerprint: String,
    val cpuId: String? = null,
    val motherboardId: String? = null,
    val diskSerial: String? = null,
    val macAddress: String? = null,
    val operatingSystem: String? = null
)
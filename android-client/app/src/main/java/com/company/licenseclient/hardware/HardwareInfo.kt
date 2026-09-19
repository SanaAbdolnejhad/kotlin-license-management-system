package com.company.licenseclient.hardware

data class HardwareInfo(
    val cpuId: String,
    val motherboardId: String,
    val diskSerial: String,
    val macAddress: String,
    val operatingSystem: String
)
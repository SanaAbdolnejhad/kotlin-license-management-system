package com.company.licenseclient.hardware

import java.security.MessageDigest

object HardwareFingerprintGenerator {

    fun generate(
        info: HardwareInfo
    ): String {

        val rawData =
            normalize(info.cpuId) +
                    "|" +
                    normalize(info.motherboardId) +
                    "|" +
                    normalize(info.diskSerial) +
                    "|" +
                    normalize(info.macAddress)

        val bytes =
            MessageDigest
                .getInstance("SHA-256")
                .digest(
                    rawData.toByteArray(
                        Charsets.UTF_8
                    )
                )

        return bytes.joinToString("") { byte ->

            "%02x".format(
                byte.toInt() and 0xff
            )
        }
    }

    private fun normalize(
        value: String
    ): String {

        return value
            .trim()
            .lowercase()
    }
}
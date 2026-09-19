package com.company.licenseclient.hardware

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.net.NetworkInterface

class AndroidHardwareProvider(
    private val context: Context
) : HardwareProvider {

    override fun getHardwareInfo(): HardwareInfo {

        val androidId = getAndroidId()

        return HardwareInfo(

            cpuId = getCpuId(),

            motherboardId = getBoardId(),

            diskSerial = androidId,

            macAddress = getMacAddress(androidId),

            operatingSystem = getOperatingSystem()
        )
    }

    private fun getAndroidId(): String {

        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    private fun getCpuId(): String {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            Build.SOC_MODEL

        } else {

            System.getProperty("os.arch")
                ?: Build.SUPPORTED_ABIS.joinToString("_")
        }
    }

    private fun getBoardId(): String {

        return "${Build.MANUFACTURER}-${Build.BOARD}-${Build.HARDWARE}"
    }

    private fun getMacAddress(
        fallback: String
    ): String {

        return try {

            val interfaces =
                NetworkInterface
                    .getNetworkInterfaces()
                    .toList()

            for (networkInterface in interfaces) {

                val address =
                    networkInterface.hardwareAddress
                        ?: continue

                if (address.isEmpty()) {
                    continue
                }

                return address.joinToString(":") {
                    "%02x".format(
                        it.toInt() and 0xff
                    )
                }
            }

            fallback

        } catch (exception: Exception) {

            fallback
        }
    }

    private fun getOperatingSystem(): String {

        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }
}
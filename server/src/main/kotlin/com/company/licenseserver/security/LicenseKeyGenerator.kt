package com.company.licenseserver.security


import java.util.UUID


object LicenseKeyGenerator {


    fun generate(
        customerId: Int,
        productId: Int,
        maxDevices: Int,
        expireDate: String
    ): String {


        val uniquePart =
            UUID.randomUUID()
                .toString()


        val rawData =
            "$customerId:$productId:$maxDevices:$expireDate:$uniquePart"


        val hash =
            HashUtil.sha256(
                rawData
            )


        return "LIC-${hash.take(24).uppercase()}"
    }
}
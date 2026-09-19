package com.company.licenseclient.api

import com.company.licenseclient.api.dto.ProvisionRequest
import com.company.licenseclient.api.dto.ProvisionResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LicenseApi(
    baseUrl: String
) {

    private val service: LicenseService

    init {

        val finalBaseUrl =
            if (baseUrl.endsWith("/")) {
                baseUrl
            } else {
                "$baseUrl/"
            }

        service =
            Retrofit.Builder()
                .baseUrl(finalBaseUrl)
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()
                .create(
                    LicenseService::class.java
                )
    }

    suspend fun provision(
        request: ProvisionRequest
    ): ProvisionResponse {

        return service.provision(
            request
        )
    }
}
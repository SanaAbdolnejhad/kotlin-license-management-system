package com.company.licenseclient.api

import com.company.licenseclient.api.dto.ProvisionRequest
import com.company.licenseclient.api.dto.ProvisionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LicenseService {

    @POST("license/provision")
    suspend fun provision(
        @Body request: ProvisionRequest
    ): ProvisionResponse
}
package com.company.licenseclient.api.dto

data class ProvisionResponse(
    val success: Boolean,
    val licenseKey: String? = null,
    val status: String? = null,
    val expireDate: String? = null,
    val message: String
)
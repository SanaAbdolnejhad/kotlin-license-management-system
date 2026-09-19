package com.company.licenseserver.api.dto


import kotlinx.serialization.Serializable


@Serializable
data class ProvisionResponse(

    val success: Boolean,

    val licenseKey: String? = null,

    val status: String? = null,

    val expireDate: String? = null,

    val message: String

)
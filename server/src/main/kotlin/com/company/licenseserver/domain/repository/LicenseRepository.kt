package com.company.licenseserver.domain.repository


import com.company.licenseserver.domain.model.License


interface LicenseRepository {


    fun save(
        license: License
    )


    fun findByKey(
        key: String
    ): License?


    fun findByCustomerAndProduct(
        customerId: Int,
        productId: Int
    ): License?


    fun findAll(): List<License>


    fun delete(
        license: License
    )


    fun count(): Int


    fun updateStatus(
        key: String,
        status: String
    )

}
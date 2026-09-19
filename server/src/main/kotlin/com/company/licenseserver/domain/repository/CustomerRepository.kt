package com.company.licenseserver.domain.repository


import com.company.licenseserver.domain.model.Customer


interface CustomerRepository {


    fun save(
        customer: Customer
    ): Customer


    fun findById(
        id: Int
    ): Customer?


    fun findByEmail(
        email: String
    ): Customer?


    fun findAll(): List<Customer>


    fun delete(
        customer: Customer
    )

}
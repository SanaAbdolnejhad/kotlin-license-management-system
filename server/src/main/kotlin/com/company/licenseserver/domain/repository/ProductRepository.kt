package com.company.licenseserver.domain.repository


import com.company.licenseserver.domain.model.Product


interface ProductRepository {


    fun save(
        product: Product
    ): Product


    fun findById(
        id: Int
    ): Product?


    fun findByNameAndVersion(
        name: String,
        version: String
    ): Product?


    fun findAll(): List<Product>


    fun delete(
        product: Product
    )

}
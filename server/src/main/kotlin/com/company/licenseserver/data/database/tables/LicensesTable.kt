package com.company.licenseserver.data.database.tables


import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date



object LicensesTable : Table("licenses") {



    val id =
        integer("id")
            .autoIncrement()



    val key =
        varchar(
            "license_key",
            200
        )



    val customerId =
        integer(
            "customer_id"
        )
            .references(
                CustomersTable.id
            )



    val productId =
        integer(
            "product_id"
        )
            .references(
                ProductsTable.id
            )



    val maxDevices =
        integer(
            "max_devices"
        )



    val status =
        varchar(
            "status",
            30
        )



    val expireDate =
        date(
            "expire_date"
        )



    override val primaryKey =
        PrimaryKey(id)

}
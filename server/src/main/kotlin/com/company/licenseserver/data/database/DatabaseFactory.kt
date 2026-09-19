package com.company.licenseserver.data.database


import com.company.licenseserver.data.database.tables.CustomersTable
import com.company.licenseserver.data.database.tables.DevicesTable
import com.company.licenseserver.data.database.tables.LicensesTable
import com.company.licenseserver.data.database.tables.ProductsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction



object DatabaseFactory {


    fun init(){

        val dbPassword = System.getenv("DB_PASSWORD")
            ?: error("DB_PASSWORD environment variable is not set")


        Database.connect(

            url = "jdbc:postgresql://localhost:5432/license_system",

            driver = "org.postgresql.Driver",

            user = "postgres",

            password = dbPassword

        )



        transaction {


            SchemaUtils.create(

                CustomersTable,

                ProductsTable,

                LicensesTable,

                DevicesTable

            )


        }


    }

}
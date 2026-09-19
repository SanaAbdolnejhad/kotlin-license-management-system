package com.company.licenseserver.data.database.tables



import org.jetbrains.exposed.sql.Table


object CustomersTable : Table("customers") {


    val id =
        integer("id")
            .autoIncrement()


    val name =
        varchar(
            "name",
            100
        )


    val email =
        varchar(
            "email",
            100
        )


    override val primaryKey =
        PrimaryKey(id)

}
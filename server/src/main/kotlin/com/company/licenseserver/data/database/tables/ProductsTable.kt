package com.company.licenseserver.data.database.tables



import org.jetbrains.exposed.sql.Table


object ProductsTable : Table("products") {


    val id =
        integer("id")
            .autoIncrement()


    val name =
        varchar(
            "name",
            100
        )


    val version =
        varchar(
            "version",
            50
        )


    override val primaryKey =
        PrimaryKey(id)

}
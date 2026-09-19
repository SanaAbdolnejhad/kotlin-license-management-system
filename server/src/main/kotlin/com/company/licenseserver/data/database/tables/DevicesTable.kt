package com.company.licenseserver.data.database.tables


import org.jetbrains.exposed.sql.Table


object DevicesTable : Table("devices") {


    val id =
        integer("id")
            .autoIncrement()


    val licenseId =
        integer("license_id")


    val hardwareFingerprint =
        varchar(
            "hardware_fingerprint",
            200
        )


    val cpuId =
        varchar(
            "cpu_id",
            200
        )
            .nullable()


    val motherboardId =
        varchar(
            "motherboard_id",
            200
        )
            .nullable()


    val diskSerial =
        varchar(
            "disk_serial",
            200
        )
            .nullable()


    val macAddress =
        varchar(
            "mac_address",
            200
        )
            .nullable()


    val operatingSystem =
        varchar(
            "operating_system",
            100
        )
            .nullable()


    override val primaryKey =
        PrimaryKey(id)


    init {

        uniqueIndex(
            licenseId,
            hardwareFingerprint
        )
    }
}
package com.company.licenseserver.data.repository

import com.company.licenseserver.data.database.tables.DevicesTable
import com.company.licenseserver.domain.model.Device
import com.company.licenseserver.domain.repository.DeviceRepository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class PostgresDeviceRepository : DeviceRepository {


    override fun exists(
        licenseId: Int,
        hardwareFingerprint: String
    ): Boolean {

        return transaction {

            DevicesTable
                .selectAll()
                .where {
                    (DevicesTable.licenseId eq licenseId) and
                            (DevicesTable.hardwareFingerprint eq hardwareFingerprint)
                }
                .count() > 0
        }
    }


    override fun existsByFingerprint(
        hardwareFingerprint: String
    ): Boolean {

        return transaction {

            DevicesTable
                .selectAll()
                .where {
                    DevicesTable.hardwareFingerprint eq hardwareFingerprint
                }
                .count() > 0
        }
    }


    override fun save(
        licenseId: Int,
        device: Device
    ) {

        transaction {

            DevicesTable.insert {

                it[DevicesTable.licenseId] =
                    licenseId

                it[hardwareFingerprint] =
                    device.hardwareFingerprint

                it[cpuId] =
                    device.cpuId

                it[motherboardId] =
                    device.motherboardId

                it[diskSerial] =
                    device.diskSerial

                it[macAddress] =
                    device.macAddress

                it[operatingSystem] =
                    device.operatingSystem
            }
        }
    }


    override fun findByLicense(
        licenseId: Int
    ): List<Device> {

        return transaction {

            DevicesTable
                .selectAll()
                .where {
                    DevicesTable.licenseId eq licenseId
                }
                .map {

                    Device(
                        id = it[DevicesTable.id],

                        hardwareFingerprint =
                            it[DevicesTable.hardwareFingerprint],

                        cpuId =
                            it[DevicesTable.cpuId],

                        motherboardId =
                            it[DevicesTable.motherboardId],

                        diskSerial =
                            it[DevicesTable.diskSerial],

                        macAddress =
                            it[DevicesTable.macAddress],

                        operatingSystem =
                            it[DevicesTable.operatingSystem]
                    )
                }
        }
    }


    override fun delete(
        licenseId: Int,
        hardwareFingerprint: String
    ) {

        transaction {

            DevicesTable.deleteWhere {

                (DevicesTable.licenseId eq licenseId) and
                        (DevicesTable.hardwareFingerprint eq hardwareFingerprint)
            }
        }
    }
}
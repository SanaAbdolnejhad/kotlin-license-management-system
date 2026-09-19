package com.company.licenseserver.data.repository


import com.company.licenseserver.data.database.tables.CustomersTable
import com.company.licenseserver.data.database.tables.LicensesTable
import com.company.licenseserver.data.database.tables.ProductsTable

import com.company.licenseserver.domain.model.Customer
import com.company.licenseserver.domain.model.License
import com.company.licenseserver.domain.model.LicenseStatus
import com.company.licenseserver.domain.model.Product

import com.company.licenseserver.domain.repository.LicenseRepository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class PostgresLicenseRepository : LicenseRepository {


    override fun save(
        license: License
    ) {

        transaction {

            LicensesTable.insert {

                it[key] =
                    license.key

                it[customerId] =
                    license.customer.id

                it[productId] =
                    license.product.id

                it[maxDevices] =
                    license.getMaxDevices()

                it[status] =
                    license.getStatus().name

                it[expireDate] =
                    license.expireDate
            }
        }
    }


    override fun findByKey(
        key: String
    ): License? {

        return transaction {

            val row =
                LicensesTable
                    .selectAll()
                    .where {
                        LicensesTable.key eq key
                    }
                    .singleOrNull()

            if (row == null) {
                null
            } else {
                mapLicense(row)
            }
        }
    }


    override fun findByCustomerAndProduct(
        customerId: Int,
        productId: Int
    ): License? {

        return transaction {

            val row =
                LicensesTable
                    .selectAll()
                    .where {

                        (LicensesTable.customerId eq customerId) and
                                (LicensesTable.productId eq productId)

                    }
                    .orderBy(
                        LicensesTable.id,
                        SortOrder.DESC
                    )
                    .limit(1)
                    .singleOrNull()

            if (row == null) {
                null
            } else {
                mapLicense(row)
            }
        }
    }


    override fun findAll(): List<License> {

        return transaction {

            LicensesTable
                .selectAll()
                .map { row ->

                    mapLicense(row)

                }
        }
    }


    override fun delete(
        license: License
    ) {

        transaction {

            LicensesTable.deleteWhere {

                LicensesTable.key eq license.key

            }
        }
    }


    override fun count(): Int {

        return transaction {

            LicensesTable
                .selectAll()
                .count()
                .toInt()
        }
    }


    override fun updateStatus(
        key: String,
        status: String
    ) {

        transaction {

            LicensesTable.update(
                where = {
                    LicensesTable.key eq key
                }
            ) {

                it[LicensesTable.status] =
                    status
            }
        }
    }


    private fun mapLicense(
        row: ResultRow
    ): License {


        val customerRow =
            CustomersTable
                .selectAll()
                .where {

                    CustomersTable.id eq
                            row[LicensesTable.customerId]

                }
                .single()


        val productRow =
            ProductsTable
                .selectAll()
                .where {

                    ProductsTable.id eq
                            row[LicensesTable.productId]

                }
                .single()


        return License(

            id =
                row[LicensesTable.id],

            key =
                row[LicensesTable.key],

            customer =
                Customer(

                    id =
                        customerRow[CustomersTable.id],

                    name =
                        customerRow[CustomersTable.name],

                    email =
                        customerRow[CustomersTable.email]
                ),

            product =
                Product(

                    id =
                        productRow[ProductsTable.id],

                    name =
                        productRow[ProductsTable.name],

                    version =
                        productRow[ProductsTable.version]
                ),

            maxDevices =
                row[LicensesTable.maxDevices],

            expireDate =
                row[LicensesTable.expireDate],

            initialStatus =
                LicenseStatus.valueOf(
                    row[LicensesTable.status]
                )
        )
    }
}
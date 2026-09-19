package com.company.licenseserver.data.repository


import com.company.licenseserver.data.database.tables.CustomersTable
import com.company.licenseserver.domain.model.Customer
import com.company.licenseserver.domain.repository.CustomerRepository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class PostgresCustomerRepository : CustomerRepository {


    override fun save(
        customer: Customer
    ): Customer {


        return transaction {


            val existing =

                CustomersTable
                    .selectAll()
                    .where {

                        CustomersTable.email eq customer.email

                    }
                    .singleOrNull()


            if (existing != null) {

                Customer(

                    id =
                        existing[CustomersTable.id],

                    name =
                        existing[CustomersTable.name],

                    email =
                        existing[CustomersTable.email]

                )

            } else {


                val newId =

                    CustomersTable.insert {


                        it[name] =
                            customer.name


                        it[email] =
                            customer.email


                    } get CustomersTable.id


                Customer(

                    id = newId,

                    name = customer.name,

                    email = customer.email

                )

            }

        }

    }





    override fun findById(
        id: Int
    ): Customer? {


        return transaction {


            CustomersTable
                .selectAll()
                .where {

                    CustomersTable.id eq id

                }
                .singleOrNull()
                ?.let {


                    Customer(

                        id =
                            it[CustomersTable.id],

                        name =
                            it[CustomersTable.name],

                        email =
                            it[CustomersTable.email]

                    )

                }

        }

    }





    override fun findByEmail(
        email: String
    ): Customer? {


        return transaction {


            CustomersTable
                .selectAll()
                .where {

                    CustomersTable.email eq email

                }
                .singleOrNull()
                ?.let {


                    Customer(

                        id =
                            it[CustomersTable.id],

                        name =
                            it[CustomersTable.name],

                        email =
                            it[CustomersTable.email]

                    )

                }

        }

    }





    override fun findAll(): List<Customer> {


        return transaction {


            CustomersTable
                .selectAll()
                .map {


                    Customer(

                        id =
                            it[CustomersTable.id],

                        name =
                            it[CustomersTable.name],

                        email =
                            it[CustomersTable.email]

                    )

                }

        }

    }





    override fun delete(
        customer: Customer
    ) {


        transaction {


            CustomersTable.deleteWhere {

                CustomersTable.id eq customer.id

            }

        }

    }

}
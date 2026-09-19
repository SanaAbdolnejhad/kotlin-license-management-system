package com.company.licenseserver.data.repository


import com.company.licenseserver.data.database.tables.ProductsTable
import com.company.licenseserver.domain.model.Product
import com.company.licenseserver.domain.repository.ProductRepository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class PostgresProductRepository : ProductRepository {


    override fun save(
        product: Product
    ): Product {


        return transaction {


            val existing =

                ProductsTable
                    .selectAll()
                    .where {

                        (ProductsTable.name eq product.name) and
                                (ProductsTable.version eq product.version)

                    }
                    .singleOrNull()


            if (existing != null) {

                Product(

                    id =
                        existing[ProductsTable.id],

                    name =
                        existing[ProductsTable.name],

                    version =
                        existing[ProductsTable.version]

                )

            } else {


                val newId =

                    ProductsTable.insert {


                        it[name] =
                            product.name


                        it[version] =
                            product.version


                    } get ProductsTable.id


                Product(

                    id = newId,

                    name = product.name,

                    version = product.version

                )

            }

        }

    }





    override fun findById(
        id: Int
    ): Product? {


        return transaction {


            ProductsTable
                .selectAll()
                .where {

                    ProductsTable.id eq id

                }
                .singleOrNull()
                ?.let {


                    Product(

                        id =
                            it[ProductsTable.id],

                        name =
                            it[ProductsTable.name],

                        version =
                            it[ProductsTable.version]

                    )

                }

        }

    }





    override fun findByNameAndVersion(
        name: String,
        version: String
    ): Product? {


        return transaction {


            ProductsTable
                .selectAll()
                .where {

                    (ProductsTable.name eq name) and
                            (ProductsTable.version eq version)

                }
                .singleOrNull()
                ?.let {


                    Product(

                        id =
                            it[ProductsTable.id],

                        name =
                            it[ProductsTable.name],

                        version =
                            it[ProductsTable.version]

                    )

                }

        }

    }





    override fun findAll(): List<Product> {


        return transaction {


            ProductsTable
                .selectAll()
                .map {


                    Product(

                        id =
                            it[ProductsTable.id],

                        name =
                            it[ProductsTable.name],

                        version =
                            it[ProductsTable.version]

                    )

                }

        }

    }





    override fun delete(
        product: Product
    ) {


        transaction {


            ProductsTable.deleteWhere {

                ProductsTable.id eq product.id

            }

        }

    }

}
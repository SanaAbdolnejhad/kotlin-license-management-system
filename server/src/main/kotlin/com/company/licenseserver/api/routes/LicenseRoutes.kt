package com.company.licenseserver.api.routes


import com.company.licenseserver.AppContainer

import com.company.licenseserver.api.dto.ProvisionRequest
import com.company.licenseserver.api.dto.ProvisionResponse

import com.company.licenseserver.domain.model.Device

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.licenseRoutes() {


    routing {


        get("/") {

            call.respond(
                "License Server Running"
            )
        }


        post("/license/provision") {


            val request =
                call.receive<ProvisionRequest>()


            val device =

                Device(

                    id = 0,

                    hardwareFingerprint =
                        request.hardwareFingerprint,

                    cpuId =
                        request.cpuId,

                    motherboardId =
                        request.motherboardId,

                    diskSerial =
                        request.diskSerial,

                    macAddress =
                        request.macAddress,

                    operatingSystem =
                        request.operatingSystem
                )


            val result =

                AppContainer
                    .licenseManager
                    .provision(

                        customerName =
                            request.customerName,

                        customerEmail =
                            request.customerEmail,

                        productName =
                            request.productName,

                        productVersion =
                            request.productVersion,

                        device =
                            device
                    )


            call.respond(

                ProvisionResponse(

                    success =
                        result.success,

                    licenseKey =
                        result.licenseKey,

                    status =
                        result.status,

                    expireDate =
                        result.expireDate,

                    message =
                        result.message
                )
            )
        }
    }
}
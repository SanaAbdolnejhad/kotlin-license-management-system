package com.company.licenseserver

import com.company.licenseserver.data.database.DatabaseFactory
import com.company.licenseserver.api.routes.licenseRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.respondText

fun main() {

    embeddedServer(
        Netty,
        port = 8080,
        module = Application::module
    ).start(wait = true)

}


fun Application.module() {


    DatabaseFactory.init()


    install(ContentNegotiation){

        json()


    }
    install(StatusPages){

        exception<Throwable>{ call, cause ->

            cause.printStackTrace()

            call.respondText(
                text = cause.message ?: "Unknown error",
                status = io.ktor.http.HttpStatusCode.InternalServerError
            )

        }

    }

    licenseRoutes()

}
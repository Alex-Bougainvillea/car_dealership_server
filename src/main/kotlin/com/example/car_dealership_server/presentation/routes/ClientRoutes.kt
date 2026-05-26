package com.example.car_dealership_server.presentation.routes

import com.example.car_dealership_server.domain.usecases.ClientService
import com.example.car_dealership_server.presentation.dto.ClientCreateRequest
import com.example.car_dealership_server.presentation.dto.ClientResponse
import com.example.car_dealership_server.presentation.dto.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerClientRoutes(service: ClientService) {
    authenticate("auth-jwt") {
        route("/clients") {
            get {
                if (!call.requireAdmin()) return@get
                val clients = service.getAll().map { it.toResponse() }
                call.respond(clients)
            }
            post {
                if (!call.requireAdmin()) return@post
                val request = call.receive<ClientCreateRequest>()
                val client = service.create(
                    firstName = request.firstName,
                    lastName = request.lastName,
                    phone = request.phone,
                    email = request.email
                )
                call.respond(HttpStatusCode.Created, client.toResponse())
            }
        }
    }
}

private fun com.example.car_dealership_server.domain.models.Client.toResponse(): ClientResponse =
    ClientResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        email = email
    )

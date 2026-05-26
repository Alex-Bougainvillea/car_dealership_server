package com.example.car_dealership_server.presentation.routes

import com.example.car_dealership_server.domain.usecases.PurchaseRequestService
import com.example.car_dealership_server.presentation.dto.ErrorResponse
import com.example.car_dealership_server.presentation.dto.PurchaseRequestCreateRequest
import com.example.car_dealership_server.presentation.dto.PurchaseRequestResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerRequestRoutes(service: PurchaseRequestService) {
    authenticate("auth-jwt") {
        route("/requests") {
            get {
                if (!call.requireAdmin()) return@get
                val requests = service.getAll().map { it.toResponse() }
                call.respond(requests)
            }
            post {
                if (!call.requireAdmin()) return@post
                val request = call.receive<PurchaseRequestCreateRequest>()
                val created = service.create(request.clientId, request.carId)
                    ?: return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid client or car"))
                call.respond(HttpStatusCode.Created, created.toResponse())
            }
        }
    }
}

private fun com.example.car_dealership_server.domain.models.PurchaseRequest.toResponse(): PurchaseRequestResponse =
    PurchaseRequestResponse(
        id = id,
        clientId = clientId,
        carId = carId,
        createdAt = createdAt.toString()
    )

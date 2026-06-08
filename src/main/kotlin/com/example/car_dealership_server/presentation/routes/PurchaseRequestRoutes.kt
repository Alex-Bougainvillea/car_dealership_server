package com.example.car_dealership_server.presentation.routes

import com.example.car_dealership_server.domain.repositories.PurchaseRequestCreateResult
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
import io.ktor.server.routing.delete
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
                when (val result = service.create(request.clientId, request.carId)) {
                    is PurchaseRequestCreateResult.Success -> {
                        call.respond(HttpStatusCode.Created, result.request.toResponse())
                    }
                    PurchaseRequestCreateResult.ClientNotFound -> {
                        call.respond(HttpStatusCode.BadRequest, ErrorResponse("Клиент с ID ${request.clientId} не найден"))
                    }
                    PurchaseRequestCreateResult.CarNotFound -> {
                        call.respond(HttpStatusCode.BadRequest, ErrorResponse("Автомобиль с ID ${request.carId} не найден"))
                    }
                    PurchaseRequestCreateResult.CarAlreadySold -> {
                        call.respond(HttpStatusCode.BadRequest, ErrorResponse("Автомобиль с ID ${request.carId} уже продан"))
                    }
                }
            }
            delete("/{id}") {
                if (!call.requireAdmin()) return@delete
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid id"))
                val deleted = service.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Заявка с ID $id не найдена"))
                }
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

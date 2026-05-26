package com.example.car_dealership_server.presentation.routes

import com.example.car_dealership_server.domain.models.CarStatus
import com.example.car_dealership_server.domain.usecases.CarService
import com.example.car_dealership_server.presentation.dto.CarCreateRequest
import com.example.car_dealership_server.presentation.dto.CarResponse
import com.example.car_dealership_server.presentation.dto.CarUpdateRequest
import com.example.car_dealership_server.presentation.dto.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerCarRoutes(service: CarService) {
    authenticate("auth-jwt") {
        route("/cars") {
            get {
                val cars = service.getAll().map { it.toResponse() }
                call.respond(cars)
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid id"))
                val car = service.getById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, ErrorResponse("Car not found"))
                call.respond(car.toResponse())
            }
            post {
                if (!call.requireAdmin()) return@post
                val request = call.receive<CarCreateRequest>()
                val car = service.create(
                    brand = request.brand,
                    model = request.model,
                    year = request.year,
                    price = request.price,
                    description = request.description,
                    posterUrl = request.posterUrl
                )
                call.respond(HttpStatusCode.Created, car.toResponse())
            }
            put("/{id}") {
                if (!call.requireAdmin()) return@put
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid id"))
                val request = call.receive<CarUpdateRequest>()
                val status = runCatching { CarStatus.valueOf(request.status.uppercase()) }.getOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid status"))
                val car = service.update(
                    id = id,
                    brand = request.brand,
                    model = request.model,
                    year = request.year,
                    price = request.price,
                    status = status,
                    description = request.description,
                    posterUrl = request.posterUrl
                ) ?: return@put call.respond(HttpStatusCode.NotFound, ErrorResponse("Car not found"))
                call.respond(car.toResponse())
            }
            delete("/{id}") {
                if (!call.requireAdmin()) return@delete
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid id"))
                val deleted = service.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Car not found"))
                }
            }
        }
    }
}

private fun com.example.car_dealership_server.domain.models.Car.toResponse(): CarResponse =
    CarResponse(
        id = id,
        brand = brand,
        model = model,
        year = year,
        price = price,
        status = status.name,
        description = description,
        posterUrl = posterUrl
    )

package com.example.car_dealership_server.presentation.routes

import com.example.car_dealership_server.domain.models.UserRole
import com.example.car_dealership_server.domain.usecases.AuthResult
import com.example.car_dealership_server.domain.usecases.AuthService
import com.example.car_dealership_server.presentation.dto.AuthResponse
import com.example.car_dealership_server.presentation.dto.ErrorResponse
import com.example.car_dealership_server.presentation.dto.LoginRequest
import com.example.car_dealership_server.presentation.dto.RegisterRequest
import com.example.car_dealership_server.presentation.dto.UserResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAuthRoutes(authService: AuthService) {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val role = if (request.role == null) {
                UserRole.CLIENT
            } else {
                val parsed = runCatching { UserRole.valueOf(request.role.uppercase()) }.getOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid role"))
                parsed
            }

            when (val result = authService.register(request.username, request.password, role)) {
                is AuthResult.Success -> {
                    val user = result.user
                    call.respond(
                        AuthResponse(
                            token = result.token,
                            user = UserResponse(user.id, user.username, user.role.name)
                        )
                    )
                }
                is AuthResult.Error -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(result.message))
            }
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            when (val result = authService.login(request.username, request.password)) {
                is AuthResult.Success -> {
                    val user = result.user
                    call.respond(
                        AuthResponse(
                            token = result.token,
                            user = UserResponse(user.id, user.username, user.role.name)
                        )
                    )
                }
                is AuthResult.Error -> call.respond(HttpStatusCode.Unauthorized, ErrorResponse(result.message))
            }
        }
    }
}

package com.example.car_dealership_server.presentation.routes

import com.example.car_dealership_server.domain.models.UserRole
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.response.respond
import io.ktor.server.auth.principal

suspend fun ApplicationCall.requireAdmin(): Boolean {
    val principal = principal<JWTPrincipal>() ?: run {
        respond(HttpStatusCode.Unauthorized)
        return false
    }
    val role = principal.payload.getClaim("role").asString()
    return if (role == UserRole.ADMIN.name) {
        true
    } else {
        respond(HttpStatusCode.Forbidden)
        false
    }
}

fun ApplicationCall.userRole(): UserRole? {
    val principal = principal<JWTPrincipal>() ?: return null
    val role = principal.payload.getClaim("role").asString() ?: return null
    return runCatching { UserRole.valueOf(role) }.getOrNull()
}

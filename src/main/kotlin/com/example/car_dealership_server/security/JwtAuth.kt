package com.example.car_dealership_server.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.ApplicationConfig

fun Application.configureJwt(config: ApplicationConfig) {
    val jwtConfig = JwtConfig.fromConfig(config)
    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.realm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtConfig.secret))
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val role = credential.payload.getClaim("role").asString()
                if (userId != null && role != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

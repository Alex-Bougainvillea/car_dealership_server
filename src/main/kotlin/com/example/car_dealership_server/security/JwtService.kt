package com.example.car_dealership_server.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.car_dealership_server.domain.models.User
import io.ktor.server.config.ApplicationConfig
import java.util.Date

data class JwtConfig(
    val issuer: String,
    val audience: String,
    val realm: String,
    val secret: String,
    val expirationMinutes: Long
) {
    companion object {
        fun fromConfig(config: ApplicationConfig): JwtConfig {
            val jwtConfig = config.config("jwt")
            return JwtConfig(
                issuer = jwtConfig.property("issuer").getString(),
                audience = jwtConfig.property("audience").getString(),
                realm = jwtConfig.property("realm").getString(),
                secret = jwtConfig.property("secret").getString(),
                expirationMinutes = jwtConfig.property("expirationMinutes").getString().toLong()
            )
        }
    }
}

class JwtService(private val config: JwtConfig) {
    fun generateToken(user: User): String {
        val expiresAt = Date(System.currentTimeMillis() + config.expirationMinutes * 60_000)
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("userId", user.id)
            .withClaim("role", user.role.name)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC256(config.secret))
    }

    companion object {
        fun fromConfig(config: ApplicationConfig): JwtService =
            JwtService(JwtConfig.fromConfig(config))
    }
}

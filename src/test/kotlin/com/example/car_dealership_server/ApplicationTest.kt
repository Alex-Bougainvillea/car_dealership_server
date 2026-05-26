package com.example.car_dealership_server

import com.example.car_dealership_server.presentation.dto.AuthResponse
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class ApplicationTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun registerAndListCars() = testApplication {
        environment { config = ApplicationConfig("application-test.conf") }
        application { module() }

        val registerResponse = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"admin","password":"pass1234","role":"ADMIN"}""")
        }
        assertEquals(HttpStatusCode.OK, registerResponse.status)

        val authResponse = json.decodeFromString(
            AuthResponse.serializer(),
            registerResponse.bodyAsText()
        )

        val carsResponse = client.get("/cars") {
            headers.append(HttpHeaders.Authorization, "Bearer ${authResponse.token}")
        }
        assertEquals(HttpStatusCode.OK, carsResponse.status)
    }
}

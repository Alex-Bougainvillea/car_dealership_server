package com.example.car_dealership_server.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientCreateRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String
)

@Serializable
data class ClientResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String
)

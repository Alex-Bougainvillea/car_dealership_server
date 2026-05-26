package com.example.car_dealership_server.domain.models

data class Client(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String
)

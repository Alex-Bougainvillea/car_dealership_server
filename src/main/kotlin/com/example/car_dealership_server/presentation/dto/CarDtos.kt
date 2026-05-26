package com.example.car_dealership_server.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarCreateRequest(
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val description: String? = null,
    val posterUrl: String? = null
)

@Serializable
data class CarUpdateRequest(
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val status: String,
    val description: String? = null,
    val posterUrl: String? = null
)

@Serializable
data class CarResponse(
    val id: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val status: String,
    val description: String? = null,
    val posterUrl: String? = null
)

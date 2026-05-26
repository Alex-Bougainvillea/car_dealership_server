package com.example.car_dealership_server.domain.models

data class Car(
    val id: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val status: CarStatus,
    val description: String?,
    val posterUrl: String?
)

enum class CarStatus {
    AVAILABLE,
    SOLD
}

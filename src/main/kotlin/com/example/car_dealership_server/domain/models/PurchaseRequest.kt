package com.example.car_dealership_server.domain.models

import java.time.LocalDateTime

data class PurchaseRequest(
    val id: Int,
    val clientId: Int,
    val carId: Int,
    val createdAt: LocalDateTime
)

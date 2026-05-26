package com.example.car_dealership_server.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequestCreateRequest(
    val clientId: Int,
    val carId: Int
)

@Serializable
data class PurchaseRequestResponse(
    val id: Int,
    val clientId: Int,
    val carId: Int,
    val createdAt: String
)

package com.example.car_dealership_server.domain.repositories

import com.example.car_dealership_server.domain.models.PurchaseRequest

interface PurchaseRequestRepository {
    suspend fun getAll(): List<PurchaseRequest>
    suspend fun createAndMarkSold(clientId: Int, carId: Int): PurchaseRequest?
}

package com.example.car_dealership_server.domain.repositories

import com.example.car_dealership_server.domain.models.PurchaseRequest

sealed class PurchaseRequestCreateResult {
    data class Success(val request: PurchaseRequest) : PurchaseRequestCreateResult()
    data object ClientNotFound : PurchaseRequestCreateResult()
    data object CarNotFound : PurchaseRequestCreateResult()
    data object CarAlreadySold : PurchaseRequestCreateResult()
}

interface PurchaseRequestRepository {
    suspend fun getAll(): List<PurchaseRequest>
    suspend fun createAndMarkSold(clientId: Int, carId: Int): PurchaseRequestCreateResult
    suspend fun deleteAndMarkAvailable(id: Int): Boolean
}

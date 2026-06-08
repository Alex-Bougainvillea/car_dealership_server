package com.example.car_dealership_server.domain.usecases

import com.example.car_dealership_server.domain.models.PurchaseRequest
import com.example.car_dealership_server.domain.repositories.PurchaseRequestCreateResult
import com.example.car_dealership_server.domain.repositories.PurchaseRequestRepository

class PurchaseRequestService(private val repository: PurchaseRequestRepository) {
    suspend fun getAll(): List<PurchaseRequest> = repository.getAll()

    suspend fun create(clientId: Int, carId: Int): PurchaseRequestCreateResult =
        repository.createAndMarkSold(clientId, carId)

    suspend fun delete(id: Int): Boolean = repository.deleteAndMarkAvailable(id)
}

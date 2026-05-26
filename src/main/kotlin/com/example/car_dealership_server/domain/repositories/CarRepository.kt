package com.example.car_dealership_server.domain.repositories

import com.example.car_dealership_server.domain.models.Car
import com.example.car_dealership_server.domain.models.CarStatus

interface CarRepository {
    suspend fun getAll(): List<Car>
    suspend fun getById(id: Int): Car?
    suspend fun create(
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car

    suspend fun update(
        id: Int,
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car?

    suspend fun delete(id: Int): Boolean
    suspend fun updateStatus(id: Int, status: CarStatus): Boolean
}

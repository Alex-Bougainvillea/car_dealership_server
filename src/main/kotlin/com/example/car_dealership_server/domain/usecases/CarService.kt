package com.example.car_dealership_server.domain.usecases

import com.example.car_dealership_server.domain.models.Car
import com.example.car_dealership_server.domain.models.CarStatus
import com.example.car_dealership_server.domain.repositories.CarRepository

class CarService(private val repository: CarRepository) {
    suspend fun getAll(): List<Car> = repository.getAll()

    suspend fun getById(id: Int): Car? = repository.getById(id)

    suspend fun create(
        brand: String,
        model: String,
        year: Int,
        price: Double,
        description: String?,
        posterUrl: String?
    ): Car = repository.create(
        brand = brand,
        model = model,
        year = year,
        price = price,
        status = CarStatus.AVAILABLE,
        description = description,
        posterUrl = posterUrl
    )

    suspend fun update(
        id: Int,
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car? = repository.update(id, brand, model, year, price, status, description, posterUrl)

    suspend fun delete(id: Int): Boolean = repository.delete(id)
}

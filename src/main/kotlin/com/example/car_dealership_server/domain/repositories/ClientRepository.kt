package com.example.car_dealership_server.domain.repositories

import com.example.car_dealership_server.domain.models.Client

interface ClientRepository {
    suspend fun getAll(): List<Client>
    suspend fun getById(id: Int): Client?
    suspend fun create(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ): Client
}

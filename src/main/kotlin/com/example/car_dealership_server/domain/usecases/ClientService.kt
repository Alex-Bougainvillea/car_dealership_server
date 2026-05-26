package com.example.car_dealership_server.domain.usecases

import com.example.car_dealership_server.domain.models.Client
import com.example.car_dealership_server.domain.repositories.ClientRepository

class ClientService(private val repository: ClientRepository) {
    suspend fun getAll(): List<Client> = repository.getAll()

    suspend fun getById(id: Int): Client? = repository.getById(id)

    suspend fun create(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ): Client = repository.create(firstName, lastName, phone, email)
}

package com.example.car_dealership_server.data.repositories

import com.example.car_dealership_server.data.database.ClientsTable
import com.example.car_dealership_server.data.database.DatabaseFactory
import com.example.car_dealership_server.domain.models.Client
import com.example.car_dealership_server.domain.repositories.ClientRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ClientRepositoryImpl : ClientRepository {
    override suspend fun getAll(): List<Client> = DatabaseFactory.dbQuery {
        ClientsTable.selectAll().map { it.toClient() }
    }

    override suspend fun getById(id: Int): Client? = DatabaseFactory.dbQuery {
        ClientsTable.select { ClientsTable.id eq id }.singleOrNull()?.toClient()
    }

    override suspend fun create(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ): Client = DatabaseFactory.dbQuery {
        val id = ClientsTable.insert {
            it[ClientsTable.firstName] = firstName
            it[ClientsTable.lastName] = lastName
            it[ClientsTable.phone] = phone
            it[ClientsTable.email] = email
        } get ClientsTable.id

        Client(id.value, firstName, lastName, phone, email)
    }

    private fun ResultRow.toClient(): Client =
        Client(
            id = this[ClientsTable.id].value,
            firstName = this[ClientsTable.firstName],
            lastName = this[ClientsTable.lastName],
            phone = this[ClientsTable.phone],
            email = this[ClientsTable.email]
        )
}

package com.example.car_dealership_server.data.repositories

import com.example.car_dealership_server.data.database.CarsTable
import com.example.car_dealership_server.data.database.ClientsTable
import com.example.car_dealership_server.data.database.DatabaseFactory
import com.example.car_dealership_server.data.database.PurchaseRequestsTable
import com.example.car_dealership_server.domain.models.CarStatus
import com.example.car_dealership_server.domain.models.PurchaseRequest
import com.example.car_dealership_server.domain.repositories.PurchaseRequestCreateResult
import com.example.car_dealership_server.domain.repositories.PurchaseRequestRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

class PurchaseRequestRepositoryImpl : PurchaseRequestRepository {
    override suspend fun getAll(): List<PurchaseRequest> = DatabaseFactory.dbQuery {
        PurchaseRequestsTable.selectAll().map { it.toRequest() }
    }

    override suspend fun createAndMarkSold(clientId: Int, carId: Int): PurchaseRequestCreateResult =
        DatabaseFactory.dbQuery {
            val clientExists = ClientsTable.select { ClientsTable.id eq clientId }.any()
            if (!clientExists) {
                return@dbQuery PurchaseRequestCreateResult.ClientNotFound
            }

            val carRow = CarsTable.select { CarsTable.id eq carId }.singleOrNull()
                ?: return@dbQuery PurchaseRequestCreateResult.CarNotFound
            val status = CarStatus.valueOf(carRow[CarsTable.status])
            if (status == CarStatus.SOLD) {
                return@dbQuery PurchaseRequestCreateResult.CarAlreadySold
            }

            CarsTable.update({ CarsTable.id eq carId }) {
                it[CarsTable.status] = CarStatus.SOLD.name
            }

            val createdAt = LocalDateTime.now()
            val id = PurchaseRequestsTable.insert {
                it[PurchaseRequestsTable.clientId] = clientId
                it[PurchaseRequestsTable.carId] = carId
                it[PurchaseRequestsTable.createdAt] = createdAt
            } get PurchaseRequestsTable.id

            PurchaseRequestCreateResult.Success(PurchaseRequest(id.value, clientId, carId, createdAt))
        }

    override suspend fun deleteAndMarkAvailable(id: Int): Boolean =
        DatabaseFactory.dbQuery {
            val requestRow = PurchaseRequestsTable.select { PurchaseRequestsTable.id eq id }.singleOrNull()
                ?: return@dbQuery false
            val carId = requestRow[PurchaseRequestsTable.carId].value

            PurchaseRequestsTable.deleteWhere { PurchaseRequestsTable.id eq id }
            CarsTable.update({ CarsTable.id eq carId }) {
                it[CarsTable.status] = CarStatus.AVAILABLE.name
            }

            true
        }

    private fun ResultRow.toRequest(): PurchaseRequest =
        PurchaseRequest(
            id = this[PurchaseRequestsTable.id].value,
            clientId = this[PurchaseRequestsTable.clientId].value,
            carId = this[PurchaseRequestsTable.carId].value,
            createdAt = this[PurchaseRequestsTable.createdAt]
        )
}

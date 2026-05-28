package com.example.car_dealership_server.data.repositories

import com.example.car_dealership_server.data.database.CarsTable
import com.example.car_dealership_server.data.database.DatabaseFactory
import com.example.car_dealership_server.domain.models.Car
import com.example.car_dealership_server.domain.models.CarStatus
import com.example.car_dealership_server.domain.repositories.CarRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.math.BigDecimal

class CarRepositoryImpl : CarRepository {
    override suspend fun getAll(): List<Car> = DatabaseFactory.dbQuery {
        CarsTable.selectAll().map { it.toCar() }
    }

    override suspend fun getById(id: Int): Car? = DatabaseFactory.dbQuery {
        CarsTable.select { CarsTable.id eq id }.singleOrNull()?.toCar()
    }

    override suspend fun create(
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car = DatabaseFactory.dbQuery {
        val id = CarsTable.insert {
            it[CarsTable.brand] = brand
            it[CarsTable.model] = model
            it[CarsTable.year] = year
            it[CarsTable.price] = BigDecimal.valueOf(price)
            it[CarsTable.status] = status.name
            it[CarsTable.description] = description
            it[CarsTable.posterUrl] = posterUrl
        } get CarsTable.id

        Car(id.value, brand, model, year, price, status, description, posterUrl)
    }

    override suspend fun update(
        id: Int,
        brand: String,
        model: String,
        year: Int,
        price: Double,
        status: CarStatus,
        description: String?,
        posterUrl: String?
    ): Car? = DatabaseFactory.dbQuery {
        val updated = CarsTable.update({ CarsTable.id eq id }) {
            it[CarsTable.brand] = brand
            it[CarsTable.model] = model
            it[CarsTable.year] = year
            it[CarsTable.price] = BigDecimal.valueOf(price)
            it[CarsTable.status] = status.name
            it[CarsTable.description] = description
            it[CarsTable.posterUrl] = posterUrl
        }
        if (updated == 0) {
            null
        } else {
            Car(id, brand, model, year, price, status, description, posterUrl)
        }
    }

    override suspend fun delete(id: Int): Boolean = DatabaseFactory.dbQuery {
        CarsTable.deleteWhere { CarsTable.id eq id } > 0
    }

    override suspend fun updateStatus(id: Int, status: CarStatus): Boolean = DatabaseFactory.dbQuery {
        CarsTable.update({ CarsTable.id eq id }) {
            it[CarsTable.status] = status.name
        } > 0
    }

    private fun ResultRow.toCar(): Car =
        Car(
            id = this[CarsTable.id].value,
            brand = this[CarsTable.brand],
            model = this[CarsTable.model],
            year = this[CarsTable.year],
            price = this[CarsTable.price].toDouble(),
            status = CarStatus.valueOf(this[CarsTable.status]),
            description = this[CarsTable.description],
            posterUrl = this[CarsTable.posterUrl]
        )
}

package com.example.car_dealership_server.data.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object UsersTable : IntIdTable("users") {
    val username = varchar("username", 50).uniqueIndex()
    val passwordHash = varchar("password_hash", 100)
    val role = varchar("role", 20)
}

object CarsTable : IntIdTable("cars") {
    val brand = varchar("brand", 50)
    val model = varchar("model", 50)
    val year = integer("year")
    val price = decimal("price", 10, 2)
    val status = varchar("status", 20)
    val description = text("description").nullable()
    val posterUrl = varchar("poster_url", 255).nullable()
}

object ClientsTable : IntIdTable("clients") {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val phone = varchar("phone", 30)
    val email = varchar("email", 100)
}

object PurchaseRequestsTable : IntIdTable("purchase_requests") {
    val clientId = reference("client_id", ClientsTable, onDelete = ReferenceOption.CASCADE)
    val carId = reference("car_id", CarsTable, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at")
}

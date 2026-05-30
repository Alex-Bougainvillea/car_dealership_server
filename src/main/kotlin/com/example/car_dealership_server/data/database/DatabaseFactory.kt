package com.example.car_dealership_server.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val dbConfig = config.config("database")
        val url = dbConfig.propertyOrNull("url")?.getString() ?: System.getenv("DB_URL")
        val user = dbConfig.propertyOrNull("user")?.getString() ?: System.getenv("DB_USER")
        val password = dbConfig.propertyOrNull("password")?.getString() ?: System.getenv("DB_PASSWORD")
        val driver = dbConfig.property("driver").getString()

        require(!url.isNullOrBlank()) { "DB_URL is required" }
        require(!user.isNullOrBlank()) { "DB_USER is required" }
        require(password != null) { "DB_PASSWORD is required" }

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = url
            username = user
            this.password = password
            driverClassName = driver
            maximumPoolSize = 5
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)
        TransactionManager.defaultDatabase

        transaction {
            SchemaUtils.create(
                UsersTable,
                CarsTable,
                ClientsTable,
                PurchaseRequestsTable
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

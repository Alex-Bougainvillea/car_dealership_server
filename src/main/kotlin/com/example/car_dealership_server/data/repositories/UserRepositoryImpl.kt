package com.example.car_dealership_server.data.repositories

import com.example.car_dealership_server.data.database.DatabaseFactory
import com.example.car_dealership_server.data.database.UsersTable
import com.example.car_dealership_server.domain.models.User
import com.example.car_dealership_server.domain.models.UserRole
import com.example.car_dealership_server.domain.models.UserWithPassword
import com.example.car_dealership_server.domain.repositories.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepositoryImpl : UserRepository {
    override suspend fun createUser(username: String, passwordHash: String, role: UserRole): User =
        DatabaseFactory.dbQuery {
            val id = UsersTable.insert {
                it[UsersTable.username] = username
                it[UsersTable.passwordHash] = passwordHash
                it[UsersTable.role] = role.name
            } get UsersTable.id

            User(id.value, username, role)
        }

    override suspend fun findByUsername(username: String): UserWithPassword? =
        DatabaseFactory.dbQuery {
            UsersTable.select { UsersTable.username eq username }
                .singleOrNull()
                ?.let { row ->
                    val role = UserRole.valueOf(row[UsersTable.role])
                    val user = User(row[UsersTable.id].value, row[UsersTable.username], role)
                    UserWithPassword(user, row[UsersTable.passwordHash])
                }
        }
}

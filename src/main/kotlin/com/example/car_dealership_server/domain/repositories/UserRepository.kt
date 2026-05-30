package com.example.car_dealership_server.domain.repositories

import com.example.car_dealership_server.domain.models.User
import com.example.car_dealership_server.domain.models.UserRole
import com.example.car_dealership_server.domain.models.UserWithPassword

interface UserRepository {
    suspend fun createUser(username: String, passwordHash: String, role: UserRole): User
    suspend fun upsertUser(username: String, passwordHash: String, role: UserRole): User
    suspend fun findByUsername(username: String): UserWithPassword?
}

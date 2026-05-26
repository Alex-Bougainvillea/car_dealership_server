package com.example.car_dealership_server.domain.models

data class User(
    val id: Int,
    val username: String,
    val role: UserRole
)

data class UserWithPassword(
    val user: User,
    val passwordHash: String
)

enum class UserRole {
    ADMIN,
    CLIENT
}

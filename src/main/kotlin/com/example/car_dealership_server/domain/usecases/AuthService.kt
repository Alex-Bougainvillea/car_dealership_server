package com.example.car_dealership_server.domain.usecases

import com.example.car_dealership_server.domain.models.User
import com.example.car_dealership_server.domain.models.UserRole
import com.example.car_dealership_server.domain.repositories.UserRepository
import com.example.car_dealership_server.security.JwtService
import com.example.car_dealership_server.security.PasswordHasher

class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    suspend fun register(username: String, password: String, role: UserRole): AuthResult {
        val existing = userRepository.findByUsername(username)
        if (existing != null) {
            return AuthResult.Error("User already exists")
        }
        val hashed = PasswordHasher.hash(password)
        val user = userRepository.createUser(username, hashed, role)
        val token = jwtService.generateToken(user)
        return AuthResult.Success(token, user)
    }

    suspend fun ensureUser(username: String, password: String, role: UserRole): User {
        val hashed = PasswordHasher.hash(password)
        return userRepository.upsertUser(username, hashed, role)
    }

    suspend fun login(username: String, password: String): AuthResult {
        val userWithPassword = userRepository.findByUsername(username)
            ?: return AuthResult.Error("Invalid credentials")
        if (!PasswordHasher.verify(password, userWithPassword.passwordHash)) {
            return AuthResult.Error("Invalid credentials")
        }
        val token = jwtService.generateToken(userWithPassword.user)
        return AuthResult.Success(token, userWithPassword.user)
    }
}

sealed class AuthResult {
    data class Success(val token: String, val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

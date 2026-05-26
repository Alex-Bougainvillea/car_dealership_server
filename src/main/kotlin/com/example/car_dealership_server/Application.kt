package com.example.car_dealership_server

import com.example.car_dealership_server.data.database.DatabaseFactory
import com.example.car_dealership_server.domain.repositories.CarRepository
import com.example.car_dealership_server.domain.repositories.ClientRepository
import com.example.car_dealership_server.domain.repositories.PurchaseRequestRepository
import com.example.car_dealership_server.domain.repositories.UserRepository
import com.example.car_dealership_server.domain.usecases.AuthService
import com.example.car_dealership_server.domain.usecases.CarService
import com.example.car_dealership_server.domain.usecases.ClientService
import com.example.car_dealership_server.domain.usecases.PurchaseRequestService
import com.example.car_dealership_server.presentation.routes.registerAuthRoutes
import com.example.car_dealership_server.presentation.routes.registerCarRoutes
import com.example.car_dealership_server.presentation.routes.registerClientRoutes
import com.example.car_dealership_server.presentation.routes.registerRequestRoutes
import com.example.car_dealership_server.security.JwtService
import com.example.car_dealership_server.security.configureJwt
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(
    userRepository: UserRepository = com.example.car_dealership_server.data.repositories.UserRepositoryImpl(),
    carRepository: CarRepository = com.example.car_dealership_server.data.repositories.CarRepositoryImpl(),
    clientRepository: ClientRepository = com.example.car_dealership_server.data.repositories.ClientRepositoryImpl(),
    purchaseRequestRepository: PurchaseRequestRepository = com.example.car_dealership_server.data.repositories.PurchaseRequestRepositoryImpl()
) {
    DatabaseFactory.init(environment.config)
    install(ContentNegotiation) {
        json()
    }
    configureJwt(environment.config)

    val jwtService = JwtService.fromConfig(environment.config)
    val authService = AuthService(userRepository, jwtService)
    val carService = CarService(carRepository)
    val clientService = ClientService(clientRepository)
    val requestService = PurchaseRequestService(purchaseRequestRepository)

    routing {
        registerAuthRoutes(authService)
        registerCarRoutes(carService)
        registerClientRoutes(clientService)
        registerRequestRoutes(requestService)
    }
}

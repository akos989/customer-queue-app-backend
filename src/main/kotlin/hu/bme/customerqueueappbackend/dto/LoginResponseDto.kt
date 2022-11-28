package hu.bme.customerqueueappbackend.dto

import java.util.*

class LoginResponseDto (
    val userId: UUID = UUID.randomUUID(),
    val tokenValidity: Date,
    val token: String = "",
    val roles: List<String> = listOf()
)

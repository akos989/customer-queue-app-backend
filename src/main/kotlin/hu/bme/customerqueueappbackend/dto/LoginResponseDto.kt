package hu.bme.customerqueueappbackend.dto

import java.util.*

class LoginResponseDto (
    val email: String = "",
    val tokenValidity: Date,
    val token: String = "",
    val roles: List<String> = listOf()
)

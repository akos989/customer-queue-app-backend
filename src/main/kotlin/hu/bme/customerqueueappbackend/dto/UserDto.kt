package hu.bme.customerqueueappbackend.dto

import java.util.UUID

class UserDto(
    val id: UUID = UUID.randomUUID(),
    val email: String = "",
    val password: String = ""
)
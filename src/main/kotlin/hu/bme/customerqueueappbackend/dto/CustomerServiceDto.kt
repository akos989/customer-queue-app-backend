package hu.bme.customerqueueappbackend.dto

import java.util.UUID

class CustomerServiceDto(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val serviceTypes: List<ServiceTypeDto> = listOf(),
    var waitingTime: Int? = null,
    var employees: List<UserDto> = listOf(),
    var admins: List<UserDto> = listOf(),
    var waitingPeople: Int = 0
)

package hu.bme.customerqueueappbackend.dto

import java.util.*

class OwnerDto (
    val id: UUID = UUID.randomUUID(),
    val email: String = "",
    val customerServices: Set<CustomerServiceDto> = setOf()
)
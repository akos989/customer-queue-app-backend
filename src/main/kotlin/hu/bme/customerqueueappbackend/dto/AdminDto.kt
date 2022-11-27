package hu.bme.customerqueueappbackend.dto

import java.util.*

class AdminDto (
    val id: UUID = UUID.randomUUID(),
    val customerService: CustomerServiceDto = CustomerServiceDto()
)
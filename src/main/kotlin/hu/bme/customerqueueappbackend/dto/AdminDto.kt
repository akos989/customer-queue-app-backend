package hu.bme.customerqueueappbackend.dto

import java.util.*

class AdminDto (
    val id: UUID = UUID.randomUUID(),
    var customerService: CustomerServiceDto = CustomerServiceDto()
)
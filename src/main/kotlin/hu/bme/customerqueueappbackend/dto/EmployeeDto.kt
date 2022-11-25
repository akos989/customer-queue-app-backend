package hu.bme.customerqueueappbackend.dto

import java.util.*

class EmployeeDto (
    val id: UUID = UUID.randomUUID(),
    val email: String = "",
    val helpDeskNumber: Int = 0,
    val customerService: CustomerServiceDto = CustomerServiceDto()
)
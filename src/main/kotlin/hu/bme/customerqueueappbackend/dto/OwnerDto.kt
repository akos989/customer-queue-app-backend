package hu.bme.customerqueueappbackend.dto

import hu.bme.customerqueueappbackend.model.CustomerService
import java.util.*

class OwnerDto (
    val id: UUID = UUID.randomUUID(),
    val email: String = "",
    val customerServices: List<CustomerService> = listOf()
)
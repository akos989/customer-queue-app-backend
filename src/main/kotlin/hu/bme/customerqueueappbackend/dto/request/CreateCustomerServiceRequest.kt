package hu.bme.customerqueueappbackend.dto.request

import java.util.*

class CreateCustomerServiceRequest (
    val ownerId: UUID,
    val name: String = "",
)
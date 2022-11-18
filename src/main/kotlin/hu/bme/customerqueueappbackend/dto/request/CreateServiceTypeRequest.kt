package hu.bme.customerqueueappbackend.dto.request

import java.util.UUID

class CreateServiceTypeRequest (
    val name: String = "",
    val handleTime: Int = 0,
    val customerServiceId: UUID
)
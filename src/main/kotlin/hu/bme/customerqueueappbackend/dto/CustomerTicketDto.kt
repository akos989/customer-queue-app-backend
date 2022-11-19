package hu.bme.customerqueueappbackend.dto

import java.util.UUID

class CustomerTicketDto (
    val id: UUID = UUID.randomUUID(),
    val customerServiceName: String = "",
    val waitingPeopleNumber: Int = 0,
    val waitingTime: Int = 0
)

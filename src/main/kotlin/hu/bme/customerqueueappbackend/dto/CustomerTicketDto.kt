package hu.bme.customerqueueappbackend.dto

import java.util.*

class CustomerTicketDto (
    val id: UUID = UUID.randomUUID(),
    val serviceTypeName: String = "",
    val waitingPeopleNumber: Int = 0,
    val callTime: Date? = null,
    val handleDesk: Int = 0
)

package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import java.util.UUID

interface CustomerTicketService {
    fun create(serviceTypeId: UUID): CustomerTicketDto

    fun delayTicket(id: UUID, minutes: Int): CustomerTicketDto

    fun deleteTicket(id: UUID)

    fun getTicket(id: UUID): CustomerTicketDto
}

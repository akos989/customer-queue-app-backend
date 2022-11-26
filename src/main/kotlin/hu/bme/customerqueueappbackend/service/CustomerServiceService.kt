package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import java.util.UUID

interface CustomerServiceService {
    fun getCustomerService(id: UUID): CustomerServiceDto

    fun deleteCustomerService(id: UUID)

    fun getNextTicket(id: UUID, employeeId: UUID): CustomerTicketDto
}

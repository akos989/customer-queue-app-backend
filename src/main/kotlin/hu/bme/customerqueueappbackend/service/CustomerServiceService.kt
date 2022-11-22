package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import hu.bme.customerqueueappbackend.dto.ServiceTypeDto
import hu.bme.customerqueueappbackend.dto.request.CreateServiceTypeRequest
import java.util.UUID

interface CustomerServiceService {
    fun getCustomerService(id: UUID): CustomerServiceDto

    fun getNextTicket(id: UUID, employeeId: UUID): CustomerTicketDto
}

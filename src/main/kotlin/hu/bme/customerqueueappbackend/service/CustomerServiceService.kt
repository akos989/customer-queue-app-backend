package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import hu.bme.customerqueueappbackend.dto.request.CreateCustomerServiceRequest
import java.util.UUID

interface CustomerServiceService {
    fun saveCustomerService(createCustomerServiceRequest: CreateCustomerServiceRequest): CustomerServiceDto

    fun getCustomerService(id: UUID): CustomerServiceDto

    fun deleteCustomerService(id: UUID)

    fun getNextTicket(id: UUID, employeeId: UUID): CustomerTicketDto
}

package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import hu.bme.customerqueueappbackend.dto.ServiceTypeDto
import hu.bme.customerqueueappbackend.dto.request.CreateServiceTypeRequest
import hu.bme.customerqueueappbackend.model.CustomerService
import hu.bme.customerqueueappbackend.model.CustomerTicket
import hu.bme.customerqueueappbackend.model.Employee
import hu.bme.customerqueueappbackend.model.ServiceType
import hu.bme.customerqueueappbackend.repository.CustomerServiceRepository
import hu.bme.customerqueueappbackend.repository.EmployeeRepository
import hu.bme.customerqueueappbackend.repository.ServiceTypeRepository
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.transaction.Transactional

@Service
class CustomerServiceServiceImpl (
    private val customerServiceRepository: CustomerServiceRepository,
    private val employeeRepository: EmployeeRepository,
    private val mapper: ModelMapper
): CustomerServiceService {
    override fun getCustomerService(id: UUID): CustomerServiceDto {
        val customerService = findCustomerServiceById(id)
        val customerServiceDto: CustomerServiceDto = customerService.toDto(mapper)
        customerServiceDto.run {
            waitingPeople = customerService.waitingTickets.count()
            waitingTime = customerService.waitingTickets.sumOf { it.serviceType.handleTime }
        }
        return customerServiceDto
    }

    @Transactional
    override fun getNextTicket(id: UUID, employeeId: UUID): CustomerTicketDto {
        val customerService = findCustomerServiceById(id)
        val nextTicket = customerService.waitingTickets
            .filter { it.handleStartTimeStamp == null }
            .minByOrNull { it.waitingPeopleNumber }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No next ticket")
        val employeeDeskNumber = findEmployeeById(employeeId).helpDeskNumber
        nextTicket.handleStartTimeStamp = Date()
        nextTicket.handleDesk = employeeDeskNumber
        return nextTicket.toDto(mapper)
    }

    private fun findCustomerServiceById(id: UUID): CustomerService
        = customerServiceRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Service not found")

    private fun findEmployeeById(id: UUID): Employee
        = employeeRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found")

}

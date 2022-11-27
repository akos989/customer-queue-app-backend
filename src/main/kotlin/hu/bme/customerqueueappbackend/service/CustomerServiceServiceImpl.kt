package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import hu.bme.customerqueueappbackend.dto.UserDto
import hu.bme.customerqueueappbackend.dto.request.CreateCustomerServiceRequest
import hu.bme.customerqueueappbackend.model.CustomerService
import hu.bme.customerqueueappbackend.model.Employee
import hu.bme.customerqueueappbackend.repository.*
import hu.bme.customerqueueappbackend.util.exceptions.BadRequestException
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class CustomerServiceServiceImpl (
    private val customerServiceRepository: CustomerServiceRepository,
    private val employeeRepository: EmployeeRepository,
    private val adminRepository: AdminRepository,
    private val customerTicketRepository: CustomerTicketRepository,
    private val serviceTypeRepository: ServiceTypeService,
    private val ownerRepository: OwnerRepository,
    private val mapper: ModelMapper
): CustomerServiceService {
    @Transactional
    override fun saveCustomerService(createCustomerServiceRequest: CreateCustomerServiceRequest): CustomerServiceDto {
        val owner = ownerRepository.findByIdOrNull(createCustomerServiceRequest.ownerId) ?: throw EntityNotFoundException("Owner not found")
        val customerService = CustomerService(name = createCustomerServiceRequest.name, owner = owner)

        customerServiceRepository.save(customerService)

        return customerService.toDto(mapper)
    }

    override fun getCustomerService(id: UUID): CustomerServiceDto {
        val customerService = findCustomerServiceById(id)
        // Navigation properties of employees and admins were removed from CustomerService class. If you want to fetch the admin and employees belonging to a CustomerService use:
        // EmployeeRepository.findByCustomerService() and AdminRepository.findByCustomerService()
        val employeeEntities = employeeRepository.findByCustomerService(customerService)
        val adminEntities = adminRepository.findByCustomerService(customerService)
        val adminDtoList: List<UserDto> = adminEntities.toDto(mapper)
        val employeeDtoList: List<UserDto> = employeeEntities.toDto(mapper)

        val customerServiceDto: CustomerServiceDto = customerService.toDto(mapper)
        customerServiceDto.run {
            waitingPeople = customerService.waitingTickets.count()
            waitingTime = customerService.waitingTickets.sumOf { it.serviceType.handleTime }
            admins = adminDtoList
            employees = employeeDtoList
        }
        return customerServiceDto
    }

    @Transactional
    override fun deleteCustomerService(id: UUID) {
        val customerService = findCustomerServiceById(id)

        if (customerTicketRepository.findFirstByCustomerService(customerService) == null) {
            // delete related employees
            val employees = employeeRepository.findByCustomerService(customerService)
            for (employee in employees) {
                println(employee)
                employeeRepository.delete(employee)
            }

            // delete related admins
            val admins = adminRepository.findByCustomerService(customerService)
            for (admin in admins) {
                println(admin)
                adminRepository.delete(admin)
            }

            // delete related service types
            for (serviceType in customerService.serviceTypes) {
                println(serviceType)
                serviceTypeRepository.deleteServiceType(serviceType.id)
            }

            // delete the customer service itself
            customerServiceRepository.delete(customerService)
        } else {
            throw BadRequestException("At least one ticket has not been completed in this customer service")
        }
    }

    @Transactional
    override fun getNextTicket(id: UUID, employeeId: UUID): CustomerTicketDto {
        val customerService = findCustomerServiceById(id)
        val nextTicket = customerService.waitingTickets
            .filter { it.handleStartTimeStamp == null }
            .minByOrNull { it.waitingPeopleNumber }
            ?: throw BadRequestException("There is no next ticket")
        val employee = findEmployeeById(employeeId)
        val employeeDeskNumber = employee.helpDeskNumber
        nextTicket.handleStartTimeStamp = Date()
        nextTicket.handleDesk = employeeDeskNumber
        return nextTicket.toDto(mapper)
    }

    private fun findCustomerServiceById(id: UUID): CustomerService
        = customerServiceRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Customer Service not found")

    private fun findEmployeeById(id: UUID): Employee
        = employeeRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Employee not found")

}

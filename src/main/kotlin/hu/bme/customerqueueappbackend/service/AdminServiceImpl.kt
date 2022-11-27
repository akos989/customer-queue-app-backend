package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.AdminDto
import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.dto.UserDto
import hu.bme.customerqueueappbackend.repository.AdminRepository
import hu.bme.customerqueueappbackend.repository.EmployeeRepository
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class AdminServiceImpl (
    private val mapper: ModelMapper,
    private val adminRepository: AdminRepository,
    private val employeeRepository: EmployeeRepository,
): AdminService {

    override fun getAdmin(id: UUID): AdminDto {
        val admin = adminRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Admin was not found")

        val employeeEntities = employeeRepository.findByCustomerService(admin.customerService)
        val employeeDtoList: List<UserDto> = employeeEntities.toDto(mapper)

        val customerServiceDto: CustomerServiceDto = admin.customerService.toDto(mapper)
        customerServiceDto.run {
            employees = employeeDtoList
        }

        val adminDto: AdminDto = admin.toDto(mapper)
        adminDto.run {
            customerService = customerServiceDto
        }

        return adminDto
    }

}
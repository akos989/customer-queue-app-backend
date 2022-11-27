package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.EmployeeDto
import hu.bme.customerqueueappbackend.repository.EmployeeRepository
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class EmployeeServiceImpl (
    private val mapper: ModelMapper,
    private val employeeRepository: EmployeeRepository
): EmployeeService {


    override fun getEmployee(id: UUID): EmployeeDto {
        val employee = employeeRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Employee was not found")
        return employee.toDto(mapper)
    }

    @Transactional
    override fun deleteEmployee(id: UUID) {
        employeeRepository.deleteById(id)
    }

}
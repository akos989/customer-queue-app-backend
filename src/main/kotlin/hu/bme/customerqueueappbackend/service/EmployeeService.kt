package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.EmployeeDto
import java.util.*

interface EmployeeService {

    fun getEmployee(id: UUID): EmployeeDto?

}
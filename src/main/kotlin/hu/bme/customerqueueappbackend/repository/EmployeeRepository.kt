package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EmployeeRepository: JpaRepository<Employee, UUID>

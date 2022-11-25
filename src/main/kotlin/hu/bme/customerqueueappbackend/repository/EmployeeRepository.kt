package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.CustomerService
import hu.bme.customerqueueappbackend.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EmployeeRepository: JpaRepository<Employee, UUID> {
    fun findAllByCustomerServiceAndHelpDeskNumberNot(customerService: CustomerService, helpDeskNumber: Int): List<Employee>

    fun findFirstByCustomerServiceOrderByHelpDeskNumberDesc(customerService: CustomerService): Employee

    fun findByEmail(email: String): Employee?

    fun findByCustomerService(customerService: CustomerService): List<Employee>
}


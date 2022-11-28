package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.Admin
import hu.bme.customerqueueappbackend.model.CustomerService
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdminRepository: JpaRepository<Admin, UUID> {
    fun findByCustomerService(customerService: CustomerService): List<Admin>
}

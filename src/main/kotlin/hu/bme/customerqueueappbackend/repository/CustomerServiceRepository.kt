package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.CustomerService
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerServiceRepository: JpaRepository<CustomerService, UUID>

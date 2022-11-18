package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.ServiceType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ServiceTypeRepository: JpaRepository<ServiceType, UUID> {
}
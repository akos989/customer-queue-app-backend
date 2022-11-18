package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.Owner
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OwnerRepository: JpaRepository<Owner, UUID>

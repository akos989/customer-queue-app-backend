package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdminRepository: JpaRepository<Admin, UUID>

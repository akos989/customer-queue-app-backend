package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.Role
import hu.bme.customerqueueappbackend.security.authorization.RoleType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoleRepository: JpaRepository<Role, UUID> {
    fun existsByName(name: RoleType): Boolean

    fun findFirstByName(name: RoleType): Role?
}

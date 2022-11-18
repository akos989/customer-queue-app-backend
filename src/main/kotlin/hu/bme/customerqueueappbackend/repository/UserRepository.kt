package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
    fun findFirstByEmail(email: String): User?
}
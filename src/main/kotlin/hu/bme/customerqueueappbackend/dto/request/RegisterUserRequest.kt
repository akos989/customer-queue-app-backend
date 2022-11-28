package hu.bme.customerqueueappbackend.dto.request

import hu.bme.customerqueueappbackend.security.authorization.RoleType
import java.util.UUID

class RegisterUserRequest(
    val email: String = "",
    val password: String = "",
    val role: RoleType = RoleType.OWNER,
    val customerServiceId: UUID = UUID.randomUUID(),
)
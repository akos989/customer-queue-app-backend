package hu.bme.customerqueueappbackend.security.authorization

import hu.bme.customerqueueappbackend.model.Role
import hu.bme.customerqueueappbackend.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {
    val admin: Role by lazy {
        roleRepository.findFirstByName(RoleType.ADMIN)!!
    }

    val owner: Role by lazy {
        roleRepository.findFirstByName(RoleType.OWNER)!!
    }

    val employee: Role by lazy {
        roleRepository.findFirstByName(RoleType.EMPLOYEE)!!
    }
}

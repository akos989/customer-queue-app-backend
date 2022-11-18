package hu.bme.customerqueueappbackend.security.authorization

import hu.bme.customerqueueappbackend.model.Role
import hu.bme.customerqueueappbackend.repository.RoleRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Order(1)
class RoleInitializer(
    private val roleRepository: RoleRepository
) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments?) {
        RoleType.values().forEach { roleType ->
            if (!roleRepository.existsByName(roleType)) {
                roleRepository.save(
                    Role(name = roleType)
                )
            }
        }
    }
}

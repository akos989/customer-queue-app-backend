package hu.bme.customerqueueappbackend.config

import hu.bme.customerqueueappbackend.model.ServiceType
import hu.bme.customerqueueappbackend.model.User
import hu.bme.customerqueueappbackend.repository.ServiceTypeRepository
import hu.bme.customerqueueappbackend.repository.UserRepository
import hu.bme.customerqueueappbackend.security.authorization.RoleService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class TestDataInitializer(
    private val serviceTypeRepository: ServiceTypeRepository,
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        serviceTypeRepository.saveAll(
            listOf(
                ServiceType(name = "Upload balance", handleTime = 11),
                ServiceType(name = "Check balance", handleTime = 4),
                ServiceType(name = "New customer", handleTime = 30),
                ServiceType(name = "Modify billing address", handleTime = 19),
                ServiceType(name = "Close account", handleTime = 23),
                ServiceType(name = "Random questions", handleTime = 5),
            )
        )

        userRepository.saveAll(
            listOf(
                User(
                    email = "User1",
                    password = passwordEncoder.encode("player"),
                    roles = mutableSetOf(roleService.admin)
                ),
                User(
                    email = "User2",
                    password = passwordEncoder.encode("player"),
                    roles = mutableSetOf(roleService.employee)
                )
            )
        )
    }
}

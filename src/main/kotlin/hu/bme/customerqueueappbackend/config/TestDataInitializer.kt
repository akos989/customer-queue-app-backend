package hu.bme.customerqueueappbackend.config

import hu.bme.customerqueueappbackend.model.*
import hu.bme.customerqueueappbackend.repository.*
import hu.bme.customerqueueappbackend.security.authorization.RoleService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Profile("dev")
class TestDataInitializer(
    private val serviceTypeRepository: ServiceTypeRepository,
    private val userRepository: UserRepository,
    private val customerTicketRepository: CustomerTicketRepository,
    private val customerServiceRepository: CustomerServiceRepository,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val owners = userRepository.saveAll(
            listOf(
                Owner(email = "owner1@user.com", password = passwordEncoder.encode("password"), roles = mutableSetOf(roleService.owner))
            )
        )
        val customerServices = customerServiceRepository.saveAll(
            listOf(
                CustomerService(id = UUID.fromString("00000000-80ed-4d57-a626-c2d5464a522a"), name = "Telekom Alle", owner = owners[0])
            )
        )
        userRepository.saveAll(
            listOf(
                Employee(email = "employee1@user.com", password = passwordEncoder.encode("password"), helpDeskNumber = 11, customerService = customerServices[0]),
                Employee(email = "employee2@user.com", password = passwordEncoder.encode("password"), helpDeskNumber = 15, customerService = customerServices[0]),
                Employee(email = "employee3@user.com", password = passwordEncoder.encode("password"), customerService = customerServices[0]),

                Admin(email = "admin1@user.com", password = passwordEncoder.encode("password"), roles = mutableSetOf(roleService.admin), customerService = customerServices[0]),
                Admin(email = "admin2@user.com", password = passwordEncoder.encode("password"), roles = mutableSetOf(roleService.admin), customerService = customerServices[0])
            )
        )
        val serviceTypes = serviceTypeRepository.saveAll(
            listOf(
                ServiceType(name = "Upload balance", handleTime = 11, customerService = customerServices[0]),
                ServiceType(name = "Check balance", handleTime = 4, customerService = customerServices[0]),
                ServiceType(name = "New customer", handleTime = 30, customerService = customerServices[0]),
                ServiceType(name = "Modify billing address", handleTime = 19, customerService = customerServices[0]),
                ServiceType(name = "Close account", handleTime = 23, customerService = customerServices[0]),
                ServiceType(name = "Random questions", handleTime = 5, customerService = customerServices[0]),
            )
        )
        customerTicketRepository.saveAll(
            listOf(
                CustomerTicket(serviceType = serviceTypes[0], customerService = customerServices[0], callTime = null, waitingPeopleNumber = 0),
                CustomerTicket(serviceType = serviceTypes[1], customerService = customerServices[0], callTime = null, waitingPeopleNumber = 1),
                CustomerTicket(serviceType = serviceTypes[2], customerService = customerServices[0], callTime = null, waitingPeopleNumber = 2),
                CustomerTicket(serviceType = serviceTypes[0], customerService = customerServices[0], callTime = null, waitingPeopleNumber = 3),
                CustomerTicket(serviceType = serviceTypes[4], customerService = customerServices[0], callTime = null, waitingPeopleNumber = 4),
                CustomerTicket(serviceType = serviceTypes[5], customerService = customerServices[0], callTime = null, waitingPeopleNumber = 5)
            )
        )
    }
}

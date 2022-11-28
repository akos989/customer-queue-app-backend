package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.UserDto
import hu.bme.customerqueueappbackend.dto.request.RegisterUserRequest
import hu.bme.customerqueueappbackend.model.Admin
import hu.bme.customerqueueappbackend.model.Employee
import hu.bme.customerqueueappbackend.model.Owner
import hu.bme.customerqueueappbackend.repository.CustomerServiceRepository
import hu.bme.customerqueueappbackend.repository.UserRepository
import hu.bme.customerqueueappbackend.security.authorization.RoleService
import hu.bme.customerqueueappbackend.security.authorization.RoleType
import hu.bme.customerqueueappbackend.util.exceptions.BadRequestException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl (
    private val userRepository: UserRepository,
    private val customerServiceRepository: CustomerServiceRepository,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder,
    private val mapper: ModelMapper
): AuthService {

    override fun registerUser(registerUserRequest: RegisterUserRequest): UserDto {
        val customerService = customerServiceRepository.findByIdOrNull(registerUserRequest.customerServiceId)

        when (registerUserRequest.role) {
            RoleType.ADMIN -> {
                if (customerService == null) {
                    throw BadRequestException("Customer service was not found")
                }
                val admin = Admin(email = registerUserRequest.email, password = passwordEncoder.encode(registerUserRequest.email.substringBefore('@')), roles = mutableSetOf(roleService.admin), customerService = customerService)
                userRepository.save(admin)

                return admin.toDto(mapper)
            }
            RoleType.OWNER -> {
                val owner = Owner(email = registerUserRequest.email, password = passwordEncoder.encode(registerUserRequest.password), roles = mutableSetOf(roleService.owner))
                userRepository.save(owner)

                return owner.toDto(mapper)
            }
            RoleType.EMPLOYEE -> {
                if (customerService == null) {
                    throw BadRequestException("Customer service was not found")
                }
                val employee = Employee(email = registerUserRequest.email, password = passwordEncoder.encode(registerUserRequest.email.substringBefore('@')), roles = mutableSetOf(roleService.employee), customerService = customerService)
                userRepository.save(employee)

                return employee.toDto(mapper)
            }
        }
    }

}
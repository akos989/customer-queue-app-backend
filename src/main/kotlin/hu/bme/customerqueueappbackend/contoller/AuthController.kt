package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.LoginResponseDto
import hu.bme.customerqueueappbackend.dto.UserDto
import hu.bme.customerqueueappbackend.dto.request.LoginRequest
import hu.bme.customerqueueappbackend.dto.request.RegisterUserRequest
import hu.bme.customerqueueappbackend.repository.EmployeeRepository
import hu.bme.customerqueueappbackend.security.jwtutils.TokenManager
import hu.bme.customerqueueappbackend.service.AuthService
import hu.bme.customerqueueappbackend.service.WaitingTimeCalculationService
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(AuthController.BASE_URL)
class AuthController (
    private val authenticationManager: AuthenticationManager,
    private val waitingTimeCalculationService: WaitingTimeCalculationService,
    private val employeeRepository: EmployeeRepository,
    private val tokenManager: TokenManager,
    private val authService: AuthService,
) {
    companion object {
        const val BASE_URL = "/api/auth"
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponseDto> {
        return try {
            val loggedInUser = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password))
            val (tokenString, tokenExpirationDate) = tokenManager.generateJwtToken(loggedInUser.name, loggedInUser.authorities)
            if (loggedInUser.authorities.isEmpty()) {
                val employee = employeeRepository.findByEmail(loggedInUser.name)!!
                val maxHelpDeskNumber = employeeRepository.findFirstByCustomerServiceOrderByHelpDeskNumberDesc(employee.customerService).helpDeskNumber
                employee.helpDeskNumber = maxHelpDeskNumber + 1
                waitingTimeCalculationService.refreshCallTimesFor(employee.customerService)
            }
            ResponseEntity.ok(
                LoginResponseDto(
                    email = loggedInUser.name,
                    token = tokenString,
                    tokenValidity = tokenExpirationDate,
                    roles = loggedInUser.authorities.map { grantedAuthority -> grantedAuthority.authority }
                )
            )
        } catch (e: Exception) {
            throw EntityNotFoundException("User not found")
        }
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody registerUserRequest: RegisterUserRequest): ResponseEntity<UserDto> {
        return ResponseEntity.ok(authService.registerUser(registerUserRequest))
    }

    @PatchMapping("/{id}/logout")
    fun logout(@PathVariable id: UUID): ResponseEntity<Unit> {
        val employee = employeeRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Employee not found")
        employee.helpDeskNumber = 0
        waitingTimeCalculationService.refreshCallTimesFor(employee.customerService)
        return ResponseEntity.ok(Unit)
    }
}

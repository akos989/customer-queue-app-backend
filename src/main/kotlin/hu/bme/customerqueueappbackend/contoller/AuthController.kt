package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.LoginResponseDto
import hu.bme.customerqueueappbackend.dto.request.LoginRequest
import hu.bme.customerqueueappbackend.security.jwtutils.TokenManager
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AuthController.BASE_URL)
class AuthController (
    private val authenticationManager: AuthenticationManager,
    private val tokenManager: TokenManager
) {
    companion object {
        const val BASE_URL = "/api/auth"
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponseDto> {
        return try {
            val loggedInUser = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password))
            val (tokenString, tokenExpirationDate) = tokenManager.generateJwtToken(loggedInUser.name, loggedInUser.authorities)
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
}

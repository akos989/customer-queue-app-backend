package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.UserDto
import hu.bme.customerqueueappbackend.dto.request.RegisterUserRequest

interface AuthService {

    fun registerUser(registerUserRequest: RegisterUserRequest): UserDto
}
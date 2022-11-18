package hu.bme.customerqueueappbackend.util.exceptions

import org.springframework.http.HttpStatus

class ErrorCode(
    val error: String,
    val httpStatus: HttpStatus,
    val message: String
)

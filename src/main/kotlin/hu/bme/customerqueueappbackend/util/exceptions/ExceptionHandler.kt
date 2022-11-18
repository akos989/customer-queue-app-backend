package hu.bme.customerqueueappbackend.util.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFound(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val body = ErrorCode(
            error = ex.javaClass.name,
            httpStatus = HttpStatus.NOT_FOUND,
            message = ex.localizedMessage
        )
        return handleExceptionInternal(ex, body, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(BadRequestException::class)
    protected fun handleBadRequest(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val body = ErrorCode(
            error = ex.javaClass.name,
            httpStatus = HttpStatus.BAD_REQUEST,
            message = ex.localizedMessage
        )
        return handleExceptionInternal(ex, body, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }
}

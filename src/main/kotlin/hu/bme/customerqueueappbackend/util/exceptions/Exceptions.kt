package hu.bme.customerqueueappbackend.util.exceptions

class EntityNotFoundException(message: String) : RuntimeException(message)

class BadRequestException(message: String) : RuntimeException(message)

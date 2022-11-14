package hu.bme.customerqueueappbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CustomerQueueAppBackendApplication

fun main(args: Array<String>) {
    runApplication<CustomerQueueAppBackendApplication>(*args)
}

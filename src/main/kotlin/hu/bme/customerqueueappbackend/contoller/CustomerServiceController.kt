package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.service.CustomerServiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping(CustomerServiceController.BASE_URL)
class CustomerServiceController(
    private val customerServiceService: CustomerServiceService
) {
    companion object {
        const val BASE_URL = "/api/customerServices"
    }

    @GetMapping("/{id}")
    fun getCustomerService(@PathVariable id: UUID): ResponseEntity<CustomerServiceDto>
        = ResponseEntity.ok(customerServiceService.getCustomerService(id))
}
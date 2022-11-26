package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.CustomerServiceDto
import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
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

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit>
        = ResponseEntity.ok(customerServiceService.deleteCustomerService(id))

    @GetMapping("/{id}/nextTicket")
    fun getNextTicket(@PathVariable id: UUID, @RequestParam employeeId: UUID): ResponseEntity<CustomerTicketDto>
        = ResponseEntity.ok(customerServiceService.getNextTicket(id, employeeId))
}

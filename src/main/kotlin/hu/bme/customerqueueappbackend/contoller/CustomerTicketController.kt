package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import hu.bme.customerqueueappbackend.service.CustomerTicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(CustomerTicketController.BASE_URL)
class CustomerTicketController(
    private val customerTicketService: CustomerTicketService
) {
    companion object {
        const val BASE_URL = "/api/tickets"
    }

    @PostMapping("/forServiceType/{serviceTypeId}")
    fun createTicket(@PathVariable serviceTypeId: UUID): ResponseEntity<CustomerTicketDto>
        = ResponseEntity.ok(customerTicketService.create(serviceTypeId))

    @PatchMapping("/{id}/delay")
    fun delayTicket(@PathVariable id: UUID, @RequestParam minutes: Int): ResponseEntity<CustomerTicketDto>
        = ResponseEntity.ok(customerTicketService.delayTicket(id, minutes))

    @DeleteMapping("/{id}")
    fun deleteTicket(@PathVariable id: UUID): ResponseEntity<Unit>
        = ResponseEntity.ok(customerTicketService.deleteTicket(id))

    @GetMapping("/{id}")
    fun getTicket(@PathVariable id: UUID): ResponseEntity<CustomerTicketDto>
        = ResponseEntity.ok(customerTicketService.getTicket(id))
}

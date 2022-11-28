package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.OwnerDto
import hu.bme.customerqueueappbackend.service.OwnerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(OwnerController.BASE_URL)
class OwnerController(
    private val ownerService: OwnerService
) {
    companion object {
        const val BASE_URL = "/api/owners"
    }

    @GetMapping("/{id}")
    fun getOwner(@PathVariable id: UUID): ResponseEntity<OwnerDto>
            = ResponseEntity.ok(ownerService.getOwner(id))

}

package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.ServiceTypeDto
import hu.bme.customerqueueappbackend.dto.request.CreateServiceTypeRequest
import hu.bme.customerqueueappbackend.service.ServiceTypeService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(ServiceTypeController.BASE_URL)
class ServiceTypeController (
    private val serviceTypeService: ServiceTypeService
) {
    companion object {
        const val BASE_URL = "/api/serviceTypes"
    }

    @PostMapping
    fun saveServiceType(@RequestBody createServiceTypeRequest: CreateServiceTypeRequest): ResponseEntity<ServiceTypeDto> =
        ResponseEntity.ok(serviceTypeService.saveServiceType(createServiceTypeRequest))

    @GetMapping("/forCustomerService/{customerServiceId}")
    fun getServiceTypes(@PathVariable customerServiceId: UUID): ResponseEntity<List<ServiceTypeDto>> =
        ResponseEntity.ok(serviceTypeService.getServiceTypes(customerServiceId))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: UUID): ResponseEntity<Unit> {
        serviceTypeService.deleteServiceType(id)
        return ResponseEntity.ok(Unit)
    }
}

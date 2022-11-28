package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.EmployeeDto
import hu.bme.customerqueueappbackend.service.EmployeeService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping(EmployeeController.BASE_URL)
class EmployeeController(
    private val employeeService: EmployeeService
) {
    companion object {
        const val BASE_URL = "/api/employees"
    }

    @GetMapping("/{id}")
    fun getEmployee(@PathVariable id: UUID): ResponseEntity<EmployeeDto>
            = ResponseEntity.ok(employeeService.getEmployee(id))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    fun deleteEmployee(@PathVariable id: UUID): ResponseEntity<Unit> =
        ResponseEntity.ok(employeeService.deleteEmployee(id))

}

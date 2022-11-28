package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.AdminDto
import hu.bme.customerqueueappbackend.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(AdminController.BASE_URL)
class AdminController(
    private val adminService: AdminService
) {
    companion object {
        const val BASE_URL = "/api/admins"
    }

    @GetMapping("/{id}")
    fun getAdmin(@PathVariable id: UUID): ResponseEntity<AdminDto>
            = ResponseEntity.ok(adminService.getAdmin(id))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    fun deleteAdmin(@PathVariable id: UUID): ResponseEntity<Unit> =
        ResponseEntity.ok(adminService.deleteAdmin(id))

}
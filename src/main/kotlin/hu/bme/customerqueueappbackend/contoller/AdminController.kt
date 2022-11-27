package hu.bme.customerqueueappbackend.contoller

import hu.bme.customerqueueappbackend.dto.AdminDto
import hu.bme.customerqueueappbackend.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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

}
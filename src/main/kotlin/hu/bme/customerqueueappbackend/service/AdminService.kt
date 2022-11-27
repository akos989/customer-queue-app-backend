package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.AdminDto
import java.util.*

interface AdminService {

    fun getAdmin(id: UUID): AdminDto

    fun deleteAdmin(id:UUID)
}
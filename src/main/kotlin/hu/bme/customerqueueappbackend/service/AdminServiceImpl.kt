package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.AdminDto
import hu.bme.customerqueueappbackend.repository.AdminRepository
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class AdminServiceImpl (
    private val mapper: ModelMapper,
    private val adminRepository: AdminRepository
): AdminService {

    override fun getAdmin(id: UUID): AdminDto? {
        val admin = adminRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Admin was not found")
        return admin.toDto(mapper)
    }

}
package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.OwnerDto
import hu.bme.customerqueueappbackend.repository.OwnerRepository
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class OwnerServiceImpl (
    private val mapper: ModelMapper,
    private val ownerRepository: OwnerRepository
): OwnerService {

    override fun getOwner(id: UUID): OwnerDto {
        val owner = ownerRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Owner was not found")
        return owner.toDto(mapper)
    }

}
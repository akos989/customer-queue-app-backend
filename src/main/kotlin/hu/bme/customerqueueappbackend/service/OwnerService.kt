package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.OwnerDto
import java.util.*

interface OwnerService {

    fun getOwner(id: UUID): OwnerDto
}
package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.ServiceTypeDto
import hu.bme.customerqueueappbackend.dto.request.CreateServiceTypeRequest
import java.util.UUID

interface ServiceTypeService {
    fun saveServiceType(createServiceTypeRequest: CreateServiceTypeRequest): ServiceTypeDto

    fun getServiceTypes(customerServiceId: UUID): List<ServiceTypeDto>

    fun deleteServiceType(id: UUID)
}
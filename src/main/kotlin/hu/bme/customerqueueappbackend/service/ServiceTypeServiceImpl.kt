package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.ServiceTypeDto
import hu.bme.customerqueueappbackend.dto.request.CreateServiceTypeRequest
import hu.bme.customerqueueappbackend.model.ServiceType
import hu.bme.customerqueueappbackend.repository.ServiceTypeRepository
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.transaction.Transactional

@Service
class ServiceTypeServiceImpl (
    private val serviceTypeRepository: ServiceTypeRepository,
    private val mapper: ModelMapper
): ServiceTypeService {

    @Transactional
    override fun saveServiceType(createServiceTypeRequest: CreateServiceTypeRequest): ServiceTypeDto {
        return ServiceTypeDto().run {
            val serviceType = ServiceType(name = createServiceTypeRequest.name, handleTime = createServiceTypeRequest.handleTime)
            serviceTypeRepository.save(serviceType)
            // Todo: add service type to customer service by createServiceTypeRequest.customerServiceId
            serviceType.toDto(mapper)
        }
    }

    override fun getServiceTypes(customerServiceId: UUID): List<ServiceTypeDto> {
        val serviceTypes = serviceTypeRepository.findAll()
        return serviceTypes.toDto(mapper)
    }

    @Transactional
    override fun deleteServiceType(id: UUID) {
        val serviceType = findServiceTypeById(id)
        serviceTypeRepository.delete(serviceType)
    }

    private fun findServiceTypeById(id: UUID): ServiceType {
        return serviceTypeRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Service Type not found")
    }
}

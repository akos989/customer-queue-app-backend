package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.ServiceTypeDto
import hu.bme.customerqueueappbackend.dto.request.CreateServiceTypeRequest
import hu.bme.customerqueueappbackend.model.ServiceType
import hu.bme.customerqueueappbackend.repository.CustomerServiceRepository
import hu.bme.customerqueueappbackend.repository.CustomerTicketRepository
import hu.bme.customerqueueappbackend.repository.ServiceTypeRepository
import hu.bme.customerqueueappbackend.util.exceptions.BadRequestException
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class ServiceTypeServiceImpl (
    private val serviceTypeRepository: ServiceTypeRepository,
    private val customerServiceRepository: CustomerServiceRepository,
    private val customerTicketRepository: CustomerTicketRepository,
    private val mapper: ModelMapper
): ServiceTypeService {

    @Transactional
    override fun saveServiceType(createServiceTypeRequest: CreateServiceTypeRequest): ServiceTypeDto {
        val customerService = customerServiceRepository.findByIdOrNull(createServiceTypeRequest.customerServiceId) ?: throw EntityNotFoundException("Customer service was not found")
        val serviceType = ServiceType(name = createServiceTypeRequest.name, handleTime = createServiceTypeRequest.handleTime, customerService = customerService)
        
        serviceTypeRepository.save(serviceType)

        return serviceType.toDto(mapper)
    }

    override fun getServiceTypes(customerServiceId: UUID): List<ServiceTypeDto> {
        val serviceTypes = serviceTypeRepository.findAll()
        return serviceTypes.toDto(mapper)
    }

    @Transactional
    override fun deleteServiceType(id: UUID) {
        val serviceType = serviceTypeRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Service Type not found")

        if (customerTicketRepository.findFirstByServiceType(serviceType) == null) {
            serviceTypeRepository.delete(serviceType)
        } else {
            throw BadRequestException("At least one ticket has not been completed in this service type")
        }
    }
}

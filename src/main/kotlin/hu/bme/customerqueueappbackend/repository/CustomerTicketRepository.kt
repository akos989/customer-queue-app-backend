package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.CustomerService
import hu.bme.customerqueueappbackend.model.CustomerTicket
import hu.bme.customerqueueappbackend.model.ServiceType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerTicketRepository: JpaRepository<CustomerTicket, UUID> {
    fun findAllByCustomerServiceAndHandleStartTimeStampNullAndIdNotOrderByWaitingPeopleNumber(customerService: CustomerService, id: UUID): List<CustomerTicket>

    fun findAllByCustomerServiceAndHandleStartTimeStampNullOrderByWaitingPeopleNumber(customerService: CustomerService): List<CustomerTicket>

    fun findAllByCustomerServiceAndHandleStartTimeStampIsNotNull(customerService: CustomerService): List<CustomerTicket>

    fun findFirstByServiceType(serviceType: ServiceType): CustomerTicket?
}

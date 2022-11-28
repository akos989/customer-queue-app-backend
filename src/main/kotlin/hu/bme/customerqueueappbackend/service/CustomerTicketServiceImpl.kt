package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import hu.bme.customerqueueappbackend.model.CustomerTicket
import hu.bme.customerqueueappbackend.model.ServiceType
import hu.bme.customerqueueappbackend.repository.CustomerTicketRepository
import hu.bme.customerqueueappbackend.repository.ServiceTypeRepository
import hu.bme.customerqueueappbackend.util.exceptions.CannotDelayTicketException
import hu.bme.customerqueueappbackend.util.exceptions.EntityNotFoundException
import hu.bme.customerqueueappbackend.util.extensions.toDto
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional
import kotlin.math.absoluteValue

@Service
class CustomerTicketServiceImpl (
    private val customerTicketRepository: CustomerTicketRepository,
    private val serviceTypeRepository: ServiceTypeRepository,
    private val waitingTimeCalculationService: WaitingTimeCalculationService,
    private val mapper: ModelMapper
): CustomerTicketService {
    companion object {
       const val HANDLE_TIME_CONSTANT = 10
    }

    @Transactional
    override fun create(serviceTypeId: UUID): CustomerTicketDto {
        val serviceType = findServiceTypeById(serviceTypeId)
        val customerService = serviceType.customerService
        val ticket = CustomerTicket(
            serviceType = serviceType,
            customerService = customerService,
            callTime = waitingTimeCalculationService.callTimeForNewCustomerTicket(customerService),
            waitingPeopleNumber = customerService.waitingTickets.count() - 1
        )
        customerTicketRepository.save(ticket)
        return CustomerTicketDto(
            id = ticket.id,
            serviceTypeName = ticket.serviceType.name,
            callTime = ticket.callTime,
            waitingPeopleNumber = ticket.waitingPeopleNumber
        )
    }

    @Transactional
    override fun delayTicket(id: UUID, minutes: Int): CustomerTicketDto {
        val delayedTicket = findCustomerTicketById(id)
        val customerService = delayedTicket.customerService
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = delayedTicket.callTime
        calendar.add(Calendar.MINUTE, minutes.absoluteValue)
        val exceptedNewCallTime = calendar.time

        if (delayedTicket.handleStartTimeStamp != null) {
            throw CannotDelayTicketException("Ticket was already called")
        }

        if (customerService.waitingTickets.maxOf { it.waitingPeopleNumber } == delayedTicket.waitingPeopleNumber) {
            throw CannotDelayTicketException("Ticket is last in line")
        }

        delayedTicket.callTime = waitingTimeCalculationService.refreshCallTimesAfterDelay(customerService, exceptedNewCallTime, delayedTicket)

        return CustomerTicketDto(
            id = delayedTicket.id,
            serviceTypeName = delayedTicket.serviceType.name,
            callTime = delayedTicket.callTime,
            waitingPeopleNumber = delayedTicket.waitingPeopleNumber
        )
    }

    @Transactional
    override fun deleteTicket(id: UUID) {
        val deleteTicket = findCustomerTicketById(id)
        val ticketCustomerService = deleteTicket.customerService
        val ticketServiceType = deleteTicket.serviceType

        val ticketHandleMinutes = if (deleteTicket.handleStartTimeStamp != null) {
                (Date().time - deleteTicket.handleStartTimeStamp!!.time) / (1000 * 60)
            } else {
                0
        }
        ticketServiceType.handleTime = (
                (ticketServiceType.handleTime * HANDLE_TIME_CONSTANT + ticketHandleMinutes) /
                        (HANDLE_TIME_CONSTANT + 1)
                ).toInt()
        customerTicketRepository.deleteById(id)

       waitingTimeCalculationService.refreshCallTimesAfterDelete(ticketCustomerService, deleteTicket.id)
    }

    override fun getTicket(id: UUID): CustomerTicketDto {
        val ticket = findCustomerTicketById(id)
        val ticketDto: CustomerTicketDto = ticket.toDto(mapper)
        return ticketDto.apply {
            serviceTypeName = ticket.serviceType.name
        }
    }

    private fun findCustomerTicketById(id: UUID): CustomerTicket
            = customerTicketRepository.findByIdOrNull(id)
        ?: throw EntityNotFoundException("Customer Ticket not found")

    private fun findServiceTypeById(id: UUID): ServiceType
            = serviceTypeRepository.findByIdOrNull(id)
        ?: throw EntityNotFoundException("Service Type not found")
}

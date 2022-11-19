package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.dto.CustomerTicketDto
import hu.bme.customerqueueappbackend.model.CustomerTicket
import hu.bme.customerqueueappbackend.model.ServiceType
import hu.bme.customerqueueappbackend.repository.CustomerTicketRepository
import hu.bme.customerqueueappbackend.repository.ServiceTypeRepository
import hu.bme.customerqueueappbackend.util.exceptions.CannotDelayTicketException
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.transaction.Transactional

@Service
class CustomerTicketServiceImpl (
    private val customerTicketRepository: CustomerTicketRepository,
    private val serviceTypeRepository: ServiceTypeRepository,
    private val mapper: ModelMapper
): CustomerTicketService {

    @Transactional
    override fun create(serviceTypeId: UUID): CustomerTicketDto {
        val serviceType = findServiceTypeById(serviceTypeId)
        val customerService = serviceType.customerService
        val ticket = CustomerTicket(
            serviceType = serviceType,
            customerService = customerService,
            waitingTime = customerService.waitingTickets.sumOf { it.serviceType.handleTime },
            waitingPeopleNumber = customerService.waitingTickets.count() - 1
        )
        customerTicketRepository.save(ticket)
        return CustomerTicketDto(
            id = ticket.id,
            customerServiceName = ticket.customerService.name,
            waitingTime = ticket.waitingTime,
            waitingPeopleNumber = ticket.waitingPeopleNumber
        )
    }

    // if ticket is last        --> cannot delay, throw exception Todo
    // if ticket is first       --> tell employee that new ticket is needed, or just update with the new ticket and refresh for the employee Todo
    // if in between tickets    -->
    //      has enough people behind to delay   --> success: delay with the least amount of time that is more than the specified Todo
    //      not enough people behind as specified  --> partial success: bring to last of list Todo
    // if ticket is changed inform all other tickets about the change Todo
    @Transactional
    override fun delayTicket(id: UUID, minutes: Int): CustomerTicketDto {
        val ticket = findCustomerTicketById(id)
        val customerService = ticket.customerService
        val waitingTickets = customerService.waitingTickets
            .sortedBy { it.waitingTime }
            .toMutableList()

        if (customerService.waitingTickets.last().id == ticket.id) {
            throw CannotDelayTicketException("Ticket is last in line")
        }
        if (customerService.waitingTickets.first().id == ticket.id) {
            throw CannotDelayTicketException("Ticket was already called")
            // it could be possible to delay these kind of tickets, but it needs WebSocket connection with the employee as well
        }

        val ticketHandleTime = ticket.serviceType.handleTime
        waitingTickets.remove(ticket)
        val movedTickets = waitingTickets.filter {
            ticket.waitingTime < it.waitingTime &&
                    ticket.waitingTime + minutes > it.waitingTime - ticketHandleTime
        }
        movedTickets.forEach {
            it.waitingTime -= ticketHandleTime
            it.waitingPeopleNumber -= 1
        } // Todo: notify tickets
        ticket.waitingTime = movedTickets.last().waitingTime + movedTickets.last().serviceType.handleTime
        ticket.waitingPeopleNumber += movedTickets.count()

        return CustomerTicketDto(
            id = ticket.id,
            customerServiceName = ticket.customerService.name,
            waitingTime = ticket.waitingTime,
            waitingPeopleNumber = ticket.waitingPeopleNumber
        )
    }

    private fun findCustomerTicketById(id: UUID): CustomerTicket
            = customerTicketRepository.findByIdOrNull(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Ticket not found")

    private fun findServiceTypeById(id: UUID): ServiceType
            = serviceTypeRepository.findByIdOrNull(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Service Type not found")
}

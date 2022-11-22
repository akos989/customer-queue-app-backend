package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.model.CustomerService
import hu.bme.customerqueueappbackend.model.CustomerTicket
import hu.bme.customerqueueappbackend.repository.CustomerTicketRepository
import hu.bme.customerqueueappbackend.repository.EmployeeRepository
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class WaitingTimeCalculationServiceImpl(
    private val customerTicketRepository: CustomerTicketRepository,
    private val employeeRepository: EmployeeRepository
): WaitingTimeCalculationService {
    @Transactional
    override fun refreshCallTimesFor(customerService: CustomerService) {
        val workingEmployeeNumber = employeeRepository.findAllByCustomerServiceAndHelpDeskNumberNot(customerService, 0).count()
        val waitingTickets = customerTicketRepository.findAllByCustomerServiceAndHandleStartTimeStampNullOrderByWaitingPeopleNumber(customerService)

        if (workingEmployeeNumber == 0) {
            waitingTickets.forEach {
                it.callTime = null
            }
        } else {
            val helpDesks = initializeHelpDesks(customerService, workingEmployeeNumber)

            waitingTickets.forEach { ticket ->
                val minWaitingTimeHelpDesk = helpDesks.minByOrNull { it.waitingTimeSum }!!
                ticket.callTime = addMinutesToDate(Date(), minWaitingTimeHelpDesk.waitingTimeSum)
                minWaitingTimeHelpDesk.waitingTimeSum += ticket.serviceType.handleTime
            }
        }
    }

    @Transactional
    override fun refreshCallTimesAfterDelete(customerService: CustomerService, excludedTicketId: UUID) {
        val workingEmployeeNumber = employeeRepository.findAllByCustomerServiceAndHelpDeskNumberNot(customerService, 0).count()
        val waitingTickets = customerTicketRepository.findAllByCustomerServiceAndHandleStartTimeStampNullAndIdNotOrderByWaitingPeopleNumber(customerService, excludedTicketId)

        if (workingEmployeeNumber == 0) {
            waitingTickets.forEach {
                it.callTime = null
                it.waitingPeopleNumber -= 1
            }
        } else {
            val helpDesks = initializeHelpDesks(customerService, workingEmployeeNumber)

            waitingTickets.forEach { ticket ->
                val minWaitingTimeHelpDesk = helpDesks.minByOrNull { it.waitingTimeSum }!!
                ticket.callTime = addMinutesToDate(Date(), minWaitingTimeHelpDesk.waitingTimeSum)
                ticket.waitingPeopleNumber -= 1
                minWaitingTimeHelpDesk.waitingTimeSum += ticket.serviceType.handleTime
            }
        }
    }

    override fun callTimeForNewCustomerTicket(customerService: CustomerService): Date? {
        val workingEmployeeNumber = employeeRepository.findAllByCustomerServiceAndHelpDeskNumberNot(customerService, 0).count()
        val waitingTickets = customerTicketRepository.findAllByCustomerServiceAndHandleStartTimeStampNullOrderByWaitingPeopleNumber(customerService)

        return if (workingEmployeeNumber == 0) {
            null
        } else {
            val helpDesks = initializeHelpDesks(customerService, workingEmployeeNumber)
            waitingTickets.forEach { ticket ->
                val minWaitingTimeHelpDesk = helpDesks.minByOrNull { it.waitingTimeSum }!!
                minWaitingTimeHelpDesk.waitingTimeSum += ticket.serviceType.handleTime
            }
            val minWaitingTimeHelpDesk = helpDesks.minByOrNull { it.waitingTimeSum }!!
            addMinutesToDate(Date(), minWaitingTimeHelpDesk.waitingTimeSum)
        }
    }

    @Transactional
    override fun refreshCallTimesAfterDelay(customerService: CustomerService, expectedNewCallTime: Date, delayedTicket: CustomerTicket): Date? {
        val workingEmployeeNumber = employeeRepository.findAllByCustomerServiceAndHelpDeskNumberNot(customerService, 0).count()
        val waitingTickets = customerTicketRepository.findAllByCustomerServiceAndHandleStartTimeStampNullAndIdNotOrderByWaitingPeopleNumber(customerService, delayedTicket.id)

        if (workingEmployeeNumber == 0) {
            waitingTickets.forEach { it.callTime = null }
            return null
        } else {
            val helpDesks = initializeHelpDesks(customerService, workingEmployeeNumber)
            var foundNewCallTime = false
            waitingTickets.forEach { ticket ->
                var minWaitingTimeHelpDesk = helpDesks.minByOrNull { it.waitingTimeSum }!!
                var callTime = addMinutesToDate(Date(), minWaitingTimeHelpDesk.waitingTimeSum)

                if (!foundNewCallTime && callTime > expectedNewCallTime) {
                    foundNewCallTime = true
                    delayedTicket.callTime = callTime
                    minWaitingTimeHelpDesk.waitingTimeSum += delayedTicket.serviceType.handleTime
                    minWaitingTimeHelpDesk = helpDesks.minByOrNull { it.waitingTimeSum }!!
                    callTime = addMinutesToDate(Date(), minWaitingTimeHelpDesk.waitingTimeSum)
                }

                ticket.callTime = callTime
                minWaitingTimeHelpDesk.waitingTimeSum += ticket.serviceType.handleTime
            }
            if (!foundNewCallTime) {
                val minWaitingTimeHelpDesk = helpDesks.minByOrNull { it.waitingTimeSum }!!
                delayedTicket.callTime = addMinutesToDate(Date(), minWaitingTimeHelpDesk.waitingTimeSum)
            }
            return delayedTicket.callTime
        }
    }

    private fun addMinutesToDate(date: Date, minutes: Int): Date {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }

    private fun initializeHelpDesks(customerService: CustomerService, workingEmployeeNumber: Int):List<HelpDesk> {
        val currentlyHandledTickets =
            customerTicketRepository.findAllByCustomerServiceAndHandleStartTimeStampIsNotNull(customerService)
        val helpDesks: MutableList<HelpDesk> = mutableListOf()
        for (i in 0 until workingEmployeeNumber) {
            val currentlyHandledTicket = currentlyHandledTickets.getOrNull(i)
            if (currentlyHandledTicket == null) {
                helpDesks.add(HelpDesk())
            } else {
                val remainingHandleMinutes = currentlyHandledTicket.serviceType.handleTime -
                        ((Date().time - currentlyHandledTicket.handleStartTimeStamp!!.time) / (1000 * 60))
                helpDesks.add(
                    HelpDesk(
                        if (remainingHandleMinutes >= 0) remainingHandleMinutes.toInt() else 0
                    )
                )
            }
        }
        return helpDesks
    }

    private class HelpDesk(
        var waitingTimeSum: Int = 0
    )
}

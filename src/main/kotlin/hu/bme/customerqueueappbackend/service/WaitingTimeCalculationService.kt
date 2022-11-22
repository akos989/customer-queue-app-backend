package hu.bme.customerqueueappbackend.service

import hu.bme.customerqueueappbackend.model.CustomerService
import hu.bme.customerqueueappbackend.model.CustomerTicket
import java.util.*

interface WaitingTimeCalculationService {
    fun refreshCallTimesFor(customerService: CustomerService)

    fun refreshCallTimesAfterDelete(customerService: CustomerService, excludedTicketId: UUID)

    fun callTimeForNewCustomerTicket(customerService: CustomerService): Date?

    fun refreshCallTimesAfterDelay(customerService: CustomerService, expectedNewCallTime: Date, delayedTicket: CustomerTicket): Date?
}
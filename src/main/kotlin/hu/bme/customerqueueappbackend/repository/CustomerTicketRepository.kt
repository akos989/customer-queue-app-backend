package hu.bme.customerqueueappbackend.repository

import hu.bme.customerqueueappbackend.model.CustomerTicket
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerTicketRepository: JpaRepository<CustomerTicket, UUID>

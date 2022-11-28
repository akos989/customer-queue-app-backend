package hu.bme.customerqueueappbackend.model

import java.util.UUID
import javax.persistence.*

@Entity
class CustomerService(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column
    val name: String = "",

    @OneToMany(mappedBy = "customerService")
    val waitingTickets: MutableList<CustomerTicket> = mutableListOf(),

    @OneToMany(mappedBy = "customerService", fetch = FetchType.EAGER)
    val serviceTypes: List<ServiceType> = listOf(),

    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    val owner: Owner = Owner()
)

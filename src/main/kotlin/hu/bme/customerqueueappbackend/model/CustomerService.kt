package hu.bme.customerqueueappbackend.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class CustomerService(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column
    val name: String = "",

    @OneToMany(mappedBy = "customerService")
    val waitingTickets: List<CustomerTicket> = listOf(),

    @OneToMany(mappedBy = "customerService")
    val employees: List<Employee> = listOf(),

    @OneToMany(mappedBy = "customerService")
    val admins: List<Admin> = listOf(),

    @OneToMany(mappedBy = "customerService")
    val serviceTypes: List<ServiceType> = listOf(),

    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    val owner: Owner = Owner()
)

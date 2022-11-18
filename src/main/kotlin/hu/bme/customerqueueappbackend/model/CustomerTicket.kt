package hu.bme.customerqueueappbackend.model

import java.util.Date
import java.util.UUID
import javax.persistence.*

@Entity
class CustomerTicket(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column
    val startTimeStamp: Date = Date(),

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceTypeId", referencedColumnName = "id")
    val serviceType: ServiceType = ServiceType(),

    @ManyToOne
    @JoinColumn(name = "customerServiceId", nullable = false)
    val customerService: CustomerService = CustomerService()
)

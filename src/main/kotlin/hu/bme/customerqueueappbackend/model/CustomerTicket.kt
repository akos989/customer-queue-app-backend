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
    var handleStartTimeStamp: Date? = null,

    @Column
    var callTime: Date? = null,

    @Column
    var waitingPeopleNumber: Int = 0,

    @Column
    var ticketNumber: Int = 0,

    @Column
    var handleDesk: Int = 0,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceTypeId", referencedColumnName = "id")
    val serviceType: ServiceType = ServiceType(),

    @ManyToOne
    @JoinColumn(name = "customerServiceId", nullable = false)
    val customerService: CustomerService = CustomerService()
)

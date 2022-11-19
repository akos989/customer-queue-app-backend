package hu.bme.customerqueueappbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID
import javax.persistence.*

@Entity
class ServiceType(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var handleTime: Int = 0,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customerServiceId", nullable = false)
    val customerService: CustomerService = CustomerService()
)

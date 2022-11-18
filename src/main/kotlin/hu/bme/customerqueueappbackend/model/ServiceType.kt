package hu.bme.customerqueueappbackend.model

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class ServiceType(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var handleTime: Int = 0
)

package hu.bme.customerqueueappbackend.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Employee(
    email: String = "",
    password: String = "",
    roles: MutableSet<Role> = hashSetOf(),

    @Column
    val helpDeskNumber: Int = 0,

    @ManyToOne
    @JoinColumn(name = "customerServiceId", nullable = true)
    val customerService: CustomerService = CustomerService()
): User(email = email, password = password, roles = roles)

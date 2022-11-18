package hu.bme.customerqueueappbackend.model

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Admin(
    email: String = "",
    password: String = "",
    roles: MutableSet<Role> = hashSetOf(),

    @ManyToOne
    @JoinColumn(name = "customerServiceId", nullable = true)
    val customerService: CustomerService = CustomerService()
): User(email = email, password = password, roles = roles)

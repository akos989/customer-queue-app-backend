package hu.bme.customerqueueappbackend.model

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
class Owner(
    email: String = "",
    password: String = "",
    roles: MutableSet<Role> = hashSetOf(),

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    val customerServices: List<CustomerService> = listOf()
): User(email = email, password = password, roles = roles)
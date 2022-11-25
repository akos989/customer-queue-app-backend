package hu.bme.customerqueueappbackend.model

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
@DiscriminatorValue("3")
class Owner(
    email: String = "",
    password: String = "",
    roles: MutableSet<Role> = hashSetOf(),

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    val customerServices: List<CustomerService> = listOf()
): User(email = email, password = password, roles = roles)
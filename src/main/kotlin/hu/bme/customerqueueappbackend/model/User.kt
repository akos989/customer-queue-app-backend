package hu.bme.customerqueueappbackend.model

import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
class User(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Email
    @Column(nullable = false, unique = true)
    val email: String = "",

    @Column(nullable = false)
    val password: String = "",

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles")
    var roles: MutableSet<Role> = hashSetOf()
)

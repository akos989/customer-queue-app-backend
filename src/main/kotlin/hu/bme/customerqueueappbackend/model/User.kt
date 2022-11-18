package hu.bme.customerqueueappbackend.model

import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class User(
    @Id
    @Column(columnDefinition = "uuid")
    open val id: UUID = UUID.randomUUID(),

    @Email
    @Column(nullable = false, unique = true)
    open val email: String = "",

    @Column(nullable = false)
    open val password: String = "",

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles")
    open var roles: MutableSet<Role> = hashSetOf()
)

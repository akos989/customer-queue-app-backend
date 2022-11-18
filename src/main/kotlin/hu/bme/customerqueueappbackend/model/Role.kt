package hu.bme.customerqueueappbackend.model

import hu.bme.customerqueueappbackend.security.authorization.RoleType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID
import javax.persistence.*

@Entity
class Role(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    var name: RoleType = RoleType.ADMIN
) {
    val grantedAuthority: SimpleGrantedAuthority
        get() = SimpleGrantedAuthority("ROLE_$name")
}

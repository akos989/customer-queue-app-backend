package hu.bme.customerqueueappbackend.security.authentication

import hu.bme.customerqueueappbackend.model.User
import org.springframework.security.core.userdetails.UserDetails

class AuthUserDetails(val user: User) : UserDetails {

    override fun getAuthorities() = user.roles.map { it.grantedAuthority }

    override fun getPassword() = user.password

    override fun getUsername() = user.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}

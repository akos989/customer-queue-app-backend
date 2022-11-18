package hu.bme.customerqueueappbackend.security.authentication

import hu.bme.customerqueueappbackend.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findFirstByEmail(email)?.let {
            AuthUserDetails(it)
        } ?: throw EntityNotFoundException("User not found")
    }
}
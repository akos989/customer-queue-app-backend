package hu.bme.customerqueueappbackend.security.jwtutils

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenManager: TokenManager
): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val tokenHeader = request.getHeader("Authorization")
        var emailAddress = ""
        var token = ""
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7)
            try {
                emailAddress = tokenManager.getUsernameFromToken(token)
            } catch (e: IllegalArgumentException) {
                println("unable to get jwt token")
            } catch (e: ExpiredJwtException) {
                println("token expired")
            } catch (e: Exception) {
                println("token exception")
            }
        } else {
            println("bearer string not found in token")
        }
        if (emailAddress.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(emailAddress)
            if (tokenManager.validateJwtToken(token, userDetails)!!) {
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.authorities
                )
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val excludedPaths: List<String> = listOf(
            "/customer-queue-app/api/auth",
            "/customer-queue-app/h2-console/",
            "/customer-queue-app/swagger-ui/",
            "/customer-queue-app/v3/api-docs/"

//            "/customer-queue-app/api/auth/logout"
//          Todo: add more paths, e.g.: the paths the unauthenticated customer will use
//          Todo: add these to a central static constants class
        )
        return excludedPaths.any { request.requestURI.startsWith(it) }
//        request.requestURI.startsWith()
//        return excludedPaths.contains(request.requestURI)
    }
}

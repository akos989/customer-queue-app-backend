package hu.bme.customerqueueappbackend.security.jwtutils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*


@Component
class TokenManager {
    private val TOKEN_VALIDITY: Long = 10 * 60 * 60
    private val JWT_SECRET: String = "verybigsecret"

    fun generateJwtToken(userEmail: String, authorities: MutableCollection<out GrantedAuthority>): Pair<String, Date> {
        val claims: Map<String, Any> = mutableMapOf()
        val expirationDate = Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)
        val tokenString = Jwts.builder()
            .setClaims(claims)
            .setSubject(userEmail)
            .setIssuedAt(Date())
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .compact()
        return Pair(first = tokenString, second = expirationDate)
    }

    fun validateJwtToken(token: String?, userDetails: UserDetails): Boolean? {
        val username = getUsernameFromToken(token)
        val claims: Claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).body
        val isTokenExpired = claims.expiration.before(Date())
        return username == userDetails.username && !isTokenExpired
    }

    fun getUsernameFromToken(token: String?): String {
        val claims: Claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).body
        return claims.subject
    }
}

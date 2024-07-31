package com.bada.weolbu.auth

import com.bada.weolbu.user.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long,
)

@Component
class JwtProvider(private val properties: JwtProperties) {
    private val secretKey = Keys.hmacShaKeyFor(properties.secret.toByteArray())
    fun generate(user: User): String {
        return Jwts.builder()
            .claims()
            .add("user_id", user.id)
            .add("email", user.email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + properties.accessTokenExpiration))
            .and()
            .signWith(secretKey)
            .compact()
    }

    fun validate(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(secretKey).build()
            return true;
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: ExpiredJwtException) {
            return false
        } catch (e: UnsupportedJwtException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return false
    }

    fun getClaims(token: String): Claims{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload
    }
}
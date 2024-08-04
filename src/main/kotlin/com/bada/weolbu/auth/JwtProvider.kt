package com.bada.weolbu.auth

import com.bada.weolbu.entity.User
import com.fasterxml.jackson.annotation.JsonProperty
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.rmi.ServerException
import java.util.*

@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties(
    @field:JsonProperty("access-token-secret") val accessTokenSecret: String,
    @field:JsonProperty("refresh-token-secret") val refreshTokenSecret: String,
    @field:JsonProperty("access-token-expiration") val accessTokenExpiration: Long,
    @field:JsonProperty("refresh-token-expiration") val refreshTokenExpiration: Long,
)

enum class TokenType {
    AccessToken, RefreshToken
}

@Component
class JwtProvider(private val properties: JwtProperties) {
    private val accessTokenSecretKey = Keys.hmacShaKeyFor(properties.accessTokenSecret.toByteArray())
    private val refreshTokenSecretKey = Keys.hmacShaKeyFor(properties.refreshTokenSecret.toByteArray())
    fun generate(user: User, type: TokenType): String {
        val expiration = when {
            type == TokenType.AccessToken -> properties.accessTokenExpiration
            type == TokenType.RefreshToken -> properties.refreshTokenExpiration
            else -> throw ServerException("Invalid JWT token type")
        }
        val secretKey = when {
            type == TokenType.AccessToken -> accessTokenSecretKey
            type == TokenType.RefreshToken -> refreshTokenSecretKey
            else -> throw ServerException("Invalid JWT token type")
        }
        return Jwts.builder()
            .claims()
            .add("user_id", user.id)
            .add("email", user.email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .and()
            .signWith(secretKey)
            .compact()
    }

    fun validate(token: String, type: TokenType): Boolean {
        try {
            val secretKey = when {
                type == TokenType.AccessToken -> accessTokenSecretKey
                type == TokenType.RefreshToken -> refreshTokenSecretKey
                else -> throw ServerException("Invalid JWT token type")
            }
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload
            return true;
        } catch (e: MalformedJwtException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: ExpiredJwtException) {
            e.printStackTrace()
        } catch (e: UnsupportedJwtException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: JwtException) {
            e.printStackTrace()
        }
        return false
    }

    fun getClaims(token: String): Claims {
        return Jwts.parser().verifyWith(accessTokenSecretKey).build().parseSignedClaims(token).payload
    }
}
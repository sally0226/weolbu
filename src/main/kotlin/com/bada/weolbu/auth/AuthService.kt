package com.bada.weolbu.auth

import com.bada.weolbu.entity.RefreshToken
import com.bada.weolbu.auth.model.SignInResponseDTO
import com.bada.weolbu.auth.model.SignupRequest
import com.bada.weolbu.common.exception.DuplicateUserException
import com.bada.weolbu.common.exception.InvalidRefreshTokenException
import com.bada.weolbu.common.exception.UserNotFoundException
import com.bada.weolbu.entity.RefreshTokenRepository
import com.bada.weolbu.entity.UserRepository
import com.bada.weolbu.entity.User
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class AuthService(
    private val authManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider
) {
    fun signup(signupRequest: SignupRequest) {
        val user = User(
            name = signupRequest.name,
            password = this.hashPassword(signupRequest.password),
            email = signupRequest.email,
            role = signupRequest.type,
            phoneNumber = signupRequest.phoneNumber
        )
        try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException) {
            throw DuplicateUserException(signupRequest.email)
        }
    }

    fun signIn(email: String, password: String): SignInResponseDTO {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                password
            )
        )
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)
        upsertRefreshToken(user, refreshToken)
        return SignInResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun refresh(token: String): SignInResponseDTO {
        if (!jwtProvider.validate(token, TokenType.RefreshToken))
            throw InvalidRefreshTokenException()

        val claims = jwtProvider.getClaims(token)
        val email = claims["email"].toString()
        val refreshToken = refreshTokenRepository.findByRefreshToken(token)
            ?.takeIf { it.user.email == email }
            ?: throw InvalidRefreshTokenException()

        val accessToken = createAccessToken(refreshToken.user)
        return SignInResponseDTO(
            accessToken = accessToken,
            refreshToken = token,
        )

    }

    fun upsertRefreshToken(user: User, newRefreshToken: String) {
        val existingToken = refreshTokenRepository.findByUser(user)
        existingToken?.let {
            it.refreshToken = newRefreshToken
            refreshTokenRepository.save(it)
        } ?: {
            val newTokenEntity = RefreshToken(
                user = user,
                refreshToken = newRefreshToken
            )
            refreshTokenRepository.save(newTokenEntity)
        }
    }

    private fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    private fun createAccessToken(user: User) = jwtProvider.generate(user, TokenType.AccessToken)

    private fun createRefreshToken(user: User) = jwtProvider.generate(user, TokenType.RefreshToken)
}
package com.bada.weolbu.auth

import com.bada.weolbu.auth.model.SignInResponseDTO
import com.bada.weolbu.auth.model.SignupRequest
import com.bada.weolbu.common.exception.UserNotFoundException
import com.bada.weolbu.user.UserRepository
import com.bada.weolbu.user.model.User
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class AuthService(
    private val authManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val userDetailsService: CustomUserDetailsService,
    private val jwtProvider: JwtProvider
) {
    fun signup(signupRequest: SignupRequest): SignInResponseDTO {
        val user = User(
            username = signupRequest.username,
            password = this.hashPassword(signupRequest.password),
            email = signupRequest.email,
            type = signupRequest.type,
            phoneNumber = signupRequest.phoneNumber
        )
        userRepository.save(user)

        val accessToken = createAccessToken(user)
        return SignInResponseDTO(
            accessToken = accessToken,
            refreshToken = "",
        )
    }

    fun signin(email: String, password: String): SignInResponseDTO {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                password
            )
        )
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        //        val user = userDetailsService.loadUserByUsername(email)
        val accessToken = createAccessToken(user)
        return SignInResponseDTO(
            accessToken = accessToken,
            refreshToken = "",
        )
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    private fun createAccessToken(user: User) = jwtProvider.generate(user)
}
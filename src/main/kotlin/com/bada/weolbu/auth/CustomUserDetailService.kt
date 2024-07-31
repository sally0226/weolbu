package com.bada.weolbu.auth

import com.bada.weolbu.common.exception.UserNotFoundException
import com.bada.weolbu.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

typealias ApplicationUser = com.bada.weolbu.user.model.User

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails =
        userRepository.findByEmail(email)
            ?.mapToUserDetails()
            ?: throw UserNotFoundException()
    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.email)
            .password(this.password)
            .roles(this.type.name)
            .build()
}
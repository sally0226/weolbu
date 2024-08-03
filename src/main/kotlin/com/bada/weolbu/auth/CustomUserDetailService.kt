package com.bada.weolbu.auth

import com.bada.weolbu.common.exception.UserNotFoundException
import com.bada.weolbu.entity.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails =
        userRepository.findByEmail(email)?.let { it } ?: throw UserNotFoundException()
}
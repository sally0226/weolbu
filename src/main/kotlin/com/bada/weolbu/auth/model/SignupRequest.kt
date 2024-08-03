package com.bada.weolbu.auth.model

import com.bada.weolbu.entity.User
import com.bada.weolbu.entity.UserRole

data class SignupRequest(
//    @field:NotNull(message = "Please provide a username")
    val name: String,
    val phoneNumber: String,
    val email: String,
    val type: UserRole,
    val password: String,
) {
    fun toUser() = User(
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        role = type,
        password = password
    )
}
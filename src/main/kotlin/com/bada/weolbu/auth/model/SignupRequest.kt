package com.bada.weolbu.auth.model

import com.bada.weolbu.user.model.User
import com.bada.weolbu.user.model.UserType

data class SignupRequest(
//    @field:NotNull(message = "Please provide a username")
    val username: String,
    val phoneNumber: String,
    val email: String,
    val type: UserType,
    val password: String,
) {
    fun toUser() = User(
        username = username,
        phoneNumber = phoneNumber,
        email = email,
        type = type,
        password = password
    )
}
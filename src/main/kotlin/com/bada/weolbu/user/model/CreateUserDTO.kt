package com.bada.weolbu.user.model

data class CreateUserDTO(
    val username: String,
    val phoneNumber: String,
    val email: String,
    val type: UserType,
    val password: String,
) {
    fun toEntity(): User {
        return User(
            username = username,
            phoneNumber = phoneNumber,
            email = email,
            password = password,
            type = type
        )
    }
}
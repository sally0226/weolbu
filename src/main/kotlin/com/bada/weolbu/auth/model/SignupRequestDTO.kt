package com.bada.weolbu.auth.model

import com.bada.weolbu.entity.User
import com.bada.weolbu.entity.UserRole
import jakarta.validation.constraints.*

data class SignupRequestDTO(
    @field:NotBlank(message = "Please provide a name")
    val name: String,

    @field:Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "Phone number must be in the format 010-XXXX-XXXX.")
    val phoneNumber: String,

    @field:Email(
        regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
        message = "Email must be in the format example@example.com."
    )
    val email: String,
    val type: UserRole,
    
    @field:Size(min = 6, max = 10, message = "Password must be between 6 and 10 characters.")
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
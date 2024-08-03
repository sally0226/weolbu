package com.bada.weolbu.auth.model

import jakarta.validation.constraints.NotBlank

class SignInRequestDTO(
    @field:NotBlank(message = "Please provide a user email")
    val email: String,

    @field:NotBlank(message = "Please provide a password")
    val password: String,
) {
}
package com.bada.weolbu.auth.model

import jakarta.validation.constraints.NotBlank

class SignInRequestDTO(
    @field:NotBlank(message = "이메일을 입력해주세요.")
    val email: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String,
) {
}
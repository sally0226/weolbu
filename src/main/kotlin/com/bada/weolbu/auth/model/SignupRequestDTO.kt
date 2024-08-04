package com.bada.weolbu.auth.model

import com.bada.weolbu.entity.User
import com.bada.weolbu.entity.UserRole
import jakarta.validation.constraints.*

data class SignupRequestDTO(
    @field:NotBlank(message = "이름을 입력해주세요.")
    val name: String,

    @field:Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대폰번호는 010-XXXX-XXXX 양식으로 입력해주세요.")
    val phoneNumber: String,

    @field:Email(
        regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
        message = "올바른 이메일 양식을 입력해주세요."
    )
    val email: String,
    val role: UserRole,

    @field:Size(min = 6, max = 10, message = "비밀번호는 6자이상 10자 이하이어야 합니다.")
    val password: String,
) {
    fun toUser() = User(
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        role = role,
        password = password
    )
}
package com.bada.weolbu.auth

import com.bada.weolbu.auth.model.SignInRequestDTO
import com.bada.weolbu.auth.model.SignInResponseDTO
import com.bada.weolbu.auth.model.SignupRequestDTO
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody body: SignupRequestDTO) {
        return authService.signup(body)
    }

    @PostMapping("/signIn")
    fun signIn(@Valid @RequestBody body: SignInRequestDTO): SignInResponseDTO {
        return authService.signIn(body.email, body.password)
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestHeader("Refresh-Token") token: String): SignInResponseDTO {
        return authService.refresh(token)
    }
}
package com.bada.weolbu.auth

import com.bada.weolbu.auth.model.SignInDTO
import com.bada.weolbu.auth.model.SignInResponseDTO
import com.bada.weolbu.auth.model.SignupRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/signup")
    fun signup(@RequestBody body: SignupRequest): SignInResponseDTO {
        return authService.signup(body)
    }

    @PostMapping("/signIn")
    fun signIn(@RequestBody body: SignInDTO): SignInResponseDTO {
        return authService.signin(body.email, body.password)
    }
}
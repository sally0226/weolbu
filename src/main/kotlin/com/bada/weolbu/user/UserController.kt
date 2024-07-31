package com.bada.weolbu.user

import com.bada.weolbu.user.model.UserResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/me")
    fun getMe(id: Long): UserResponseDTO {
        return this.userService.findById(id).toResponseDTO();
    }
}
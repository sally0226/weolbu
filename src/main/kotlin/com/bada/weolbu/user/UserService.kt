package com.bada.weolbu.user

import com.bada.weolbu.common.exception.UserNotFoundException
import com.bada.weolbu.user.model.CreateUserDTO
import com.bada.weolbu.entity.User
import com.bada.weolbu.entity.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun create(user: CreateUserDTO): User {
        return userRepository.save(user.toEntity());
    }

    fun findById(id: Long): User {
        val user = userRepository.findById(id);
        if (!user.isPresent) {
            throw UserNotFoundException(id);
        }
        return user.get()
    }
}
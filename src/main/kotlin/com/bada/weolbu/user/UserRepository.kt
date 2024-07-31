package com.bada.weolbu.user

import com.bada.weolbu.user.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findByEmail(email:String): User?
}
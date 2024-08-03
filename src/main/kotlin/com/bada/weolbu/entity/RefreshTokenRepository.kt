package com.bada.weolbu.entity

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {
    fun findByUser(user: User): RefreshToken?
    fun findByRefreshToken(refreshToken: String): RefreshToken?
}
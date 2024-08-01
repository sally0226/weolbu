package com.bada.weolbu.auth

import com.bada.weolbu.auth.model.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {
    fun findByRefreshToken(refreshToken: String): Optional<RefreshToken>
}
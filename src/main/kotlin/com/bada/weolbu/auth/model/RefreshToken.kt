package com.bada.weolbu.auth.model

import com.bada.weolbu.user.model.User
import jakarta.persistence.*

@Entity
data class RefreshToken(
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,
    private var refreshToken: String,
//    private var reissueCount: Int = 0
) {
    @Id
    val userId: Long? = null

    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun validateRefreshToken(refreshToken: String) = this.refreshToken == refreshToken

}
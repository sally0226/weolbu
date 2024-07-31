package com.bada.weolbu.auth.model

import com.bada.weolbu.user.model.User
import jakarta.persistence.*

@Entity
data class RefreshToken(
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,
    @Id
    val userId: Long? = null,
    private var refreshToken: String,
//    private var reissueCount: Int = 0
) {
    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun validateRefreshToken(refreshToken: String) = this.refreshToken == refreshToken

}
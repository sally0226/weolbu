package com.bada.weolbu.user.model

import com.bada.weolbu.entity.UserRole
import java.time.OffsetDateTime

data class UserResponseDTO(
    val id: Long?,
    val username: String,
    val phoneNumber: String,
    val email: String,
    val type: UserRole,
    val createdDateTime: OffsetDateTime,
    val updateDateTime: OffsetDateTime?
)
package com.bada.weolbu.user.model

import java.time.OffsetDateTime

data class UserResponseDTO (
    val id: Long?,
    val username: String,
    val phoneNumber: String,
    val email: String,
    val type: UserType,
    val createdDateTime: OffsetDateTime,
    val updateDateTime: OffsetDateTime?
)
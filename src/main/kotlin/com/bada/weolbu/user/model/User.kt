package com.bada.weolbu.user.model

import com.bada.weolbu.auth.model.RefreshToken
import jakarta.persistence.*
import java.time.OffsetDateTime

enum class UserType {
    Instructor, Student
}

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String,
    val phoneNumber: String,
    val password: String,
    val email: String,
    @Enumerated(EnumType.STRING)
    val type: UserType,
    val createdDateTime: OffsetDateTime = OffsetDateTime.now(),
    val updateDateTime: OffsetDateTime? = null,
) {
    fun toResponseDTO(): UserResponseDTO {
        return UserResponseDTO(
            id = id,
            username = username,
            phoneNumber = phoneNumber,
            email = email,
            type = type,
            createdDateTime = createdDateTime,
            updateDateTime = updateDateTime,
        )
    }
}

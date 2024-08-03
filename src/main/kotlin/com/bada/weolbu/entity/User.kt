package com.bada.weolbu.entity

import com.bada.weolbu.user.model.UserResponseDTO
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.OffsetDateTime

@Entity
@Table(name = "`user`")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    @Column(unique = true, nullable = false)
    val email: String,
    private val password: String,
    @Enumerated(EnumType.STRING)
    val role: UserRole,
    val createdDateTime: OffsetDateTime = OffsetDateTime.now(),
    val updateDateTime: OffsetDateTime? = null,
    val phoneNumber: String,

    // TODO: cascade type 지정 필요
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val courses: List<UserCourse> = mutableListOf(),
) : UserDetails {
    fun toResponseDTO(): UserResponseDTO {
        return UserResponseDTO(
            id = id,
            username = name,
            phoneNumber = phoneNumber,
            email = email,
            type = role,
            createdDateTime = createdDateTime,
            updateDateTime = updateDateTime,
        )
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        setOf(SimpleGrantedAuthority("ROLE_$role")).toMutableSet()

    override fun getPassword(): String = password

    override fun getUsername(): String = name
}

enum class UserRole {
    Instructor, Student
}

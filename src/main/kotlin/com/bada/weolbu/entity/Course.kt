package com.bada.weolbu.entity

import com.bada.weolbu.course.model.CourseResponseDTO
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    @ManyToOne(fetch = FetchType.LAZY)
    var instructor: User? = null,
    val capacity: Int,
    val currentEnrollment: Int = 0,
    var currentEnrollmentPercent: Float = 0.0f,
    @Enumerated(EnumType.STRING)
    val category: CourseCategory,
    val price: Int,
    val createdDateTime: OffsetDateTime = OffsetDateTime.now(),
    val updateDateTime: OffsetDateTime? = null,

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val members: List<UserCourse> = mutableListOf(),
) {
    fun toResponseDTO(): CourseResponseDTO {
        val dto = CourseResponseDTO(
            id = id,
            title = title,
            capacity = capacity,
            currentEnrollment = currentEnrollment,
            price = price,
            category = category,
            createdDateTime = createdDateTime,
            updateDateTime = updateDateTime
        )
        if (instructor != null) {
            dto.instructor = instructor!!.toResponseDTO()
        }
        return dto
    }
}

enum class CourseCategory() {
    ORIGINAL,
    REAL_ESTATE,
    FINANCE,
    COACHING,
    BOOK_CLUB;
}
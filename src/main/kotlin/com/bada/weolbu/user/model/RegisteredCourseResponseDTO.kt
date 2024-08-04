package com.bada.weolbu.user.model

import com.bada.weolbu.course.model.CourseResponseDTO
import java.time.OffsetDateTime

data class RegisteredCourseResponseDTO(
    val course: CourseResponseDTO,
    val createdDateTime: OffsetDateTime,
    val updatedDateTime: OffsetDateTime? = null
)



package com.bada.weolbu.course.model

import com.bada.weolbu.entity.CourseCategory
import com.bada.weolbu.user.model.UserResponseDTO
import java.time.OffsetDateTime

data class CourseResponseDTO(
    val id: Long?,
    val title: String,
    val capacity: Int,
    val currentEnrollment: Int,
    val category: CourseCategory,
    val price: Int,
    val createdDateTime: OffsetDateTime,
    val updateDateTime: OffsetDateTime?,
) {
    var instructor: UserResponseDTO? = null
}

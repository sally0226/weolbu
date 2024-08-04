package com.bada.weolbu.course.model

import com.bada.weolbu.entity.Course
import com.bada.weolbu.entity.CourseCategory
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateCourseDTO(
    @field:NotBlank(message = "강좌의 제목을 입력해주세요")
    val title: String,

    @field:Min(1, message = "최대수강인원은 최소 1명 이상이어야 합니다.")
    val capacity: Int,

    @field:Min(1, message = "가격은 최소 1원 이상이어야 합니다.")
    val price: Int,

    val category: CourseCategory,
) {
    fun toEntity(): Course {
        return Course(
            title = title,
            capacity = capacity,
            price = price,
            category = category,
        )
    }
}
package com.bada.weolbu.user.model

import jakarta.validation.constraints.Size

data class RegisterCoursesRequestDTO(
    @field:Size(min = 1, max = 50, message = "한 번에 수강신청할 수 있는 강의의 수는 최소 1개 ~ 최대 50개입니다.")
    val courseIds: List<Long>,
) {
}
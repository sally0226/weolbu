package com.bada.weolbu.course.model

data class GetCoursesResponseDTO(
    val courses: List<CourseResponseDTO>, // 강의 리스트
    val totalCount: Int, // 총 강의 개수 (페이지네이션을 위한 정보)
    val pageNo: Int, // 현재 페이지 번호
    val pageSize: Int // 페이지 크기 (한 페이지에 표시되는 강의 개수)
)
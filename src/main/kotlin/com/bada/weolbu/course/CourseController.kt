package com.bada.weolbu.course

import com.bada.weolbu.course.model.CourseResponseDTO
import com.bada.weolbu.course.model.CreateCourseDTO
import com.bada.weolbu.course.model.GetCoursesResponseDTO
import com.bada.weolbu.entity.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController(private val courseService: CourseService) {
    @GetMapping()
    @Operation(
        summary = "모든 강좌 조회",
        description = "정렬 : {컬럼이름},{asc or desc} \n " +
                "- 예시 : \n" +
                "    - 최근등록순: \"createdDateTime,desc\"\n" +
                "    - 신청률높은순: \"currentEnrollmentPercent,desc\"\n" +
                "    - 신청자많은순: \"currentEnrollment,desc\" \n" +
                "default page size : 20",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getCourses(pageable: Pageable): GetCoursesResponseDTO {
        return courseService.getCourses(pageable)
    }

    @GetMapping("/{courseId}")
    @Operation(
        summary = "강좌 단건 조회",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getCourse(@PathVariable courseId: Long): CourseResponseDTO {
        return courseService.getCourse(courseId).toResponseDTO()
    }

    @PostMapping()
    @Operation(
        summary = "강의 생성 (강사만 가능)",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun create(@AuthenticationPrincipal user: User, @Valid @RequestBody body: CreateCourseDTO): CourseResponseDTO {
        return courseService.createCourse(body, user).toResponseDTO()
    }
}
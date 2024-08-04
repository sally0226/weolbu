package com.bada.weolbu.user

import com.bada.weolbu.entity.User
import com.bada.weolbu.user.model.RegisteredCourseResponseDTO
import com.bada.weolbu.user.model.RegisterCoursesRequestDTO
import com.bada.weolbu.user.model.UserResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/courses")
    @Operation(
        summary = "수강신청된 강의 목록",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getCourses(@AuthenticationPrincipal user: User): ResponseEntity<List<RegisteredCourseResponseDTO>> {
        val list = this.userService.getCourses(user)
        val response = list.map { userCourse ->
            RegisteredCourseResponseDTO(
                course = userCourse.course.toResponseDTO(),
                createdDateTime = userCourse.createdDateTime,
                updatedDateTime = userCourse.updateDateTime
            )
        }

        return ResponseEntity.ok(response)
    }

    @PostMapping("/courses")
    @Operation(
        summary = "여러 강의에 대해 수강신청",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun registerCourse(
        @Valid @RequestBody body: RegisterCoursesRequestDTO,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<List<RegisteredCourseResponseDTO>> {
        val list = userService.enrollUser(body.courseIds, user)

        val response = list.map { userCourse ->
            RegisteredCourseResponseDTO(
                course = userCourse.course.toResponseDTO(),
                createdDateTime = userCourse.createdDateTime,
                updatedDateTime = userCourse.updateDateTime
            )
        }

        return ResponseEntity.ok(response)
    }
}
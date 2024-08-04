package com.bada.weolbu.course

import com.bada.weolbu.common.exception.CourseNotFoundException
import com.bada.weolbu.course.model.CourseResponseDTO
import com.bada.weolbu.course.model.CreateCourseDTO
import com.bada.weolbu.course.model.GetCoursesResponseDTO
import com.bada.weolbu.entity.Course
import com.bada.weolbu.entity.CourseRepository
import com.bada.weolbu.entity.User
import com.bada.weolbu.user.model.UserResponseDTO
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class CourseService(private val courseRepository: CourseRepository) {
    fun createCourse(createCourseDTO: CreateCourseDTO, user: User): Course {
        val course = createCourseDTO.toEntity()
        course.instructor = user
        return courseRepository.save(course)
    }

    fun getCourse(courseId: Long): Course {
        val course = courseRepository.findById(courseId)
            .orElseThrow { CourseNotFoundException(courseId) }
        return course
    }

    fun getCourses(pageable: Pageable): GetCoursesResponseDTO {
        // 페이지네이션 쿼리 실행
        val coursePage = courseRepository.findAll(pageable)

        // DTO로 변환
        val courses: List<CourseResponseDTO> = coursePage.content.map { course ->
            var courseResponseDTO = CourseResponseDTO(
                id = course.id,
                title = course.title,
                capacity = course.capacity,
                currentEnrollment = course.currentEnrollment,
                category = course.category,
                price = course.price,
                createdDateTime = course.createdDateTime,
                updateDateTime = course.updateDateTime,
            )
            courseResponseDTO.instructor = courseResponseDTO.instructor?.let {
                UserResponseDTO(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    role = it.role,
                    createdDateTime = it.createdDateTime,
                    updateDateTime = it.updateDateTime,
                    phoneNumber = it.phoneNumber,
                )
            }
            courseResponseDTO
        }

        return GetCoursesResponseDTO(
            courses = courses,
            totalCount = coursePage.totalElements.toInt(),
            pageNo = coursePage.number,
            pageSize = coursePage.size
        )
    }

}
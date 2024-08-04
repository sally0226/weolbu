package com.bada.weolbu.user

import com.bada.weolbu.common.exception.CourseNotFoundException
import com.bada.weolbu.common.exception.DuplicateUserCourseException
import com.bada.weolbu.entity.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class UserService(
    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository,
    private val userCourseRepository: UserCourseRepository
) {
    fun getCourses(user: User): List<UserCourse> {
        return userCourseRepository.findAllByUser(user)
    }

    @Transactional
    fun enrollUser(courseIds: List<Long>, user: User): MutableList<UserCourse> {
        val list = mutableListOf<UserCourse>()

        courseIds.toSet().forEach { courseId ->
            val course = courseRepository.findById(courseId).orElseThrow { CourseNotFoundException(courseId) }
            val currentCount = userCourseRepository.countByCourse(course)
            val maxCapacity = course.capacity

            if (currentCount >= maxCapacity) {
                throw CourseNotFoundException(courseId)
            }

            if (userCourseRepository.existsByUserIdAndCourseId(user.id!!, courseId)) {
                throw DuplicateUserCourseException(courseId)
            }

            val userCourse = UserCourse(user = user, course = course)
            userCourseRepository.save(userCourse)
            list.add(userCourse)
        }
        return list
    }
}
package com.bada.weolbu.entity

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCourseRepository : CrudRepository<UserCourse, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun countByCourse(course: Course): Long
    fun existsByUserIdAndCourseId(userId: Long, courseId: Long): Boolean
    fun findAllByUser(user: User): List<UserCourse>
}
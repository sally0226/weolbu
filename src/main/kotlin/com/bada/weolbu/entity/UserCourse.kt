package com.bada.weolbu.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "course_id"])]
)
data class UserCourse(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    val course: Course,

    val createdDateTime: OffsetDateTime = OffsetDateTime.now(),

    val updateDateTime: OffsetDateTime? = null,
)
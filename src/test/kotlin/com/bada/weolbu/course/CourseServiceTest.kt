package com.bada.weolbu.course

import com.bada.weolbu.common.exception.CourseNotFoundException
import com.bada.weolbu.course.model.CreateCourseDTO
import com.bada.weolbu.entity.Course
import com.bada.weolbu.entity.CourseCategory
import com.bada.weolbu.entity.CourseRepository
import com.bada.weolbu.entity.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CourseServiceTest : DescribeSpec() {
    private val courseRepository: CourseRepository = mockk<CourseRepository>()
    private val courseService: CourseService = CourseService(courseRepository)
    val user = mockk<User>()

    init {
        describe("createCourse") {
            val createCourseDTO = CreateCourseDTO(
                title = "Test Course",
                category = CourseCategory.REAL_ESTATE,
                price = 300000,
                capacity = 30
            )
            context("유효한 DTO가 제공되었을 때") {
                it("과정을 저장하고 반환해야 한다") {
                    val course = createCourseDTO.toEntity()
                    course.instructor = user

                    every { courseRepository.save(any<Course>()) } returns course
                    val result = courseService.createCourse(createCourseDTO, user)

                    result.instructor shouldBe user
                    result.title shouldBe createCourseDTO.title
                    verify(exactly = 1) { courseRepository.save(any<Course>()) }
                }
            }
        }

        describe("getCourse") {
            val courseId = 1L
            val course = Course(
                title = "Test Course",
                category = CourseCategory.REAL_ESTATE,
                price = 300000,
                capacity = 30
            )

            context("해당 ID를 가진 강좌가 존재하면") {
                it("강좌 정보가 반환된다") {
                    every { courseRepository.findById(courseId) } returns java.util.Optional.of(course)
                    val result = courseService.getCourse(courseId)

                    result shouldBe course
                }
            }

            context("존재하지 않는 경우") {
                it("CourseNotFoundException 예외를 던진다") {
                    every { courseRepository.findById(courseId) } returns java.util.Optional.empty()
                    shouldThrow<CourseNotFoundException> {
                        courseService.getCourse(courseId)
                    }
                }
            }
        }
    }
}
package com.bada.weolbu.user

import com.bada.weolbu.entity.*
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class UserServiceTest : DescribeSpec() {
    private val userRepository: UserRepository = mockk<UserRepository>()
    private val courseRepository: CourseRepository = mockk<CourseRepository>()
    private val userCourseRepository: UserCourseRepository = mockk<UserCourseRepository>()

    private val userService = UserService(userRepository, courseRepository, userCourseRepository)

    private val maxCapacity = 5 // Example capacity
    private var course: Course = Course(
        id = 1,
        title = "Test Course",
        capacity = maxCapacity, price = 10,
        category = CourseCategory.REAL_ESTATE
    )
    private var user: User = User(
        id = 1,
        email = "test@example.com",
        name = "Test User",
        phoneNumber = "010-1234-5678",
        role = UserRole.INSTRUCTOR,
        password = ""
    )


    init {
        describe("Enroll multiple users concurrently") {
            context("when multiple users try to enroll at the same time") {
                every { courseRepository.findById(course.id!!) } returns Optional.of(course)
                every { userCourseRepository.existsByUserIdAndCourseId(any<Long>(), course.id!!) } returns false
                every { userCourseRepository.save(any()) } returns mockk()

                it("should not exceed the maximum capacity") {
                    val numberOfThreads = maxCapacity + 1
                    val executor = Executors.newFixedThreadPool(numberOfThreads)
                    val latch = CountDownLatch(numberOfThreads)

                    val count = AtomicInteger(0)

                    repeat(numberOfThreads) {
                        executor.submit {
                            try {
                                every { userCourseRepository.countByCourse(course) } answers {
                                    count.incrementAndGet().toLong() - 1
                                }

                                userService.enrollUser(listOf(course.id!!), user)
                            } catch (e: RuntimeException) {
                                // Handle the exception if necessary
                            } finally {
                                latch.countDown()
                            }
                        }
                    }

                    latch.await(1, TimeUnit.MINUTES)

                    verify(exactly = maxCapacity) { userCourseRepository.save(any<UserCourse>()) }
                }
            }
        }
    }

}
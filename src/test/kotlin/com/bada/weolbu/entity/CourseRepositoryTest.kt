package com.bada.weolbu.entity

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import java.time.OffsetDateTime

@DataJpaTest
@ActiveProfiles("test") // Ensure this points to the application-test.yml
class CourseRepositoryTest(
    @Autowired private val courseRepository: CourseRepository
) : DescribeSpec() {

    init {
        beforeEach {
            courseRepository.deleteAll()
        }

        describe("save") {
            context("유효한 데이터가 주어지면") {
                it("강의가 성공적으로 저장된다") {
                    val course = Course(
                        id = 1L,
                        title = "Test Course",
                        capacity = 10,
                        category = CourseCategory.REAL_ESTATE,
                        price = 100,
                        createdDateTime = OffsetDateTime.now()
                    )

                    val savedCourse = courseRepository.save(course)

                    savedCourse.id shouldNotBe null
                    val foundCourse = courseRepository.findById(savedCourse.id!!).orElse(null)
                    foundCourse shouldNotBe null
                    foundCourse?.title shouldBe course.title
                }
            }
        }

        describe("findAll") {
            context("11개의 강의에 대해") {
                val courses = List(11) { index ->
                    var course = Course(
                        id = (index + 1).toLong(),
                        title = "Course ${index + 1}",
                        capacity = 10 + index,
                        currentEnrollment = index,
                        category = CourseCategory.REAL_ESTATE,
                        price = 100 + (index * 10),
                    )
                    course.currentEnrollmentPercent =
                        course.currentEnrollment.toFloat() / course.currentEnrollment.toFloat() * 100f
                    course
                }
                beforeEach {
                    courseRepository.saveAll(courses)
                }
                context("최근 생성순, 0페이지, 페이지당 5개 조회시") {
                    it("최근 생성순으로 정렬된 5개 강의 목록을 반환한다.") {
                        var pageSize = 5
                        var pageNo = 0
                        val pageable: Pageable =
                            PageRequest.of(pageNo, pageSize, Sort.by("createdDateTime").descending())
                        val pageResult = courseRepository.findAll(pageable)

                        pageResult.content.size shouldBe pageSize
                        pageResult.content.zipWithNext { a, b ->
                            a.createdDateTime shouldBeGreaterThanOrEqualTo b.createdDateTime
                        }
                    }
                }
                context("최근 생성순, 2페이지, 페이지당 5개 조회시") {
                    it("제일 먼저 생성된 1개의 강의목록을 반환한다.") {
                        var pageSize = 5
                        var pageNo = 2

                        val pageable: Pageable =
                            PageRequest.of(pageNo, pageSize, Sort.by("createdDateTime").descending())
                        val pageResult = courseRepository.findAll(pageable)

                        pageResult.content.size shouldBe 1
                        val oldestCourse = courses.sortedBy { it.createdDateTime }[0]
                        pageResult.content[0].createdDateTime shouldBe oldestCourse.createdDateTime
                    }
                }
                context("신청률 높은순, 0페이지, 페이지당 20개 조회시") {
                    it("신청률 높은 순서로 정렬된 11개의 강의 목록을 반환한다.") {
                        val pageSize = 20
                        val pageNo = 0
                        val pageable: Pageable =
                            PageRequest.of(pageNo, pageSize, Sort.by("currentEnrollmentPercentage").descending())
                        val pageResult = courseRepository.findAll(pageable)

                        pageResult.content.size shouldBe courses.size
                        pageResult.content.zipWithNext { a, b ->
                            a.currentEnrollmentPercent shouldBeGreaterThanOrEqualTo b.currentEnrollmentPercent
                        }
                    }
                }
                context("신청자 많은순, 0페이지, 페이지당 20개 조회시") {
                    it("신청자 많은 순서로 정렬된 11개의 강의 목록을 반환한다.") {
                        val pageSize = 20
                        val pageNo = 0
                        val pageable: Pageable =
                            PageRequest.of(pageNo, pageSize, Sort.by("currentEnrollment").descending())
                        val pageResult = courseRepository.findAll(pageable)

                        pageResult.content.size shouldBe courses.size
                        pageResult.content.zipWithNext { a, b ->
                            a.currentEnrollment shouldBeGreaterThanOrEqualTo b.currentEnrollment
                        }
                    }
                }
            }

        }
    }

}
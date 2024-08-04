package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class DuplicateUserCourseException(courseId: Long) :
    ResponseStatusException(HttpStatus.CONFLICT, "$courseId 는 이미 수강 신청이 완료된 강좌입니다.")
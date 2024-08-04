package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CourseFullException(courseId: Long) :
    ResponseStatusException(HttpStatus.BAD_REQUEST, "$courseId 강좌의 수강 인원이 초과되어 수강신청이 불가합니다.")
package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CourseNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "$id 강좌를 찾을 수 없습니다.")
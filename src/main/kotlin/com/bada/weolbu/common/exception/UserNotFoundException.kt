package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserNotFoundException(email: String) : ResponseStatusException(HttpStatus.NOT_FOUND, "$email 유저를 찾을 수 없습니다.")
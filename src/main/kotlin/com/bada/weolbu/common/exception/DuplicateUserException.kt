package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class DuplicateUserException(email: String) :
    ResponseStatusException(HttpStatus.CONFLICT, "$email 은 이미 사용중인 이메일입니다.")
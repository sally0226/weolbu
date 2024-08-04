package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InvalidPasswordException() :
    ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "비밀번호는 영문 대문자, 영문 소문자, 숫자 중 2개 이상의 조합으로 이루어져야 합니다."
    )
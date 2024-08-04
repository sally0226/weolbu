package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidRefreshTokenException() : ResponseStatusException(
    HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."
)
package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidRefreshTokenException() : RuntimeException("Invalid RefreshToken") {}
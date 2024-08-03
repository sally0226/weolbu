package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class InvalidPasswordException() :
    ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Password must contain at least two of the following: lowercase letters, uppercase letters, or digits."
    )
package com.bada.weolbu.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class DuplicateUserException(email: String) :
    ResponseStatusException(HttpStatus.CONFLICT, "User with email $email already exists.")
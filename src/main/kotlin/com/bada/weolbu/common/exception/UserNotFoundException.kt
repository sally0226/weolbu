package com.bada.weolbu.common.exception

class UserNotFoundException(id: Long?=0) :RuntimeException("$id User not found")
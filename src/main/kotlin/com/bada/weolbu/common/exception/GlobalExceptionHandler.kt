package com.bada.weolbu.common.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<ErrorResponse> {
        val status = ex.statusCode
        return ResponseEntity(
            ErrorResponse(status.value(), ex.reason ?: "An unexpected error occurred"),
            status
        )
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errors: MutableMap<String, String?> = HashMap()
        for (error in ex.bindingResult.allErrors) {
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity(
            ErrorResponse(status.value(), ex.message ?: "An unexpected error occurred"),
            status
        )
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        logger.error("[HttpMessageNotReadableException] ${ex.message}")
        val errorMessage = when (val cause = ex.cause) {
            is MismatchedInputException -> "${cause.path.joinToString() { it.fieldName }} is null"
            else -> "유효하지 않은 요청입니다"
        }
        return ResponseEntity(
            ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage),
            HttpStatus.BAD_REQUEST
        )
    }

    data class ErrorResponse(val status: Int, val message: String)
}
package com.vibecheck.vibecheck_backend.exception

import com.google.firebase.auth.FirebaseAuthException
import com.vibecheck.vibecheck_backend.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), ex.message ?: "Unauthorized"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Terjadi kesalahan pada server"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errorMessage = ex.bindingResult.allErrors.firstOrNull()?.defaultMessage ?: "Data tidak valid"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage))
    }

    // Menangkap error dari Firebase (misal: Email sudah terdaftar)
    @ExceptionHandler(FirebaseAuthException::class)
    fun handleFirebaseAuthException(ex: FirebaseAuthException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Firebase Error: ${ex.message}"))
    }
}
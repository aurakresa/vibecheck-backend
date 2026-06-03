package com.vibecheck.vibecheck_backend.dto

import java.time.LocalDateTime

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null,
    val timestamp: String = LocalDateTime.now().toString()
) {
    companion object {
        fun <T> success(data: T?, message: String = "Success"): ApiResponse<T> {
            return ApiResponse(200, message, data)
        }

        fun <T> error(status: Int, message: String): ApiResponse<T> {
            return ApiResponse(status, message, null)
        }
    }
}
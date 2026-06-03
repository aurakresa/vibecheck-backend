package com.vibecheck.vibecheck_backend.dto


import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Username tidak boleh kosong")
    val username: String,

    @field:Email(message = "Format email tidak valid")
    @field:NotBlank(message = "Email tidak boleh kosong")
    val email: String,

    @field:Size(min = 6, message = "Password minimal 6 karakter") // Aturan bawaan Firebase
    val password: String
)

data class UpdateUsernameRequest(
    @field:NotBlank(message = "Username baru tidak boleh kosong")
    val newUsername: String
)

data class UpdatePasswordRequest(
    @field:Size(min = 6, message = "Password minimal 6 karakter")
    val newPassword: String
)
package com.vibecheck.vibecheck_backend.controller

import com.vibecheck.vibecheck_backend.dto.ApiResponse
import com.vibecheck.vibecheck_backend.dto.RegisterRequest
import com.vibecheck.vibecheck_backend.dto.UpdatePasswordRequest
import com.vibecheck.vibecheck_backend.dto.UpdateUsernameRequest
import com.vibecheck.vibecheck_backend.dto.UserProfileDto
import com.vibecheck.vibecheck_backend.service.AuthService
import jakarta.validation.Valid

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    // POST /api/v1/auth/register -> Ini endpoint PUBLIC (tidak butuh token)
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<UserProfileDto>> {
        val newUser = authService.registerUser(request)
        return ResponseEntity.ok(ApiResponse.success(newUser, "Registrasi berhasil! Silakan login di aplikasi."))
    }

    // GET /api/v1/auth/profile -> Butuh Token Bearer
    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<ApiResponse<UserProfileDto>> {
        val uid = getUidFromContext()
        val userProfile = authService.getUserProfile(uid)
        return ResponseEntity.ok(ApiResponse.success(userProfile, "Berhasil mengambil profil"))
    }

    // PUT /api/v1/auth/username -> Butuh Token Bearer
    @PutMapping("/username")
    fun updateUsername(@Valid @RequestBody request: UpdateUsernameRequest): ResponseEntity<ApiResponse<UserProfileDto>> {
        val uid = getUidFromContext()
        val updatedProfile = authService.updateUsername(uid, request.newUsername)
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Username berhasil diubah"))
    }

    // PUT /api/v1/auth/password -> Butuh Token Bearer
    @PutMapping("/password")
    fun updatePassword(@Valid @RequestBody request: UpdatePasswordRequest): ResponseEntity<ApiResponse<Nothing>> {
        val uid = getUidFromContext()
        authService.updatePassword(uid, request.newPassword)
        // Jika hanya mengubah data tanpa perlu mengembalikan objek, data diisi null
        return ResponseEntity.ok(ApiResponse.success(null, "Password berhasil diubah"))
    }

    // Helper untuk mengambil UID dari SecurityContext (yang diset oleh FirebaseTokenFilter)
    private fun getUidFromContext(): String {
        return SecurityContextHolder.getContext().authentication?.principal as String
    }
}
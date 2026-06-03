package com.vibecheck.vibecheck_backend.service


import com.vibecheck.vibecheck_backend.dto.RegisterRequest
import com.vibecheck.vibecheck_backend.dto.UserProfileDto

interface AuthService {
    fun registerUser(request: RegisterRequest): UserProfileDto
    fun getUserProfile(firebaseUid: String): UserProfileDto
    fun updateUsername(firebaseUid: String, newUsername: String): UserProfileDto
    fun updatePassword(firebaseUid: String, newPassword: String)
}
package com.vibecheck.vibecheck_backend.dto

data class UserProfileDto(
    val firebaseUid: String,
    val email: String?, // Kasih tanda tanya (?) karena email Firebase kadang bisa null
    val username: String?, // <-- Ini yang bikin error tadi
    val isPremium: Boolean,
    val role: String
)
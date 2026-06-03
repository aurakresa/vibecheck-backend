package com.vibecheck.vibecheck_backend.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import com.vibecheck.vibecheck_backend.dto.RegisterRequest
import com.vibecheck.vibecheck_backend.dto.UserProfileDto
import com.vibecheck.vibecheck_backend.exception.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl : AuthService {

    override fun registerUser(request: RegisterRequest): UserProfileDto {
        // 1. Siapkan data untuk Firebase
        val createRequest = UserRecord.CreateRequest()
            .setEmail(request.email)
            .setPassword(request.password)
            .setDisplayName(request.username)

        // 2. Buat user di Firebase
        val userRecord = FirebaseAuth.getInstance().createUser(createRequest)

        // TODO: Jika kamu pakai database PostgreSQL, simpan data user ke DB di sini pakai userRecord.uid

        return UserProfileDto(
            firebaseUid = userRecord.uid,
            email = userRecord.email,
            username = userRecord.displayName,
            isPremium = false,
            role = "GUEST"
        )
    }

    override fun getUserProfile(firebaseUid: String): UserProfileDto {
        val userRecord = fetchFirebaseUser(firebaseUid)
        return UserProfileDto(
            firebaseUid = userRecord.uid,
            email = userRecord.email,
            username = userRecord.displayName ?: "PLAYER_UNKNOWN",
            isPremium = true,
            role = "HOST_PREMIUM"
        )
    }

    override fun updateUsername(firebaseUid: String, newUsername: String): UserProfileDto {
        // 1. Update ke Firebase
        val updateRequest = UserRecord.UpdateRequest(firebaseUid)
            .setDisplayName(newUsername)
        val userRecord = FirebaseAuth.getInstance().updateUser(updateRequest)

        // TODO: Update juga ke tabel PostgreSQL kamu jika ada

        return UserProfileDto(
            firebaseUid = userRecord.uid,
            email = userRecord.email,
            username = userRecord.displayName,
            isPremium = true,
            role = "HOST_PREMIUM"
        )
    }

    override fun updatePassword(firebaseUid: String, newPassword: String) {
        val updateRequest = UserRecord.UpdateRequest(firebaseUid)
            .setPassword(newPassword)

        // Update password di Firebase Auth
        FirebaseAuth.getInstance().updateUser(updateRequest)
    }

    // Helper method
    private fun fetchFirebaseUser(uid: String): UserRecord {
        try {
            return FirebaseAuth.getInstance().getUser(uid)
        } catch (e: Exception) {
            throw UserNotFoundException("Data user tidak ditemukan di sistem")
        }
    }
}
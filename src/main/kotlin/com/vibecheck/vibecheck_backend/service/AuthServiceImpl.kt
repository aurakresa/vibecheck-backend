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
        val createRequest = UserRecord.CreateRequest()
            .setEmail(request.email)
            .setPassword(request.password)
            .setDisplayName(request.username)

        val userRecord = FirebaseAuth.getInstance().createUser(createRequest)

        return UserProfileDto(
            firebaseUid = userRecord.uid,
            email = userRecord.email,
            username = userRecord.displayName,
            isPremium = false,
            role = "GUEST",
            photoUrl = userRecord.photoUrl
        )
    }

    override fun getUserProfile(firebaseUid: String): UserProfileDto {
        val userRecord = fetchFirebaseUser(firebaseUid)
        return UserProfileDto(
            firebaseUid = userRecord.uid,
            email = userRecord.email,
            username = userRecord.displayName ?: "PLAYER_UNKNOWN",
            isPremium = true,
            role = "HOST_PREMIUM",
            photoUrl = userRecord.photoUrl
        )
    }

    override fun updateUsername(firebaseUid: String, newUsername: String): UserProfileDto {
        val updateRequest = UserRecord.UpdateRequest(firebaseUid)
            .setDisplayName(newUsername)
        val userRecord = FirebaseAuth.getInstance().updateUser(updateRequest)

        return UserProfileDto(
            firebaseUid = userRecord.uid,
            email = userRecord.email,
            username = userRecord.displayName,
            isPremium = true,
            role = "HOST_PREMIUM",
            photoUrl = userRecord.photoUrl
        )
    }

    override fun updatePassword(firebaseUid: String, newPassword: String) {
        val updateRequest = UserRecord.UpdateRequest(firebaseUid)
            .setPassword(newPassword)

        FirebaseAuth.getInstance().updateUser(updateRequest)
    }

    override fun updateProfilePhoto(firebaseUid: String, photoUrl: String): UserProfileDto {
        val updateRequest = UserRecord.UpdateRequest(firebaseUid)
            .setPhotoUrl(photoUrl)

        val userRecord = FirebaseAuth.getInstance().updateUser(updateRequest)

        return UserProfileDto(
            firebaseUid = userRecord.uid,
            email = userRecord.email,
            username = userRecord.displayName,
            isPremium = true,
            role = "HOST_PREMIUM",
            photoUrl = userRecord.photoUrl
        )
    }

    private fun fetchFirebaseUser(uid: String): UserRecord {
        try {
            return FirebaseAuth.getInstance().getUser(uid)
        } catch (e: Exception) {
            throw UserNotFoundException("Data user tidak ditemukan di sistem")
        }
    }
}
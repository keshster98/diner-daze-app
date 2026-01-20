package com.keshen.dinerdazeapp.service

import com.google.firebase.firestore.FirebaseFirestore
import com.keshen.dinerdazeapp.data.model.User
import kotlinx.coroutines.tasks.await

class UserProfileService(
    private val db: FirebaseFirestore
) {
    // Get the profile of the current signed in user
    suspend fun getProfile(uid: String): User {
        val doc = db.collection("users")
            .document(uid)
            .get()
            .await()

        if (!doc.exists()) {
            throw IllegalStateException("Profile not found")
        }

        return doc.toObject(User::class.java)
            ?: throw IllegalStateException("Failed to parse profile")
    }

    // Get the first name of the currently signed in user (returns email as a failsafe)
    suspend fun getUserFirstName(uid: String, email: String): String {
        val doc = db.collection("users").document(uid).get().await()
        val firstName = doc.getString("firstName")
        return firstName?.takeIf { it.isNotBlank() } ?: email
    }

    // Check if the profile of the current signed in user has been completely filled
    suspend fun isProfileCompleted(uid: String): Boolean {
        val doc = db.collection("users").document(uid).get().await()
        return doc.exists() && doc.getBoolean("completed") == true
    }
}
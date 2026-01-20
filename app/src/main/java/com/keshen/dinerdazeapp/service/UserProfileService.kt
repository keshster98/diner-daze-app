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
}
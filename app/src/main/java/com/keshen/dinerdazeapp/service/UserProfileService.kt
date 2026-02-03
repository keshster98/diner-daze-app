package com.keshen.dinerdazeapp.service

import com.google.firebase.firestore.FirebaseFirestore
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Gender
import com.keshen.dinerdazeapp.data.model.Role
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.data.model.User
import kotlinx.coroutines.tasks.await

class UserProfileService(
    private val db: FirebaseFirestore
) {
    // Get all users
    suspend fun getAllUsers(): List<User> {
        return db.collection("users")
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(User::class.java)?.copy(uid = doc.id)
            }
    }

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
    suspend fun isProfileFilled(uid: String): Boolean {
        val doc = db.collection("users").document(uid).get().await()
        return doc.exists() && doc.getBoolean("profileFilled") == true
    }

    // Save the profile of the current user after they've submitted their registration form
    suspend fun saveProfile(profile: User) {
        db.collection("users")
            .document(profile.uid)
            .set(profile)
            .await()
    }

    suspend fun isAdmin(uid: String): Boolean {
        val doc = db.collection("users")
            .document(uid)
            .get()
            .await()

        val role = doc.getString("role") ?: return false
        return role == Role.ADMIN.name
    }


    // Update the profile of the current user
    suspend fun updateProfile(
        uid: String,
        firstName: String,
        lastName: String,
        gender: Gender,
        phone: String,
        diet: Diet,
        spiciness: Spiciness
    ) {
        db.collection("users")
            .document(uid)
            .update(
                mapOf(
                    "updatedAt" to System.currentTimeMillis(),
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "gender" to gender.name,
                    "phone" to phone,
                    "diet" to diet.name,
                    "spiciness" to spiciness.name,
                )
            )
            .await()
    }
}
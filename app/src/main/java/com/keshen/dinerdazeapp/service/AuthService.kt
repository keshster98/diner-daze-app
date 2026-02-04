package com.keshen.dinerdazeapp.service

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class AuthService(
    private val auth: FirebaseAuth
) {
    private val _uidFlow = MutableStateFlow(auth.currentUser?.uid)
    val uidFlow =  _uidFlow.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _uidFlow.value = firebaseAuth.currentUser?.uid
        }
    }

    // Registers a new user
    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    // Signs in an existing user
    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    // Checks if the user is logged in
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Gets the current user's UID
    fun uid(): String {
        return auth.currentUser!!.uid
    }

    // Gets the current user's email
    fun email(): String {
        return auth.currentUser!!.email!!
    }

    // Signs out the current user
    fun signOut() {
        auth.signOut()
    }
}
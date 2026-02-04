package com.keshen.dinerdazeapp.service

import com.google.firebase.firestore.FirebaseFirestore
import com.keshen.dinerdazeapp.data.model.Post
import com.keshen.dinerdazeapp.data.model.PostTag
import kotlinx.coroutines.tasks.await

class PostService(
    private val db: FirebaseFirestore
) {

    /* ---------------- GET ---------------- */

    suspend fun getAllPosts(): List<Post> {
        return db.collection("posts")
            .orderBy("createdAt")
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Post::class.java)?.copy(uid = doc.id)
            }
    }

    suspend fun getPost(uid: String): Post {
        val doc = db.collection("posts")
            .document(uid)
            .get()
            .await()

        if (!doc.exists()) {
            throw IllegalStateException("Post not found")
        }

        return doc.toObject(Post::class.java)
            ?.copy(uid = doc.id)
            ?: throw IllegalStateException("Failed to parse post")
    }

    /* ---------------- CREATE ---------------- */

    suspend fun savePost(post: Post) {
        val docRef = db.collection("posts").document()

        val postWithId = post.copy(
            uid = docRef.id,
            createdAt = System.currentTimeMillis()
        )

        docRef.set(postWithId).await()
    }

    /* ---------------- UPDATE ---------------- */

    suspend fun updatePost(
        uid: String,
        title: String,
        description: String,
        tag: PostTag
    ) {
        if (uid.isBlank()) {
            throw IllegalArgumentException("Post ID cannot be blank")
        }

        db.collection("posts")
            .document(uid)
            .update(
                mapOf(
                    "title" to title,
                    "description" to description,
                    "tag" to tag.name,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .await()
    }

    /* ---------------- DELETE ---------------- */

    suspend fun deletePost(uid: String) {
        if (uid.isBlank()) {
            throw IllegalArgumentException("Post ID cannot be blank")
        }

        db.collection("posts")
            .document(uid)
            .delete()
            .await()
    }
}

package com.keshen.dinerdazeapp.service

import com.google.firebase.firestore.FirebaseFirestore
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Menu
import com.keshen.dinerdazeapp.data.model.MenuCategory
import com.keshen.dinerdazeapp.data.model.Spiciness
import kotlinx.coroutines.tasks.await

class MenuService(
    private val db: FirebaseFirestore
) {

    /* ---------------- GET ---------------- */

    suspend fun getAllMenu(): List<Menu> {
        return db.collection("menu")
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Menu::class.java)?.copy(uid = doc.id)
            }
    }

    suspend fun getMenu(uid: String): Menu {
        val doc = db.collection("menu")
            .document(uid)
            .get()
            .await()

        if (!doc.exists()) {
            throw IllegalStateException("Menu item not found")
        }

        return doc.toObject(Menu::class.java)
            ?.copy(uid = doc.id)
            ?: throw IllegalStateException("Failed to parse menu item")
    }

    /* ---------------- CREATE ---------------- */

    suspend fun saveMenu(menu: Menu) {
        val docRef = db.collection("menu").document()

        val menuWithId = menu.copy(
            uid = docRef.id
        )

        docRef.set(menuWithId).await()
    }

    /* ---------------- UPDATE ---------------- */

    suspend fun updateMenu(
        uid: String,
        name: String,
        description: String,
        price: Double,
        category: MenuCategory,
        diet: Diet,
        spiciness: Spiciness,
        isAvailable: Boolean
    ) {
        if (uid.isBlank()) {
            throw IllegalArgumentException("Menu ID cannot be blank")
        }

        db.collection("menu")
            .document(uid)
            .update(
                mapOf(
                    "name" to name,
                    "description" to description,
                    "price" to price,
                    "category" to category.name,
                    "diet" to diet.name,
                    "spiciness" to spiciness.name,
                    "isAvailable" to isAvailable,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .await()
    }

    /* ---------------- DELETE ---------------- */

    suspend fun deleteMenu(uid: String) {
        if (uid.isBlank()) {
            throw IllegalArgumentException("Menu ID cannot be blank")
        }

        db.collection("menu")
            .document(uid)
            .delete()
            .await()
    }
}

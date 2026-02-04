package com.keshen.dinerdazeapp.core.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.pendingDeletionDataStore by preferencesDataStore("pending_deletion_store")

@Singleton
class PendingDeletionStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val KEY_PENDING = booleanPreferencesKey("pending_delete")
    private val KEY_EMAIL = stringPreferencesKey("pending_delete_email")
    private val KEY_REQUESTED_AT = longPreferencesKey("pending_delete_requested_at")

    val isPending: Flow<Boolean> =
        context.pendingDeletionDataStore.data.map { it[KEY_PENDING] ?: false }

    val email: Flow<String?> =
        context.pendingDeletionDataStore.data.map { it[KEY_EMAIL] }

    val requestedAt: Flow<Long?> =
        context.pendingDeletionDataStore.data.map { it[KEY_REQUESTED_AT] }

    suspend fun setPending(email: String, requestedAt: Long) {
        context.pendingDeletionDataStore.edit {
            it[KEY_PENDING] = true
            it[KEY_EMAIL] = email
            it[KEY_REQUESTED_AT] = requestedAt
        }
    }

    suspend fun clear() {
        context.pendingDeletionDataStore.edit {
            it[KEY_PENDING] = false
            it.remove(KEY_EMAIL)
            it.remove(KEY_REQUESTED_AT)
        }
    }
}

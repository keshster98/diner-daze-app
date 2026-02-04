package com.keshen.dinerdazeapp.core.di

import com.keshen.dinerdazeapp.core.store.PendingDeletionStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PendingDeletionEntryPoint {
    fun pendingDeletionStore(): PendingDeletionStore
}

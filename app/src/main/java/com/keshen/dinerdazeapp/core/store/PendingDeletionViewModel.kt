package com.keshen.dinerdazeapp.core.store

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PendingDeletionViewModel @Inject constructor(
    val store: PendingDeletionStore
): ViewModel()

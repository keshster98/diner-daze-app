package com.keshen.dinerdazeapp.core.di

import com.google.firebase.auth.FirebaseAuth
import com.keshen.dinerdazeapp.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthService(
        auth: FirebaseAuth
    ): AuthService =
        AuthService(auth)
}
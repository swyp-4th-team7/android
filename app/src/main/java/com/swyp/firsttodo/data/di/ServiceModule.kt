package com.swyp.firsttodo.data.di

import com.swyp.firsttodo.data.remote.service.AuthService
import com.swyp.firsttodo.data.remote.service.NotificationService
import com.swyp.firsttodo.data.remote.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAuthService(
        @WithoutToken retrofit: Retrofit,
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
        @WithToken retrofit: Retrofit,
    ): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideNotificationService(
        @WithToken retrofit: Retrofit,
    ): NotificationService = retrofit.create(NotificationService::class.java)
}

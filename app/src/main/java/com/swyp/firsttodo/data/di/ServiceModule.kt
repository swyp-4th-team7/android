package com.swyp.firsttodo.data.di

import com.swyp.firsttodo.data.remote.service.AuthService
import com.swyp.firsttodo.data.remote.service.FamilyService
import com.swyp.firsttodo.data.remote.service.HabitService
import com.swyp.firsttodo.data.remote.service.NotificationService
import com.swyp.firsttodo.data.remote.service.ScheduleService
import com.swyp.firsttodo.data.remote.service.StickerService
import com.swyp.firsttodo.data.remote.service.TodoService
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
    fun providesAuthService(
        @WithoutToken retrofit: Retrofit,
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesUserService(
        @WithToken retrofit: Retrofit,
    ): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providesNotificationService(
        @WithToken retrofit: Retrofit,
    ): NotificationService = retrofit.create(NotificationService::class.java)

    @Provides
    @Singleton
    fun providesTodoService(
        @WithToken retrofit: Retrofit,
    ): TodoService = retrofit.create(TodoService::class.java)

    @Provides
    @Singleton
    fun providesFamilyService(
        @WithToken retrofit: Retrofit,
    ): FamilyService = retrofit.create(FamilyService::class.java)

    @Provides
    @Singleton
    fun providesScheduleService(
        @WithToken retrofit: Retrofit,
    ): ScheduleService = retrofit.create(ScheduleService::class.java)

    @Provides
    @Singleton
    fun providesStickerService(
        @WithToken retrofit: Retrofit,
    ): StickerService = retrofit.create(StickerService::class.java)

    @Provides
    @Singleton
    fun providesHabitService(
        @WithToken retrofit: Retrofit,
    ): HabitService = retrofit.create(HabitService::class.java)
}

package com.swyp.firsttodo.data.di

import com.swyp.firsttodo.data.repositoryimpl.AuthRepositoryImpl
import com.swyp.firsttodo.data.repositoryimpl.FamilyRepositoryImpl
import com.swyp.firsttodo.data.repositoryimpl.NotificationRepositoryImpl
import com.swyp.firsttodo.data.repositoryimpl.ScheduleRepositoryImpl
import com.swyp.firsttodo.data.repositoryimpl.TodoRepositoryImpl
import com.swyp.firsttodo.data.repositoryimpl.UserRepositoryImpl
import com.swyp.firsttodo.domain.repository.AuthRepository
import com.swyp.firsttodo.domain.repository.FamilyRepository
import com.swyp.firsttodo.domain.repository.NotificationRepository
import com.swyp.firsttodo.domain.repository.ScheduleRepository
import com.swyp.firsttodo.domain.repository.TodoRepository
import com.swyp.firsttodo.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl,
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindsTodoRepository(todoRepositoryImpl: TodoRepositoryImpl): TodoRepository

    @Binds
    @Singleton
    abstract fun bindsFamilyRepository(familyRepositoryImpl: FamilyRepositoryImpl): FamilyRepository

    @Binds
    @Singleton
    abstract fun bindsScheduleRepository(scheduleRepositoryImpl: ScheduleRepositoryImpl): ScheduleRepository
}

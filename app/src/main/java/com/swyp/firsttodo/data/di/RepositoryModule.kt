package com.swyp.firsttodo.data.di

import com.swyp.firsttodo.data.repository.AuthRepository
import com.swyp.firsttodo.data.repository.UserRepository
import com.swyp.firsttodo.data.repositoryimpl.AuthRepositoryImpl
import com.swyp.firsttodo.data.repositoryimpl.UserRepositoryImpl
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
    abstract fun bidnsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}

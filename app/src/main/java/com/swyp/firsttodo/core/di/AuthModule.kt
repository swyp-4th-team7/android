package com.swyp.firsttodo.core.di

import com.swyp.firsttodo.core.auth.datasource.SessionDataSource
import com.swyp.firsttodo.core.auth.datasource.SessionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindsSessionDataSource(sessionDataSourceImpl: SessionDataSourceImpl): SessionDataSource
}

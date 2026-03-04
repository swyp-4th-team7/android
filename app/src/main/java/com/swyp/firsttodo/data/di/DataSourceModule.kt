package com.swyp.firsttodo.data.di

import com.swyp.firsttodo.data.remote.datasource.AuthDataSource
import com.swyp.firsttodo.data.remote.datasource.NotificationDataSource
import com.swyp.firsttodo.data.remote.datasource.UserDataSource
import com.swyp.firsttodo.data.remote.datasourceimpl.AuthDataSourceImpl
import com.swyp.firsttodo.data.remote.datasourceimpl.NotificationDataSourceImpl
import com.swyp.firsttodo.data.remote.datasourceimpl.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindsUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    @Singleton
    abstract fun bindsNotificationDataSource(
        notificationDataSourceImpl: NotificationDataSourceImpl,
    ): NotificationDataSource
}

package com.swyp.firsttodo.data.di

import com.swyp.firsttodo.data.remote.datasource.AuthDataSource
import com.swyp.firsttodo.data.remote.datasource.FamilyDataSource
import com.swyp.firsttodo.data.remote.datasource.NotificationDataSource
import com.swyp.firsttodo.data.remote.datasource.ScheduleDataSource
import com.swyp.firsttodo.data.remote.datasource.StickerDataSource
import com.swyp.firsttodo.data.remote.datasource.TodoDataSource
import com.swyp.firsttodo.data.remote.datasource.UserDataSource
import com.swyp.firsttodo.data.remote.datasourceimpl.AuthDataSourceImpl
import com.swyp.firsttodo.data.remote.datasourceimpl.FamilyDataSourceImpl
import com.swyp.firsttodo.data.remote.datasourceimpl.NotificationDataSourceImpl
import com.swyp.firsttodo.data.remote.datasourceimpl.ScheduleDataSourceImpl
import com.swyp.firsttodo.data.remote.datasourceimpl.StickerDataSourceImpl
import com.swyp.firsttodo.data.remote.datasourceimpl.TodoDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindsTodoDataSource(todoDataSourceImpl: TodoDataSourceImpl): TodoDataSource

    @Binds
    @Singleton
    abstract fun bindsFamilyDataSource(familyDataSourceImpl: FamilyDataSourceImpl): FamilyDataSource

    @Binds
    @Singleton
    abstract fun bindsScheduleDataSource(scheduleDataSourceImpl: ScheduleDataSourceImpl): ScheduleDataSource

    @Binds
    @Singleton
    abstract fun bindsStickerDataSource(stickerDataSourceImpl: StickerDataSourceImpl): StickerDataSource
}

package com.swyp.firsttodo

import android.app.Application
import com.swyp.firsttodo.core.notification.PushNotificationService
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimber()
        PushNotificationService.createChannels(this)
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

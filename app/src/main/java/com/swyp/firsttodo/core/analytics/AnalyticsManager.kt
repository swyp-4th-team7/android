package com.swyp.firsttodo.core.analytics

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.AutocaptureOption
import com.amplitude.android.Configuration
import com.swyp.firsttodo.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) {
        private val amplitude = Amplitude(
            Configuration(
                apiKey = BuildConfig.AMPLITUDE_KEY,
                context = context,
                autocapture = setOf(
                    AutocaptureOption.SESSIONS,
                    AutocaptureOption.APP_LIFECYCLES,
                ),
            ),
        )

        fun track(event: AnalyticsEvent) {
            val props = event.properties?.toMutableMap() ?: mutableMapOf()
            event.screenName?.let { props["screen_name"] = it }

            amplitude.track(event.name, props)
        }
    }

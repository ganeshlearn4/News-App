package com.newsapp.di

import android.app.Application
import com.newsapp.data.prefs.PrefsHelper
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.internal.modules.ApplicationContextModule

@HiltAndroidApp
class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DaggerNewsApp_HiltComponents_SingletonC.builder().applicationContextModule(
            ApplicationContextModule(this)
        ).build()
        PrefsHelper.init(this)
    }
}
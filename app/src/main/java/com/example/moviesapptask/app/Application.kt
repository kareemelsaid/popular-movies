package com.example.moviesapptask.app

import android.app.Application
import android.content.Context
import com.example.moviesapptask.di.ApplicationComponent
import com.example.moviesapptask.di.ApplicationModule
import com.example.moviesapptask.di.DaggerApplicationComponent
import com.example.moviesapptask.utilities.LocaleHelper
import java.util.*


class UserApplication : Application() {

    lateinit var component: ApplicationComponent
        private set
    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLanguageContext(newBase, Locale("en")))
    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocaleHelper.applyLanguageContext(context, Locale("en"))
    }

}
package com.example.moviesapptask.utilities

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.provider.MediaStore
import java.util.*


object LocaleHelper{
    fun applyLanguageContext(context: Context, locale: Locale?): Context {
        if (locale == null) return context
        if (locale == getLocale(context.resources.configuration)) return context

        return try {
            setupLocale(locale)
            val resources = context.resources
            val configuration = getOverridingConfig(locale, resources)
            updateResources(context, resources, configuration)
            context.createConfigurationContext(configuration)
        } catch (exception: Exception) {
            context
        }
    }

    private fun updateResources(
        context: Context,
        resources: Resources,
        config: Configuration
    ) {
        resources.updateConfiguration(config, resources.displayMetrics)
        if (context.applicationContext !== context) {
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }

    private fun setupLocale(locale: Locale) {
        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.setDefault(LocaleList(locale))
        }
    }

    private fun getOverridingConfig(locale: Locale, resources: Resources): Configuration {
        val configuration = resources.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
        } else {
            configuration.locale = locale
        }
        return configuration
    }

    private fun getLocale(configuration: Configuration): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            configuration.locale
        }
    }
    fun getRealPathFromURI(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)!!
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val str = cursor.getString(idx)
        cursor.close()
        return str
    }
}
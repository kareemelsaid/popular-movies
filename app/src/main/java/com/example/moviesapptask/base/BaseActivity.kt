package com.example.moviesapptask.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapptask.app.UserApplication
import com.example.moviesapptask.di.ApplicationComponent
import com.example.moviesapptask.utilities.LocaleHelper
import com.example.moviesapptask.utilities.builders.LoadingDialog
import java.util.*
import javax.inject.Inject


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    val component: ApplicationComponent
        get() = (application as UserApplication).component

    val loadingDialog by lazy {
        val dialog = LoadingDialog(this).build()
        dialog
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appResources: Resources

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.applyLanguageContext(
                newBase,
                Locale("en")
            )
        )
    }

    override fun getBaseContext(): Context {
        return LocaleHelper.applyLanguageContext(super.getBaseContext(), Locale("en"))
    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocaleHelper.applyLanguageContext(context, Locale("en"))
    }


}
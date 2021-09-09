package com.example.moviesapptask.di;

import com.example.moviesapptask.ui.activity.MoviesActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ApplicationModule::class,
        RepositoriesModule::class,
        ViewModelModule::class,
    ]
)
@Singleton
interface ApplicationComponent {
    fun inject(target: MoviesActivity)
}
package com.example.moviesapptask.di;

import android.content.Context
import android.content.res.Resources
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.example.moviesapptask.BuildConfig
import com.example.moviesapptask.utilities.Constants
import com.example.moviesapptask.utilities.interceptors.CurlLoggingInterceptor
import com.example.moviesapptask.utilities.interceptors.HeadersInterceptor
import com.example.moviesapptask.utilities.interceptors.ResponseLoggingInterceptor
import com.example.moviesapptask.utilities.managers.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class NetworkModule {

    @Provides
    @Singleton
    fun provideInternetConnectionManager(context: Context): InternetConnectionManagerInterface {
        return InternetConnectionManager(context)
    }

    @Provides
    @Singleton
    fun provideApiRequestManager(resources: Resources): ApiRequestManagerInterface {
        return ApiRequestManager(resources)
    }

    @Provides
    @Singleton
    @Named("baseURL")
    fun provideBaseURL(): String {
        return "${Constants.baseURL}"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(HeadersInterceptor())

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder
                .addInterceptor(CurlLoggingInterceptor())
                .addInterceptor(ResponseLoggingInterceptor())
        }

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(
        @Named("baseURL") baseUrl: String,
        gson: Gson,
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()
    }
}

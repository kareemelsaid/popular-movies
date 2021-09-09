package com.example.moviesapptask.utilities.interceptors


import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeadersInterceptor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

                request
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")

        return chain.proceed(request.build())
    }
}
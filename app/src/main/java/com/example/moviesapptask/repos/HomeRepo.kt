package com.example.moviesapptask.repos


import com.example.moviesapptask.utilities.Constants
import com.example.moviesapptask.models.response.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import javax.inject.Inject

interface HomeApis {
    @GET(Constants.Endpoints.movie)
    suspend fun movie(@Query("page")page:Int): Response<MoviesResponse>

}

interface HomeRepoInterface {
    suspend fun movie(page: Int): Response<MoviesResponse>

}

class HomeRepo @Inject constructor(retrofit: Retrofit) : HomeRepoInterface {
    private val api = retrofit.create(HomeApis::class.java)
    override suspend fun movie(page: Int): Response<MoviesResponse> {
        return api.movie(page)
    }

}
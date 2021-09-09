package com.example.moviesapptask.utilities

import com.example.moviesapptask.models.response.Message


sealed class ResponseResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResponseResult<T>()
    data class Failure(val message: Message) : ResponseResult<Nothing>()
}
package com.example.moviesapptask.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface AppDispatcher {
    fun io()= Dispatchers.IO
    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun default() = Dispatchers.Default
}
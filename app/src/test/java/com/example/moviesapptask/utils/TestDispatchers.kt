package com.example.moviesapptask.utils

import com.example.moviesapptask.di.AppDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatchers : AppDispatcher {
    override fun io(): CoroutineDispatcher {
        return TestCoroutineDispatcher()
    }

    override fun main(): CoroutineDispatcher {
        return TestCoroutineDispatcher()
    }

    override fun default(): CoroutineDispatcher {
        return TestCoroutineDispatcher()
    }
}
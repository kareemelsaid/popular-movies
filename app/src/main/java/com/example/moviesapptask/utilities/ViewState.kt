package com.example.moviesapptask.utilities
import com.example.moviesapptask.models.response.Message


sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(var data: T): ViewState<T>()
    data class Error(val message: Message): ViewState<Nothing>()
}

sealed class CompletableViewState {
    object Loading : CompletableViewState()
    object Completed : CompletableViewState()
    class Error(val message: Message): CompletableViewState()
}

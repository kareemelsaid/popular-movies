package com.example.moviesapptask.ui.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapptask.R
import com.example.moviesapptask.models.response.Message
import com.example.moviesapptask.models.response.SetDataViewModel
import com.example.moviesapptask.repos.HomeRepoInterface
import com.example.moviesapptask.utilities.ViewState
import com.example.moviesapptask.utilities.managers.ApiRequestManagerInterface
import com.example.moviesapptask.utilities.managers.InternetConnectionManagerInterface
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val internetConnectionManager: InternetConnectionManagerInterface,
    private val apiRequestManager: ApiRequestManagerInterface,
    private val homeRepoInterface: HomeRepoInterface,
    private val resources: Resources,
) : ViewModel() {
    val setDataViewState = MutableLiveData<ViewState<ArrayList<SetDataViewModel>>>(ViewState.Loading)
    val totalPages = MutableLiveData<Int>()


    fun getMovie(page: Int = 1) {
        if (internetConnectionManager.isConnectedToInternet) {

            setDataViewState.value = ViewState.Loading

            apiRequestManager.execute(
                request = {
                    homeRepoInterface.movie(page)
                },
                onSuccess = { data, headers, statusCode ->
                    totalPages.value = data.total_pages
                    setDataViewState.value =
                        ViewState.Success(ArrayList(data.results.map {
                            SetDataViewModel(it)
                        }))


                },
                onFailure = { message, statusCode ->
                    setDataViewState.value = ViewState.Error(message)
                }
            )

        } else {
            setDataViewState.value =
                ViewState.Error(
                    Message(
                        resources.getString(
                            R.string.no_internet_connection
                        )
                    )
                )
        }
    }


}
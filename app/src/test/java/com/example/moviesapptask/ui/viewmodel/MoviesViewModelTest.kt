package com.example.moviesapptask.ui.viewmodel

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesapptask.R
import com.example.moviesapptask.models.response.Message
import com.example.moviesapptask.models.response.MoviesResponse
import com.example.moviesapptask.repos.HomeRepoInterface
import com.example.moviesapptask.utilities.ViewState
import com.example.moviesapptask.utilities.managers.ApiRequestManager
import com.example.moviesapptask.utilities.managers.ApiRequestManagerInterface
import com.example.moviesapptask.utilities.managers.InternetConnectionManager
import com.example.moviesapptask.utils.TestDispatchers
import com.example.moviesapptask.utils.getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MoviesViewModelTest : TestCase() {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var internetConnectionManager: InternetConnectionManager
    private lateinit var apiRequestManagerInterface: ApiRequestManagerInterface

    @Mock
    private lateinit var homeRepoInterface: HomeRepoInterface

    @Mock
    private lateinit var resources: Resources
    private lateinit var moviesViewModel: MoviesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        apiRequestManagerInterface = ApiRequestManager(resources, TestDispatchers())
        moviesViewModel = MoviesViewModel(
            internetConnectionManager,
            apiRequestManagerInterface,
            homeRepoInterface,
            resources
        )
    }

    @Test
    fun `Given no internet connection, When getMovie, Then emit Error State`() {
        // GIVEN
        Mockito.`when`(internetConnectionManager.isConnectedToInternet).thenReturn(false)
        Mockito.`when`(resources.getString(R.string.no_internet_connection))
            .thenReturn("No internet connection")

        // WHEN
        moviesViewModel.getMovie(page = 1)

        // THEN
        val actualValue = moviesViewModel.setDataViewState.getOrAwaitValue()
        val expectedValue = ViewState.Error(Message("No internet connection"))

        Assert.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Given internet connection, When getMovie, Then emit Success State`() = runBlockingTest {
        // GIVEN
        Mockito.`when`(internetConnectionManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(homeRepoInterface.movie(Mockito.anyInt()))
            .thenReturn(
                Response.success(
                    MoviesResponse(
                        page = 1,
                        arrayListOf(
                            MoviesResponse.Result(
                                id = 1,
                                original_title = "original_title",
                                overview = "overview",
                                poster_path = "poster_path",
                                release_date = "release_date",
                                title = "title",
                                vote_average = 5.0,
                                vote_count = 10
                            )
                        ),
                        total_pages = 1,
                        total_results = 1
                    )
                )
            )

        // WHEN
        moviesViewModel.getMovie(page = 1)

        // THEN
        val actualValue =  ViewState.Success(
            MoviesResponse(
                page = 1,
                arrayListOf(
                    MoviesResponse.Result(
                        id = 1,
                        original_title = "original_title",
                        overview = "overview",
                        poster_path = "poster_path",
                        release_date = "release_date",
                        title = "title",
                        vote_average = 5.0,
                        vote_count = 10
                    )
                ),
                total_pages = 1,
                total_results = 1
            )
        )

        val expectedValue =
            ViewState.Success(
                MoviesResponse(
                    page = 1,
                    arrayListOf(
                        MoviesResponse.Result(
                            id = 1,
                            original_title = "original_title",
                            overview = "overview",
                            poster_path = "poster_path",
                            release_date = "release_date",
                            title = "title",
                            vote_average = 5.0,
                            vote_count = 10
                        )
                    ),
                    total_pages = 1,
                    total_results = 1
                )
            )

        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Given internet connection, When getMovie, Then emit Error State`() = runBlockingTest {
        // GIVEN
        Mockito.`when`(internetConnectionManager.isConnectedToInternet).thenReturn(true)
        Mockito.`when`(homeRepoInterface.movie(Mockito.anyInt()))
            .thenReturn(
                Response.success(
                    MoviesResponse(
                        page = 1,
                        arrayListOf(),
                        total_pages = 1,
                        total_results = 1
                    )
                )
            )

        // WHEN
        moviesViewModel.getMovie(page = 1)

        // THEN
        val actualValue = moviesViewModel.setDataViewState.getOrAwaitValue()
        val expectedValue =
            ViewState.Error(
                Message(""))

        assertEquals(expectedValue, actualValue)
    }

}
package com.example.moviesapptask.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapptask.R
import com.example.moviesapptask.base.BaseActivity
import com.example.moviesapptask.models.response.SetDataViewModel
import com.example.moviesapptask.ui.viewmodel.MoviesViewModel
import com.example.moviesapptask.utilities.ViewState
import com.example.moviesapptask.utilities.builders.recyclerview.RecyclerViewBuilder
import com.example.moviesapptask.utilities.builders.recyclerview.RecyclerViewBuilderFactory
import com.example.moviesapptask.utilities.extensions.arraylist.toArrayList
import com.example.moviesapptask.utilities.extensions.imageview.load
import com.example.moviesapptask.utilities.extensions.toast.toast
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.dialog_movies_details.*

class MoviesActivity : BaseActivity() {
    private lateinit var recyclerViewBuilder: RecyclerViewBuilder
    private lateinit var viewModel: MoviesViewModel
    private var pageNumber = 1
    private var totalPages = 1
    private var dataFromPaginate: ArrayList<SetDataViewModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MoviesViewModel::class.java]
        setOnClickListener()
        setupObservers()
        viewModel.getMovie(pageNumber)
    }

    private fun setOnClickListener() {
        recyclerViewBuilder =
            RecyclerViewBuilderFactory(moviesRecyclerView)
                .buildWithGridLayout(
                    isDataBindingEnabled = true,
                    orientation = LinearLayoutManager.VERTICAL,
                    columnCount = 3
                )
                .setPaginationEnabled(true)
                .onPaginate {
                    if (pageNumber == totalPages) {
                        recyclerViewBuilder.setPaginationEnabled(false)
                        return@onPaginate
                    }
                    pageNumber++
                    viewModel.getMovie(pageNumber)
                }
                .startLoading()
                .setOnItemClick { itemView, model, position ->
                    val dialog = Dialog(this)
                    dialog.setContentView(R.layout.dialog_movies_details)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    window.setWindowAnimations(R.style.DialogAnimation)
                    dialog.titleTextView.text = (model as SetDataViewModel).originalTitle
                    dialog.posterImageView.load(model.posterPath)
                    dialog.overviewTextView.text = model.overview
                    dialog.userRateTextView.text = "${resources.getString(R.string.user_rate)}  ${model.voteAverage}"
                    dialog.releaseDateTextView.text = "${resources.getString(R.string.release_date)}  ${model.releaseDate}"
                    dialog.show()

                }

    }

    private fun setupObservers() {
        viewModel.totalPages.observe(this, Observer {
            totalPages = it
        })
        viewModel.setDataViewState.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    loadingDialog.show()

                }
                is ViewState.Success -> {
                    loadingDialog.dismiss()
                    it.data.map {
                        dataFromPaginate.add(it)
                    }
                    recyclerViewBuilder.setViewItems(dataFromPaginate.map {
                        it.viewItem
                    }.toArrayList(),true)
                }
                is ViewState.Error -> {
                    toast(it.message.errorMessage)
                }
            }
        })
    }
}
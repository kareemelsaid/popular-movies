package com.example.moviesapptask.ui.viewitem


import com.example.moviesapptask.R
import com.example.moviesapptask.databinding.ItemMovieBinding
import com.example.moviesapptask.models.response.SetDataViewModel
import com.example.moviesapptask.utilities.builders.recyclerview.BindingViewItem
import javax.inject.Inject


class MovieViewItem @Inject constructor(
    private val setDataViewModel: SetDataViewModel,
) : BindingViewItem<ItemMovieBinding>(
    R.layout.item_movie,
    setDataViewModel
) {

    override fun hashCode(): Int {
        return setDataViewModel.id!!
    }

    override fun bind(binding: ItemMovieBinding, viewItemPosition: Int) {
        binding.viewModel = setDataViewModel
    }
}
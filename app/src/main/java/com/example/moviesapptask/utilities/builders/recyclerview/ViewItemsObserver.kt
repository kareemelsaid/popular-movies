package com.example.moviesapptask.utilities.builders.recyclerview

data class ViewItemsObserver(
    var viewItemsArrayList: ArrayList<AbstractViewItem> = arrayListOf(),
    var clearsOnSet: Boolean = false,
    var appendToEnd: Boolean = true
)
package com.test.denis.repositoriesinfo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SearchViewModel : ViewModel() {
    private val _query = MutableLiveData<String>()
    val data = MutableLiveData<ViewState>()

    val query: LiveData<String> = _query

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }
        //nextPageHandler.reset()
        _query.value = input
    }
}
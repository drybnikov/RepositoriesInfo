package com.test.denis.repositoriesinfo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.network.PAGE_SIZE
import java.util.*

const val FIRST_PAGE: Int = 1

class SearchViewModel : ViewModel() {
    private val _query = MutableLiveData<String>()
    val data = MutableLiveData<ArrayList<Repo>>()
    val query: LiveData<String> = _query

    var currentPage: Int = FIRST_PAGE
    var totalCount: Int = 0
    var lastQuery: String = ""

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }
        //nextPageHandler.reset()
        _query.value = input
    }

    fun setData(newData: List<Repo>) {
        if (data.value == null) {
            data.value = ArrayList(newData)
        } else {
            data.value = data.value?.apply { addAll(newData) }
        }
    }
}

fun SearchViewModel.hasMoreElements() = currentPage * PAGE_SIZE < totalCount
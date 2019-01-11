package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.util.Log
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.model.RepositoryResponse
import com.test.denis.repositoriesinfo.network.PAGE_SIZE
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.io.Serializable
import javax.inject.Inject

const val FIRST_PAGE: Int = 1
const val VIEW_STATE_KEY: String = "stored_voew_state"

class RepositoryListPresenter @Inject constructor(private val repository: RepoRepository) {
    private val disposable = CompositeDisposable()
    private var view: RepositoryListView? = null
    private var viewState = ViewState.EMPTY

    fun onAttach(view: RepositoryListView, savedInstanceState: Bundle?) {
        this.view = view

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        } else {
            this.view?.setProgressVisibility(true)
            loadData()
        }
    }

    private fun restoreState(savedInstanceState: Bundle) {
        viewState = savedInstanceState.getSerializable(VIEW_STATE_KEY) as ViewState? ?: ViewState.EMPTY
        Log.d("restoreState", "data: $viewState")

        view?.apply {
            showRepositoryList(viewState.data)
            setProgressVisibility(false)
        }
    }

    private fun loadData() {
        disposable.add(
            repository
                .searchRepo(query = viewState.queryString, page = viewState.currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded) { Log.e("loadData", "error: $it") }
        )
    }

    private fun onDataLoaded(data: RepositoryResponse) {
        Log.d("onDataLoaded", "data: $data")
        viewState.totalCount = data.total
        viewState.data.addAll(data.items)

        view?.apply {
            if (viewState.isFirstPage()) {
                showMoreItems(data.items)
                setLoadMoreVisibility(false)
            } else {
                showRepositoryList(data.items)
                setProgressVisibility(false)
            }
        }
    }

    fun onDetach() {
        view = null
        disposable.clear()
    }

    fun loadNextPage() {
        if (viewState.hasMoreElements()) {
            this.view?.setLoadMoreVisibility(true)
            viewState.currentPage++

            loadData()
        }
    }

    fun setQuery(query: String) {
        viewState.apply {
            queryString = query
            currentPage = FIRST_PAGE
            data.clear()
        }

        view?.apply {
            setProgressVisibility(true)
            showRepositoryList(emptyList())
        }
        loadData()
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(VIEW_STATE_KEY, viewState)
    }
}

interface RepositoryListView {
    fun showRepositoryList(repos: List<Repo>)
    fun setProgressVisibility(visible: Boolean)
    fun setLoadMoreVisibility(visible: Boolean)
    fun showMoreItems(repos: List<Repo>)
}

data class ViewState(
    var queryString: String = "tetris",
    var currentPage: Int = FIRST_PAGE,
    var totalCount: Int = 0,
    var data: ArrayList<Repo>
) : Serializable {
    companion object {
        val EMPTY = ViewState(queryString = "tetris", currentPage = FIRST_PAGE, data = arrayListOf())
    }

    fun hasMoreElements() = currentPage * PAGE_SIZE < totalCount
    fun isFirstPage() = currentPage > FIRST_PAGE
}
package com.test.denis.repositoriesinfo.ui

import android.util.Log
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.model.RepositoryResponse
import com.test.denis.repositoriesinfo.network.PAGE_SIZE
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(private val repository: RepoRepository) {
    private val disposable = CompositeDisposable()
    private var view: RepositoryListView? = null
    private var currentPage: Int = 1
    private var totalCount: Int = 0

    fun onAttach(view: RepositoryListView) {
        this.view = view

        this.view?.setProgressVisibility(true)
        loadData()
    }

    private fun loadData() {
        disposable.add(
            repository
                .searchRepo(query = "tetris", page = currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded) { Log.e("loadData", "error: $it") }
        )
    }

    private fun onDataLoaded(data: RepositoryResponse) {
        Log.d("onDataLoaded", "data: $data")
        totalCount = data.total

        view?.apply {
            if (currentPage > 1) {
                showMoreItems(data.items.sortedBy { it.size })
                setLoadMoreVisibility(false)
            } else {
                showRepositoryList(data.items.sortedBy { it.size })
                setProgressVisibility(false)
            }
        }
    }

    fun onDetach() {
        view = null
        disposable.clear()
    }

    fun loadNextPage() {
        if (currentPage * PAGE_SIZE < totalCount) {
            this.view?.setLoadMoreVisibility(true)
            currentPage++

            loadData()
        }
    }
}

interface RepositoryListView {
    fun showRepositoryList(repos: List<Repo>)
    fun setProgressVisibility(visible: Boolean)
    fun setLoadMoreVisibility(visible: Boolean)
    fun showMoreItems(repos: List<Repo>)
}
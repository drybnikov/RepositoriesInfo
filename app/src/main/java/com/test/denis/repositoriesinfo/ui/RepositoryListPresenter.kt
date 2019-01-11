package com.test.denis.repositoriesinfo.ui

import android.util.Log
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.model.RepositoryResponse
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(private val repository: RepoRepository) {
    private lateinit var disposable: Disposable
    private var view: RepositoryListView? = null
    private var currentPage: Int = 1

    fun onAttach(view: RepositoryListView) {
        this.view = view

        disposable = repository
            .searchRepo(query = "tetris", page = currentPage)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { this.view?.setProgressVisibility(true) }
            .subscribe(this::onDataLoaded) { Log.e("loadData", "error: $it") }
    }

    private fun onDataLoaded(data: RepositoryResponse) {
        Log.d("loadData", "data: $data")

        view?.apply {
            showRepositoryList(data.items.sortedBy { it.size })
            setProgressVisibility(false)
        }
    }

    fun onDetach() {
        view = null
        disposable.dispose()
    }
}

interface RepositoryListView {
    fun showRepositoryList(repos: List<Repo>)
    fun setProgressVisibility(visible: Boolean)
}
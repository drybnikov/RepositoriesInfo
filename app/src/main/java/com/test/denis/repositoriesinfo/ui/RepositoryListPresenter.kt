package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.model.RepositoryResponse
import com.test.denis.repositoriesinfo.network.ConnectionStatus
import com.test.denis.repositoriesinfo.network.ConnectionStatusProvider
import com.test.denis.repositoriesinfo.network.PAGE_SIZE
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import java.io.IOException
import java.io.Serializable
import javax.inject.Inject

const val FIRST_PAGE: Int = 1
const val VIEW_STATE_KEY: String = "stored_voew_state"

class RepositoryListPresenter @Inject constructor(
    private val repository: RepoRepository,
    private val connectionStatusProvider: ConnectionStatusProvider
) {
    private val disposable = CompositeDisposable()
    private var connectionDisposable = Disposables.disposed()
    private var view: RepositoryListView? = null
    private var viewState = ViewState.EMPTY

    fun onAttach(view: RepositoryListView, savedInstanceState: Bundle?) {
        this.view = view

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        } else {
            onRetrieveReposListStart()
            loadData()
        }
    }

    private fun restoreState(savedInstanceState: Bundle) {
        viewState = savedInstanceState.getSerializable(VIEW_STATE_KEY) as ViewState? ?: ViewState.EMPTY
        Log.d("restoreState", "data: $viewState")

        view?.apply {
            showRepositoryList(viewState.data)
            onRetrieveReposListDone()
        }
    }

    private fun loadData() {
        disposable.add(
            repository
                .searchRepo(query = viewState.queryString, page = viewState.currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded, this::onDataError)
        )
    }

    private fun onDataLoaded(data: RepositoryResponse) {
        Log.d("onDataLoaded", "data: $data")
        viewState.totalCount = data.total
        viewState.data.addAll(data.items)
        onRetrieveReposListDone()

        view?.apply {
            if (viewState.isFirstPage()) {
                showMoreItems(data.items)
            } else {
                showRepositoryList(data.items)
            }
        }
    }

    private fun onDataError(error: Throwable) {
        Log.e("onDataError", "error: $error")

        onRetrieveReposListDone()
        view?.showError(R.string.repo_error)

        if (error is IOException) {
            connectionDisposable = connectionStatusProvider
                .status
                .subscribe {
                    if (it == ConnectionStatus.CONNECTED) {
                        connectionDisposable.dispose()

                        setQuery(viewState.queryString)
                    }
                }
        }
    }

    fun onDetach() {
        view = null
        disposable.clear()
        connectionDisposable.dispose()
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

        onRetrieveReposListStart()
        loadData()
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(VIEW_STATE_KEY, viewState)
    }

    fun retry() {
        onRetrieveReposListStart()
        loadData()
    }

    private fun onRetrieveReposListStart() {
        view?.apply {
            setProgressVisibility(true)
            hideError()
        }
    }

    private fun onRetrieveReposListDone() {
        view?.apply {
            setProgressVisibility(false)
            setLoadMoreVisibility(false)
        }
    }
}

interface RepositoryListView {
    fun showRepositoryList(repos: List<Repo>)
    fun setProgressVisibility(visible: Boolean)
    fun setLoadMoreVisibility(visible: Boolean)
    fun showMoreItems(repos: List<Repo>)
    fun showError(@StringRes errorMessage: Int)
    fun hideError()
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
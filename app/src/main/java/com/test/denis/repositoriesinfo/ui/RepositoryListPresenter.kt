package com.test.denis.repositoriesinfo.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.model.RepositoryResponse
import com.test.denis.repositoriesinfo.network.ConnectionStatus
import com.test.denis.repositoriesinfo.network.ConnectionStatusProvider
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import java.io.IOException
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(
    private val repository: RepoRepository,
    private val connectionStatusProvider: ConnectionStatusProvider,
    private val searchViewModel: SearchViewModel
) {
    private val disposable = CompositeDisposable()
    private var connectionDisposable = Disposables.disposed()
    private var view: RepositoryListView? = null

    fun onAttach(view: RepositoryListView) {
        this.view = view

        subscribeToDataUpdate()
        subscribeToSearchUpdate()

        if (searchViewModel.data.value == null) {
            onRetrieveReposListStart()
            loadData("kotlinbackend")
        }
    }

    private fun subscribeToSearchUpdate() {
        searchViewModel.query.observe(view!!, Observer { result ->
            setQuery(result)
        })
    }

    private fun subscribeToDataUpdate() {
        view?.let {
            searchViewModel.data.observe(it, Observer { result ->
                Log.w("observe", "data: $result")

                it.showRepositoryList(result)
                onRetrieveReposListDone()
            })
        }

    }

    private fun loadData(query: String?) {
        query?.let {
            searchViewModel.lastQuery = query

            disposable.add(
                repository
                    .searchRepo(query = query, page = searchViewModel.currentPage)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onDataLoaded, this::onDataError)
            )
        }
    }

    private fun onDataLoaded(data: RepositoryResponse) {
        Log.d("onDataLoaded", "data: $data")
        searchViewModel.totalCount = data.total
        searchViewModel.setData(data.items)
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

                        setQuery(searchViewModel.query.value!!)
                    }
                }
        }
    }

    fun onDetach() {
        //searchViewModel.data.removeObservers(view!!) //TODO Check on MemoryLeak
        view = null
        disposable.clear()
        connectionDisposable.dispose()
    }

    fun loadNextPage() {

        if (searchViewModel.hasMoreElements()) {
            this.view?.setLoadMoreVisibility(true)
            searchViewModel.currentPage++

            loadData(searchViewModel.query.value)
        }
    }

    fun setQuery(query: String) {
        if (query == searchViewModel.lastQuery) {
            return
        }

        searchViewModel.currentPage = FIRST_PAGE
        searchViewModel.data.value?.clear()

        onRetrieveReposListStart()
        loadData(query)
    }

    fun retry() {
        onRetrieveReposListStart()
        loadData(searchViewModel.query.value)
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

    fun onItemClick(repo: Repo) {
        view?.navigateTo(RepositoryListFragmentDirections.openRepository(repo.fullName))
    }
}

interface RepositoryListView : LifecycleOwner {
    fun showRepositoryList(repos: List<Repo>)
    fun setProgressVisibility(visible: Boolean)
    fun setLoadMoreVisibility(visible: Boolean)
    fun showError(@StringRes errorMessage: Int)
    fun hideError()
    fun navigateTo(directions: NavDirections)
}
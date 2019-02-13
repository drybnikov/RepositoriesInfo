package com.test.denis.repositoriesinfo.ui

import android.os.Parcelable
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
import com.test.denis.repositoriesinfo.network.PAGE_SIZE
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import kotlinx.android.parcel.Parcelize
import java.io.IOException
import javax.inject.Inject

const val FIRST_PAGE: Int = 1

class RepositoryListPresenter @Inject constructor(
    private val repository: RepoRepository,
    private val connectionStatusProvider: ConnectionStatusProvider,
    private val searchViewModel: SearchViewModel
) {
    private val disposable = CompositeDisposable()
    private var connectionDisposable = Disposables.disposed()
    private var view: RepositoryListView? = null
    private var viewState = ViewState.EMPTY

    fun onAttach(view: RepositoryListView) {
        this.view = view

        searchViewModel.data.observe(view, Observer { result ->
            Log.w("observe", "data: $viewState")
        })

        if (searchViewModel.data.value != null) {
            viewState = searchViewModel.data.value!!
            restoreState()
        } else {
            searchViewModel.data.value = viewState
            onRetrieveReposListStart()
            loadData()
        }
    }

    private fun restoreState() {
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
        searchViewModel.data.removeObservers(view!!)
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
        if (query == viewState.queryString) {
            return
        }

        viewState.apply {
            queryString = query
            currentPage = FIRST_PAGE
            data.clear()
        }

        onRetrieveReposListStart()
        loadData()
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

    fun onItemClick(repo: Repo) {
        view?.navigateTo(RepositoryListFragmentDirections.openRepository(repo.fullName))
    }
}

interface RepositoryListView : LifecycleOwner {
    fun showRepositoryList(repos: List<Repo>)
    fun setProgressVisibility(visible: Boolean)
    fun setLoadMoreVisibility(visible: Boolean)
    fun showMoreItems(repos: List<Repo>)
    fun showError(@StringRes errorMessage: Int)
    fun hideError()
    fun navigateTo(directions: NavDirections)
}

@Parcelize
data class ViewState(
    var queryString: String,
    var currentPage: Int,
    var totalCount: Int = 0,
    var data: ArrayList<Repo>
) : Parcelable {
    companion object {
        val EMPTY = ViewState(queryString = "kotlinbackend", currentPage = FIRST_PAGE, data = arrayListOf())
    }
}

fun ViewState.hasMoreElements() = currentPage * PAGE_SIZE < totalCount

fun ViewState.isFirstPage() = currentPage > FIRST_PAGE
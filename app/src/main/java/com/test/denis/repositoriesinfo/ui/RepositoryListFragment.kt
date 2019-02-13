package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.util.autoCleared
import com.test.denis.repositoriesinfo.widget.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_repository_list.*
import javax.inject.Inject

class RepositoryListFragment : Fragment(), Injectable, RepositoryListView {

    @Inject
    lateinit var presenter: RepositoryListPresenter

    private var viewAdapter by autoCleared<RepositoryListAdapter>()
    private var errorSnackbar: Snackbar? = null
    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_repository_list, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initList()

        presenter.onAttach(this)
    }

    private fun initList() {
        viewAdapter = RepositoryListAdapter { presenter.onItemClick(it) }
        contentList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = viewAdapter

            addOnScrollListener(object : PaginationScrollListener(layoutManager as LinearLayoutManager) {

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    isLoading = true
                    presenter.loadNextPage()
                }
            })
        }
    }

    override fun showRepositoryList(repos: List<Repo>) {
        isLoading = false
        viewAdapter.initData(repos)
    }

    override fun setProgressVisibility(visible: Boolean) {
        if (visible) {
            progressBar.show()
        } else {
            progressBar.hide()
        }
    }

    override fun setLoadMoreVisibility(visible: Boolean) {
        loadMoreBar.visibility = if (visible) VISIBLE else GONE
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showError(@StringRes errorMessage: Int) {
        isLoading = false
        errorSnackbar = Snackbar.make(view!!.rootView, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(R.string.retry, { presenter.retry() })
                errorSnackbar?.show()
            }
    }

    override fun hideError() {
        errorSnackbar?.dismiss()
    }

    override fun navigateTo(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}
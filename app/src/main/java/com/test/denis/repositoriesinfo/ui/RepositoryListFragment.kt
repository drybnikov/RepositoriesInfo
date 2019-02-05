package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable
import com.test.denis.repositoriesinfo.model.Repo
import com.test.denis.repositoriesinfo.widget.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_repository_list.*
import javax.inject.Inject

class RepositoryListFragment : Fragment(), Injectable, RepositoryListView {

    @Inject
    lateinit var presenter: RepositoryListPresenter

    lateinit var searchViewModel: SearchViewModel

    private lateinit var viewAdapter: RepositoryListAdapter
    private var errorSnackbar: Snackbar? = null
    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_repository_list, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initList()
        searchViewModel = ViewModelProviders.of(activity!!).get(SearchViewModel::class.java)

        presenter.onAttach(this, savedInstanceState)

        subscribeToSearchUpdate()
    }

    private fun initList() {
        viewAdapter = RepositoryListAdapter()
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

    private fun subscribeToSearchUpdate() {
        searchViewModel.query.observe(viewLifecycleOwner, Observer { result ->
            presenter.setQuery(result)
        })
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

    override fun showMoreItems(repos: List<Repo>) {
        isLoading = false
        viewAdapter.addData(repos)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun showError(@StringRes errorMessage: Int) {
        isLoading = false
        errorSnackbar = Snackbar.make(view!!.rootView, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, { presenter.retry() })
        errorSnackbar?.show()
    }

    override fun hideError() {
        errorSnackbar?.dismiss()
    }

    companion object {
        private const val TAG = "RepositoryListFragment"

        fun attachIfNeeded(@IdRes containerViewId: Int, fm: FragmentManager) {
            if (fm.findFragmentByTag(TAG) == null) {
                fm.beginTransaction()
                    .add(containerViewId, RepositoryListFragment(), TAG)
                    .commitAllowingStateLoss()
            }
        }
    }
}
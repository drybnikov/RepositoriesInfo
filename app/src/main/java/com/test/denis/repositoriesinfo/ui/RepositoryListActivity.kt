package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable
import com.test.denis.repositoriesinfo.model.Repo
import kotlinx.android.synthetic.main.activity_repository_list.*
import javax.inject.Inject

class RepositoryListActivity : AppCompatActivity(), Injectable, RepositoryListView {

    @Inject
    lateinit var presenter: RepositoryListPresenter

    private lateinit var viewAdapter: RepositoryListAdapter
    private var isLastPage = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        initList()
        presenter.onAttach(this)
    }

    private fun initList() {
        viewAdapter = RepositoryListAdapter()
        contentList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = viewAdapter
            itemAnimator = DefaultItemAnimator()

            addOnScrollListener(object : PaginationScrollListener(layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return isLastPage
                }

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
}
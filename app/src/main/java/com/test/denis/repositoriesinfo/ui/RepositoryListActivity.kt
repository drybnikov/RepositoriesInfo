package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        }
    }

    override fun showRepositoryList(channels: List<Repo>) {
        viewAdapter.apply {
            items = channels
            notifyDataSetChanged()
        }
    }

    override fun setProgressVisibility(visible: Boolean) {
        if (visible) {
            progressBar.show()
        } else {
            progressBar.hide()
        }
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
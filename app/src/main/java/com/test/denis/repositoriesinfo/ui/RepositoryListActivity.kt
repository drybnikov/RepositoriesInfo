package com.test.denis.repositoriesinfo.ui

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable
import com.test.denis.repositoriesinfo.model.Repo
import kotlinx.android.synthetic.main.activity_repository_list.*
import javax.inject.Inject

class RepositoryListActivity : AppCompatActivity(), Injectable, RepositoryListView {

    @Inject
    lateinit var presenter: RepositoryListPresenter

    private lateinit var viewAdapter: RepositoryListAdapter
    private var errorSnackbar: Snackbar? = null
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        initList()
        initSearchInputListener()
        presenter.onAttach(this, savedInstanceState)
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

    private fun initSearchInputListener() {
        inputSearch.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        inputSearch.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = inputSearch.text.toString()

        dismissKeyboard(v.windowToken)
        presenter.setQuery(query)
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

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        presenter.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun showError(@StringRes errorMessage: Int) {
        isLoading = false
        errorSnackbar = Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, { presenter.retry() })
        errorSnackbar?.show()
    }

    override fun hideError() {
        errorSnackbar?.dismiss()
    }
}
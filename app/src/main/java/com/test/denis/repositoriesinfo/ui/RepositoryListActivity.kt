package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable
import javax.inject.Inject

class RepositoryListActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var presenter: RepositoryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        if (savedInstanceState == null)
            presenter.onAttach()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
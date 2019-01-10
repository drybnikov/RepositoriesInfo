package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RepositoryListActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var repository: RepoRepository

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        if (savedInstanceState == null)
            loadData()
    }

    private fun loadData() {
        disposable = repository
            .searchRepo("kotlinbackend")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d("loadData", "data: $it") },
                { Log.e("loadData", "error: $it") }
            )
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
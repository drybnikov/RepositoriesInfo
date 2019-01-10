package com.test.denis.repositoriesinfo.ui

import android.util.Log
import com.test.denis.repositoriesinfo.network.RepoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(private val repository: RepoRepository) {
    private lateinit var disposable: Disposable

    fun onAttach() {
        disposable = repository
            .searchRepo("kotlinbackend")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d("loadData", "data: $it") },
                { Log.e("loadData", "error: $it") }
            )
    }

    fun onDetach() {
        disposable.dispose()
    }
}
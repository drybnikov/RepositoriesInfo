package com.test.denis.repositoriesinfo.di

import androidx.lifecycle.ViewModelProviders
import com.test.denis.repositoriesinfo.ui.RepositoryListActivity
import com.test.denis.repositoriesinfo.ui.SearchViewModel
import dagger.Module
import dagger.Provides

@Module
class RepositoryListActivityModule {

    @Provides
    fun provideSearchViewModel(activity: RepositoryListActivity) =
        ViewModelProviders.of(activity).get(SearchViewModel::class.java)
}
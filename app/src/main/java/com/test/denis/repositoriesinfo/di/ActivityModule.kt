package com.test.denis.repositoriesinfo.di

import com.test.denis.repositoriesinfo.ui.RepositoryListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector()
    abstract fun contributeRepositoryListActivity(): RepositoryListActivity
}
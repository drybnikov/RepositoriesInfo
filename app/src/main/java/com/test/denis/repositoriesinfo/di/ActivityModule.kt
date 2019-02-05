package com.test.denis.repositoriesinfo.di

import com.test.denis.repositoriesinfo.ui.MotionLayoutActivity
import com.test.denis.repositoriesinfo.ui.RepositoryListActivity
import com.test.denis.repositoriesinfo.ui.RepositoryListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector()
    abstract fun contributeRepositoryListActivity(): RepositoryListActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMotionLayoutActivity(): MotionLayoutActivity
}
package com.test.denis.repositoriesinfo.di

import com.test.denis.repositoriesinfo.ui.MotionDashboardFragment
import com.test.denis.repositoriesinfo.ui.RepositoryListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeRepositoryListFragment(): RepositoryListFragment

    @ContributesAndroidInjector
    abstract fun contributeMotionDashboardFragment(): MotionDashboardFragment
}
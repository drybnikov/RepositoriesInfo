package com.test.denis.repositoriesinfo.di

import com.test.denis.repositoriesinfo.ui.MotionLayoutActivity
import com.test.denis.repositoriesinfo.ui.RepositoryListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [RepositoryListActivityModule::class])
    abstract fun contributeRepositoryListActivity(): RepositoryListActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class, MotionLayoutActivityModule::class])
    abstract fun contributeMotionLayoutActivity(): MotionLayoutActivity
}
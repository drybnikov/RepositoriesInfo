package com.test.denis.repositoriesinfo.di

import androidx.lifecycle.ViewModelProviders
import com.test.denis.repositoriesinfo.ui.MotionLayoutActivity
import com.test.denis.repositoriesinfo.ui.SearchViewModel
import dagger.Module
import dagger.Provides

@Module
class MotionLayoutActivityModule {

    @Provides
    fun provideSearchViewModel(activity: MotionLayoutActivity) =
        ViewModelProviders.of(activity).get(SearchViewModel::class.java)
}
package com.test.denis.repositoriesinfo.di

import com.test.denis.repositoriesinfo.RepositoriesInfoApp

object AppInjector {
    fun init(app: RepositoriesInfoApp) {
        DaggerAppComponent.builder()
            .application(app)
            .build()
            .inject(app)
    }
}
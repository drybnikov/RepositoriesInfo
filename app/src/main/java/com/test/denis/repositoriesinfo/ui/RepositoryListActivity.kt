package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable

class RepositoryListActivity : AppCompatActivity(), Injectable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)
    }
}

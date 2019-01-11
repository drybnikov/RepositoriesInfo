package com.test.denis.repositoriesinfo.network

import javax.inject.Inject

const val PAGE_SIZE: Int = 10

class RepoRepository @Inject constructor(private val api: RepositoryApi) {

    fun searchRepo(query: String, page: Int, perPage: Int = PAGE_SIZE) =
        api.getRepositories(query, page, perPage)
}
package com.test.denis.repositoriesinfo.network

import javax.inject.Inject

class RepoRepository @Inject constructor(private val api: RepositoryApi) {

    fun searchRepo(query: String, page: Int, perPage: Int = 10) = api.getRepositories(query, page, perPage)
}
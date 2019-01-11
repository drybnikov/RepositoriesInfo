package com.test.denis.repositoriesinfo.network

import com.test.denis.repositoriesinfo.model.RepositoryResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL: String = "https://api.github.com"

interface RepositoryApi {
    @GET("/search/repositories")
    fun getRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Observable<RepositoryResponse>
}
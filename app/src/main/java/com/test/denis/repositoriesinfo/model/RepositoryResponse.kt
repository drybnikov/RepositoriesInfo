package com.test.denis.repositoriesinfo.model

import com.squareup.moshi.Json
import java.io.Serializable

data class RepositoryResponse(
    @field:Json(name = "total_count")
    val total: Int = 0,
    val items: List<Repo>
)

data class Repo(
    val id: Long,
    val name: String,
    val size: Int,
    @field:Json(name = "full_name")
    val fullName: String,
    val owner: Owner,
    @field:Json(name = "has_wiki")
    val hasWiki: Boolean
) : Serializable

data class Owner(
    @field:Json(name = "login")
    val login: String,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "avatar_url")
    val avatarUrl: String?
) : Serializable

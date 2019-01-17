package com.test.denis.repositoriesinfo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class RepositoryResponse(
    @field:Json(name = "total_count")
    val total: Int = 0,
    val items: List<Repo>
)

@Parcelize
data class Repo(
    val id: Long,
    val name: String,
    val size: Int,
    @field:Json(name = "full_name")
    val fullName: String,
    val owner: Owner,
    @field:Json(name = "has_wiki")
    val hasWiki: Boolean
) : Parcelable

@Parcelize
data class Owner(
    @field:Json(name = "login")
    val login: String,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "avatar_url")
    val avatarUrl: String?
) : Parcelable

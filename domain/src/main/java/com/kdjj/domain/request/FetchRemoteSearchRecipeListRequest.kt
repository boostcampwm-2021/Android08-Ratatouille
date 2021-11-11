package com.kdjj.domain.request

data class FetchRemoteSearchRecipeListRequest(
    val keyword: String,
    val lastVisibleTitle: String,
) : Request
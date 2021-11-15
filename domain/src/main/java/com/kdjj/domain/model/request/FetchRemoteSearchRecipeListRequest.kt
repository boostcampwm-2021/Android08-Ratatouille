package com.kdjj.domain.model.request

data class FetchRemoteSearchRecipeListRequest(
    val keyword: String,
    val lastVisibleTitle: String,
) : Request

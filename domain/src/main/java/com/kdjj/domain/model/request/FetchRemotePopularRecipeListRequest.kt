package com.kdjj.domain.model.request

data class FetchRemotePopularRecipeListRequest(
    val lastVisibleViewCount: Int,
) : Request

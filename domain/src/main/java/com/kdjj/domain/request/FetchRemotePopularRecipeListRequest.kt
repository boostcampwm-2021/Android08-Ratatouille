package com.kdjj.domain.request

data class FetchRemotePopularRecipeListRequest(
    val lastVisibleViewCount: Int,
) : Request
package com.kdjj.domain.request

data class FetchRemotePopularRecipeListRequset(
    val lastVisibleViewCount: Int,
) : Request
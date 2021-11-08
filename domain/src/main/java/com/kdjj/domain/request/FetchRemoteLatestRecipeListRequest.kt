package com.kdjj.domain.request

data class FetchRemoteLatestRecipeListRequest(
    val lastVisibleCreateTime: Long,
) : Request
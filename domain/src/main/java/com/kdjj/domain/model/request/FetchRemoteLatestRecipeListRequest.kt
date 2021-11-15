package com.kdjj.domain.model.request

data class FetchRemoteLatestRecipeListRequest(
    val lastVisibleCreateTime: Long,
) : Request

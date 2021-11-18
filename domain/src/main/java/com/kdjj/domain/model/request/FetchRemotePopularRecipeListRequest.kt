package com.kdjj.domain.model.request

data class FetchRemotePopularRecipeListRequest(
    val refresh: Boolean,
) : Request

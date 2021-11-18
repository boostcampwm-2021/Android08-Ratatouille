package com.kdjj.domain.model.request

data class FetchRemoteLatestRecipeListRequest(
    val refresh: Boolean,
) : Request

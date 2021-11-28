package com.kdjj.domain.model.request

data class FetchOthersSearchRecipeListRequest(
    val keyword: String,
    val refresh: Boolean
) : Request

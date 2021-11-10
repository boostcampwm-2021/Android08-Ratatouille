package com.kdjj.domain.request

data class FetchLocalSearchRecipeListRequest(
    val keyword: String,
    val index: Int
) : Request
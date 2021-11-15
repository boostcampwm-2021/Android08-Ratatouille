package com.kdjj.domain.model.request

data class FetchLocalSearchRecipeListRequest(
    val keyword: String,
    val index: Int
) : Request

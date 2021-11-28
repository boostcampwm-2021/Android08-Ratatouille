package com.kdjj.domain.model.request

data class FetchMySearchRecipeListRequest(
    val keyword: String,
    val index: Int
) : Request

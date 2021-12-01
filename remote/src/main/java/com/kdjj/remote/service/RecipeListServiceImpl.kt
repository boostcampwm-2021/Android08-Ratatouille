package com.kdjj.remote.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.remote.dto.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeListServiceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : RemoteRecipeListService {

    private val queryMap = mutableMapOf<String, Query?>(
        "LATEST_LIST_QUERY" to null,
        "POPULAR_LIST_QUERY" to null,
        "SEARCH_LIST_QUERY" to null
    )

    private suspend fun fetchRecipeList(
        refresh: Boolean,
        listQuery: Query,
        key: String,
        limitSize: Long = DEFAULT_PAGING_SIZE,
    ): List<RecipeDto> = withContext(Dispatchers.IO) {
        if (refresh) {
            queryMap[key] = null
        }

        val query = queryMap[key] ?: listQuery.limit(limitSize)
        val querySnapshot = query.get(Source.SERVER).await()
        if (querySnapshot.documents.isNotEmpty()) {
            queryMap[key] = query.startAfter(querySnapshot.documents.last())
        }

        querySnapshot.map { queryDocumentSnapshot ->
            queryDocumentSnapshot.toObject()
        }

    }

    override suspend fun fetchLatestRecipeListAfter(
        refresh: Boolean
    ): List<RecipeDto> {
        val listQuery = fireStore.collection(RECIPE_COLLECTION_ID)
            .orderBy(FIELD_CREATE_TIME, Query.Direction.DESCENDING)

        return fetchRecipeList(refresh, listQuery, LATEST_LIST_QUERY)
    }

    override suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): List<RecipeDto> {
        val listQuery = fireStore.collection(RECIPE_COLLECTION_ID)
            .orderBy(FIELD_VIEW_COUNT, Query.Direction.DESCENDING)

        return fetchRecipeList(refresh, listQuery, POPULAR_LIST_QUERY)
    }

    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): List<RecipeDto> {
        val listQuery = fireStore.collection(RECIPE_COLLECTION_ID)
            .whereGreaterThanOrEqualTo(FIELD_TITLE, keyword)
            .whereLessThan(FIELD_TITLE, keyword + HANGLE_MAX_VALUE)
            .orderBy(FIELD_TITLE, Query.Direction.ASCENDING)

        return fetchRecipeList(refresh, listQuery, SEARCH_LIST_QUERY)
    }

    companion object {

        const val HANGLE_MAX_VALUE = "힣"
        const val FIELD_TITLE = "title"
        const val FIELD_CREATE_TIME = "createTime"
        const val FIELD_VIEW_COUNT = "viewCount"
        const val RECIPE_COLLECTION_ID = "recipe"
        const val DEFAULT_PAGING_SIZE = 10L
        const val LATEST_LIST_QUERY = "latestListQuery"
        const val POPULAR_LIST_QUERY = "popularListQuery"
        const val SEARCH_LIST_QUERY = "searchListQuery"
    }
}

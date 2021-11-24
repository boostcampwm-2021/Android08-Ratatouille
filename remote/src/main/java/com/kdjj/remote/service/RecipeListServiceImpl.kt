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

    private var latestListQuery: Query? = null
    private var popularListQuery: Query? = null
    private var searchListQuery: Query? = null

    override suspend fun fetchLatestRecipeListAfter(
        refresh: Boolean
    ): List<RecipeDto> =
        withContext(Dispatchers.IO) {
            if (refresh) {
                latestListQuery = null
            }

            val query = latestListQuery ?: fireStore.collection(RECIPE_COLLECTION_ID)
                .orderBy(FIELD_CREATE_TIME, Query.Direction.DESCENDING)
                .limit(PAGING_SIZE)

            val querySnapshot = query.get(Source.SERVER).await()
            if (querySnapshot.documents.isNotEmpty()) {
                latestListQuery = fireStore.collection(RECIPE_COLLECTION_ID)
                    .orderBy(FIELD_CREATE_TIME, Query.Direction.DESCENDING)
                    .startAfter(querySnapshot.documents.last())
                    .limit(PAGING_SIZE)
            }
            querySnapshot.map {
                it.toObject<RecipeDto>()
            }
        }

    override suspend fun fetchPopularRecipeListAfter(
        refresh: Boolean
    ): List<RecipeDto> =
        withContext(Dispatchers.IO) {
            if (refresh) {
                popularListQuery = null
            }

            val query = popularListQuery ?: fireStore.collection(RECIPE_COLLECTION_ID)
                .orderBy(FIELD_VIEW_COUNT, Query.Direction.DESCENDING)
                .limit(PAGING_SIZE)

            val querySnapshot = query.get(Source.SERVER).await()
            if (querySnapshot.documents.isNotEmpty()) {
                popularListQuery = fireStore.collection(RECIPE_COLLECTION_ID)
                    .orderBy(FIELD_VIEW_COUNT, Query.Direction.DESCENDING)
                    .startAfter(querySnapshot.documents.last())
                    .limit(PAGING_SIZE)
            }
            querySnapshot.map {
                it.toObject<RecipeDto>()
            }
        }

    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        refresh: Boolean
    ): List<RecipeDto> =
        withContext(Dispatchers.IO) {
            if (refresh) {
                searchListQuery = null
            }

            val query = searchListQuery ?: fireStore.collection(RECIPE_COLLECTION_ID)
                .whereGreaterThanOrEqualTo(FIELD_TITLE, keyword)
                .whereLessThan(FIELD_TITLE, keyword + HANGLE_MAX_VALUE)
                .orderBy(FIELD_TITLE, Query.Direction.ASCENDING)
                .limit(PAGING_SIZE)

            val querySnapshot = query.get(Source.SERVER).await()
            if (querySnapshot.documents.isNotEmpty()) {
                searchListQuery = fireStore.collection(RECIPE_COLLECTION_ID)
                    .whereGreaterThanOrEqualTo(FIELD_TITLE, keyword)
                    .whereLessThan(FIELD_TITLE, keyword + HANGLE_MAX_VALUE)
                    .orderBy(FIELD_TITLE, Query.Direction.ASCENDING)
                    .startAfter(querySnapshot.documents.last())
                    .limit(PAGING_SIZE)
            }
            querySnapshot.map {
                it.toObject<RecipeDto>()
            }
        }

    companion object {

        const val HANGLE_MAX_VALUE = "힣"
        const val FIELD_TITLE = "title"
        const val FIELD_CREATE_TIME = "createTime"
        const val FIELD_VIEW_COUNT = "viewCount"
        const val RECIPE_COLLECTION_ID = "recipe"
        const val PAGING_SIZE = 10L
    }
}

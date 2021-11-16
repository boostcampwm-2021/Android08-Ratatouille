package com.kdjj.remote.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import com.kdjj.remote.dto.RecipeDto
import com.kdjj.remote.dto.toDomain
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

internal class RecipeListServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteRecipeListService {

    override suspend fun fetchLatestRecipeListAfter(
        lastVisibleCreateTime: Long
    ): List<Recipe> =
        suspendCancellableCoroutine {
            firestore.collection(RECIPE_COLLECTION_ID)
                .orderBy(FIELD_CREATE_TIME, Query.Direction.DESCENDING)
                .startAfter(lastVisibleCreateTime)
                .limit(PAGING_SIZE)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    if (queryDocumentSnapshot.metadata.isFromCache) {
                        it.resumeWithException(NetworkException())
                    } else {
                        it.resumeWith(
                            Result.success(
                                queryDocumentSnapshot.map { item ->
                                    item.toObject<RecipeDto>().toDomain()
                                }
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    it.resumeWithException(ApiException(exception.message))
                }
        }
    
    override suspend fun fetchPopularRecipeListAfter(
        lastVisibleViewCount: Int
    ): List<Recipe> =
        suspendCancellableCoroutine {
            firestore.collection(RECIPE_COLLECTION_ID)
                .orderBy(FIELD_VIEW_COUNT, Query.Direction.DESCENDING)
                .startAfter(lastVisibleViewCount)
                .limit(PAGING_SIZE)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    if (queryDocumentSnapshot.metadata.isFromCache) {
                        it.resumeWithException(NetworkException())
                    } else {
                        it.resumeWith(
                            Result.success(
                                queryDocumentSnapshot.map { item ->
                                    item.toObject<RecipeDto>().toDomain()
                                }
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    it.resumeWithException(ApiException(exception.message))
                }
        }
    
    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): List<Recipe> =
        suspendCancellableCoroutine {
            firestore.collection(RECIPE_COLLECTION_ID)
                .whereGreaterThanOrEqualTo(FIELD_TITLE, keyword)
                .whereLessThan(FIELD_TITLE, keyword + HANGLE_MAX_VALUE)
                .orderBy(FIELD_TITLE, Query.Direction.ASCENDING)
                .startAfter(lastVisibleTitle)
                .limit(PAGING_SIZE)
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                    if (queryDocumentSnapshot.metadata.isFromCache) {
                        it.resumeWithException(NetworkException())
                    } else {
                        it.resumeWith(
                            Result.success(
                                queryDocumentSnapshot.map { item ->
                                    item.toObject<RecipeDto>().toDomain()
                                }
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    it.resumeWithException(ApiException(exception.message))
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

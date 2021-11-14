package com.kdjj.remote.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.dto.RecipeEntity
import com.kdjj.remote.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeListDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteRecipeListDao {
    
    override suspend fun fetchLatestRecipeListAfter(
        lastVisibleCreateTime: Long
    ): List<Recipe> =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .orderBy(FIELD_CREATE_TIME, Query.Direction.DESCENDING)
                .startAfter(lastVisibleCreateTime)
                .limit(PAGING_SIZE)
                .get()
                .await()
                .map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject<RecipeEntity>().toDomain()
                }
        }
    
    override suspend fun fetchPopularRecipeListAfter(
        lastVisibleViewCount: Int
    ): List<Recipe> =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .orderBy(FIELD_VIEW_COUNT, Query.Direction.DESCENDING)
                .startAfter(lastVisibleViewCount)
                .limit(PAGING_SIZE)
                .get()
                .await()
                .map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject<RecipeEntity>().toDomain()
                }
        }
    
    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        lastVisibleTitle: String
    ): List<Recipe> =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .whereGreaterThanOrEqualTo(FIELD_TITLE, keyword)
                .whereLessThan(FIELD_TITLE, keyword + HANGLE_MAX_VALUE)
                .orderBy(FIELD_TITLE, Query.Direction.ASCENDING)
                .startAfter(lastVisibleTitle)
                .limit(PAGING_SIZE)
                .get()
                .await()
                .map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject<RecipeEntity>().toDomain()
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

package com.kdjj.remote.dao

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.entity.RecipeEntity
import com.kdjj.remote.entity.RecipeTypeEntity
import com.kdjj.remote.entityToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeListDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RecipeListDao {

    override suspend fun fetchLatestRecipeList(): List<Recipe> =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .get()
                .await()
                .map { queryDocumentSnapshot ->
                    entityToDomain(queryDocumentSnapshot.toObject<RecipeEntity>())
                }
        }


    companion object {

        const val RECIPE_COLLECTION_ID = "recipe"
    }
}
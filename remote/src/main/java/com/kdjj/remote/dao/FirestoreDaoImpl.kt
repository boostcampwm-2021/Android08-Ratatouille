package com.kdjj.remote.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.dto.RecipeTypeEntity
import com.kdjj.remote.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class FirestoreDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreDao {
    
    override suspend fun fetchRecipeTypes(): List<RecipeType> =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_TYPE_COLLECTION_ID)
                .get()
                .await()
                .map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject<RecipeTypeEntity>().toDomain()
                }
        }
    
    companion object {
        
        const val RECIPE_TYPE_COLLECTION_ID = "recipeType"
    }
}

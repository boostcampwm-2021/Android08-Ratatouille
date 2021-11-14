package com.kdjj.remote.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.kdjj.domain.model.Recipe
import com.kdjj.remote.dto.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteRecipeDao {
    
    override suspend fun uploadRecipe(recipe: Recipe): Unit =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipe.recipeId)
                .set(recipe.toEntity())
                .await()
        }
    
    override suspend fun increaseViewCount(recipe: Recipe): Unit =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipe.recipeId)
                .update("viewCount", recipe.viewCount + 1)
                .await()
        }
    
    companion object {
        
        const val RECIPE_COLLECTION_ID = "recipe"
    }
}

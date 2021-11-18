package com.kdjj.remote.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import com.kdjj.remote.dto.RecipeDto
import com.kdjj.remote.dto.toDomain
import com.kdjj.remote.dto.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

internal class RecipeServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteRecipeService {

    override suspend fun uploadRecipe(recipe: Recipe): Unit =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipe.recipeId)
                .set(recipe.toDto())
                .await()
        }

    override suspend fun increaseViewCount(recipe: Recipe): Unit =
        withContext(Dispatchers.IO) {
            val documentRefer = firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipe.recipeId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(documentRefer)
                val newRecipeViewCount = snapshot.getLong("viewCount")!! + 1
                transaction.update(documentRefer, "viewCount", newRecipeViewCount)
            }.await()
        }

    override suspend fun deleteRecipe(recipe: Recipe): Unit =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipe.recipeId)
                .delete()
                .await()
        }

    override suspend fun fetchRecipe(recipeID: String): Recipe =
        suspendCancellableCoroutine {
            firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipeID)
                .get()
                .addOnSuccessListener { recipeFireStore ->
                    if (recipeFireStore.metadata.isFromCache) {
                        it.resumeWithException(NetworkException())
                    } else {
                        val recipe = recipeFireStore.toObject<RecipeDto>()?.toDomain()
                        recipe?.let { item ->
                            it.resumeWith(Result.success(item))
                        } ?: it.resumeWithException(ApiException())
                    }
                }
                .addOnFailureListener { exception ->
                    it.resumeWithException(ApiException(exception.message))
                }
        }

    companion object {

        const val RECIPE_COLLECTION_ID = "recipe"
    }
}

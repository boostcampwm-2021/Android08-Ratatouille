package com.kdjj.remote.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.remote.dto.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteRecipeService {

    override suspend fun uploadRecipe(recipeDto: RecipeDto): Unit =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipeDto.recipeId)
                .set(recipeDto)
                .await()
        }

    override suspend fun increaseViewCount(recipeDto: RecipeDto): Unit =
        withContext(Dispatchers.IO) {
            val documentRefer = firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipeDto.recipeId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(documentRefer)
                val newRecipeViewCount = snapshot.getLong("viewCount")!! + 1
                transaction.update(documentRefer, "viewCount", newRecipeViewCount)
            }.await()
        }

    override suspend fun deleteRecipe(recipeDto: RecipeDto): Unit =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipeDto.recipeId)
                .delete()
                .await()
        }

    override suspend fun fetchRecipe(recipeId: String): RecipeDto =
        withContext(Dispatchers.IO) {
            val documentSnapShot = firestore.collection(RECIPE_COLLECTION_ID)
                .document(recipeId)
                .get(Source.SERVER)
                .await()
            documentSnapShot.toObject<RecipeDto>() ?: throw Exception()
        }

    companion object {

        const val RECIPE_COLLECTION_ID = "recipe"
    }
}

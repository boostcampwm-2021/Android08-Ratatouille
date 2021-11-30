package com.kdjj.remote.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.remote.dto.RecipeTypeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecipeTypeServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RecipeTypeService {

    override suspend fun fetchRecipeTypes(): List<RecipeTypeDto> =
        withContext(Dispatchers.IO) {
            firestore.collection(RECIPE_TYPE_COLLECTION_ID)
                .get()
                .await()
                .map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject<RecipeTypeDto>()
                }
        }

    companion object {

        const val RECIPE_TYPE_COLLECTION_ID = "recipeType"
    }
}

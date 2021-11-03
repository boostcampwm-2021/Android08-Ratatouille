package com.kdjj.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.entity.RecipeTypeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecipeRemoteDataSourceImpl(
	private val database: FirebaseFirestore,
	private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRemoteDataSource {

	override suspend fun fetchRecipeTypes(): Result<List<RecipeType>> =
		withContext(dispatcher) {
			kotlin.runCatching {
				database.collection("tesT")
					.get()
					.await()
					.map { queryDocumentSnapshot ->
						entityToDomain(queryDocumentSnapshot.toObject<RecipeTypeEntity>())
					}
			}
		}

	companion object {

		const val RECIPE_TYPE_COLLECTION_ID = "recipeType"
	}
}
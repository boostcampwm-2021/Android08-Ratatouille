package com.kdjj.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.entity.RecipeTypeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreDaoImpl @Inject constructor(
	private val firestore: FirebaseFirestore
) : FirestoreDao {
	
	override suspend fun fetchRecipeTypes(): List<RecipeType> =
		withContext(Dispatchers.IO) {
			firestore.collection(RECIPE_TYPE_COLLECTION_ID)
				.get()
				.await()
				.map { queryDocumentSnapshot ->
					entityToDomain(queryDocumentSnapshot.toObject<RecipeTypeEntity>())
				}
		}
	
	companion object {
		
		const val RECIPE_TYPE_COLLECTION_ID = "recipeType"
	}
}
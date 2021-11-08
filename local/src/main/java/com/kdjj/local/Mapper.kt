package com.kdjj.local

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeType
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity
import com.kdjj.local.model.RecipeTypeEntity

fun Recipe.toEntity(): RecipeMetaEntity {
	return RecipeMetaEntity(
		recipeId,
		title,
		stuff,
		imgPath,
		authorId,
		isFavorite,
		createTime,
		state,
		type.id.toLong()
	)
}

fun RecipeStep.toEntity(recipeMetaID: String, order: Int): RecipeStepEntity {
	return RecipeStepEntity(
		stepId,
		name,
		order,
		type,
		description,
		imgPath,
		seconds,
		recipeMetaID
	)
}

fun RecipeTypeEntity.toDomain(): RecipeType {
	return RecipeType(
		recipeTypeId.toInt(),
		title
	)
}

fun RecipeType.toEntity(): RecipeTypeEntity {
	return RecipeTypeEntity(
		id.toLong(),
		title
	)
}

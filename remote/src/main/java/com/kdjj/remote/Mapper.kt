package com.kdjj.remote

import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.entity.RecipeTypeEntity

fun entityToDomain(recipeTypeEntity: RecipeTypeEntity): RecipeType {
	return RecipeType(
		recipeTypeEntity.id,
		recipeTypeEntity.title
	)
}
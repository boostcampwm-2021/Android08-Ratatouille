package com.kdjj.remote

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeType
import com.kdjj.remote.entity.RecipeEntity
import com.kdjj.remote.entity.RecipeStepEntity
import com.kdjj.remote.entity.RecipeTypeEntity

fun entityToDomain(recipeTypeEntity: RecipeTypeEntity): RecipeType {
	return RecipeType(
		recipeTypeEntity.id,
		recipeTypeEntity.title
	)
}

fun entityToDomain(recipeEntity: RecipeEntity): Recipe {
	return Recipe(
		recipeEntity.recipeId,
		recipeEntity.title,
		entityToDomain(recipeEntity.type),
		recipeEntity.stuff,
		recipeEntity.imgPath,
		recipeEntity.stepList.map { entityToDomain(it) },
		recipeEntity.authorId,
		recipeEntity.viewCount,
		false,
		recipeEntity.createTime,
		recipeEntity.state
	)
}

fun entityToDomain(recipeStepEntity: RecipeStepEntity): RecipeStep {
	return RecipeStep(
		recipeStepEntity.stepId,
		recipeStepEntity.name,
		recipeStepEntity.type,
		recipeStepEntity.description,
		recipeStepEntity.imgPath,
		recipeStepEntity.seconds
	)
}


package com.kdjj.local

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity

fun domainToEntity(recipe: Recipe): RecipeMetaEntity {
    return RecipeMetaEntity(
        recipe.recipeId,
        recipe.title,
        recipe.stuff,
        recipe.imgPath,
        recipe.authorId,
        recipe.isFavorite,
        recipe.createTime,
        recipe.state,
        recipe.type.id.toLong()
    )
}

fun domainToEntity(step: RecipeStep, recipeMetaID: String, order: Int): RecipeStepEntity {
    return RecipeStepEntity(
        step.stepId,
        step.name,
        order,
        step.type,
        step.description,
        step.imgPath,
        step.seconds,
        recipeMetaID
    )
}
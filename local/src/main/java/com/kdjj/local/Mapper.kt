package com.kdjj.local

import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity

fun domainToEntity(recipe: Recipe): RecipeMetaEntity {
    return RecipeMetaEntity(
        null,
        recipe.title,
        recipe.stuff,
        recipe.imgPath,
        recipe.type.id.toLong()
    )
}

fun domainToEntity(step: RecipeStep, recipeMetaID: Long): RecipeStepEntity {
     return RecipeStepEntity(
        null,
         step.name,
         step.type,
         step.description,
         step.imgPath,
         step.seconds,
         recipeMetaID
    )
}
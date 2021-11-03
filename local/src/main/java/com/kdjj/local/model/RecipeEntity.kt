package com.kdjj.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeEntity(
    @Embedded val recipeMeta: RecipeMetaEntity,

    @Relation(
        parentColumn = "recipeTypeId",
        entityColumn = "recipeTypeId"
    )
    val recipeType: RecipeTypeEntity,

    @Relation(
        parentColumn = "recipeMetaId",
        entityColumn = "parentRecipeId"
    )
    val steps: List<RecipeStepEntity>
)

package com.kdjj.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.kdjj.domain.model.Recipe
import com.kdjj.local.toDomain

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

fun RecipeEntity.toDomain(): Recipe =
    Recipe(
        recipeId = recipeMeta.recipeMetaId,
        title = recipeMeta.title,
        type = recipeType.toDomain(),
        stuff = recipeMeta.stuff,
        imgPath = recipeMeta.imgPath,
        stepList = steps.map { it.toDomain() },
        authorId = recipeMeta.authorId,
        viewCount = 0,
        isFavorite = recipeMeta.isFavorite,
        createTime = recipeMeta.createTime,
        state = recipeMeta.state
    )

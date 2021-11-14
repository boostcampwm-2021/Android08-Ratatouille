package com.kdjj.local.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.RecipeStepType

@Entity(
    tableName = "RecipeStep",
    foreignKeys = [
        ForeignKey(
            entity = RecipeMetaEntity::class,
            parentColumns = arrayOf("recipeMetaId"),
            childColumns = arrayOf("parentRecipeId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class RecipeStepEntity(
    @PrimaryKey
    val stepId: String,
    val name: String,
    val order: Int,
    val type: RecipeStepType,
    val description: String,
    var imgPath: String,
    val seconds: Int,
    val parentRecipeId: String,
)

internal fun RecipeStepEntity.toDomain() =
    RecipeStep(
        stepId,
        name,
        type,
        description,
        imgPath,
        seconds
    )

internal fun RecipeStep.toEntity(recipeMetaID: String, order: Int): RecipeStepEntity =
    RecipeStepEntity(
        stepId,
        name,
        order,
        type,
        description,
        imgPath,
        seconds,
        recipeMetaID
    )

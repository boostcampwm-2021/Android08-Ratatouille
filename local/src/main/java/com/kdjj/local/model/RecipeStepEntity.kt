package com.kdjj.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.local.model.RecipeMetaEntity

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
data class RecipeStepEntity(
    @PrimaryKey(autoGenerate = true)
    val stepId: Long? = null,
    val name: String,
    val type: RecipeStepType,
    val description: String,
    var imgPath: String,
    val seconds: Int,
    val parentRecipeId: Long,
)

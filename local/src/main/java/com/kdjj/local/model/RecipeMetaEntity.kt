package com.kdjj.local.model

import androidx.room.*

// https://developer.android.com/training/data-storage/room/relationships
@Entity(
    tableName = "RecipeMeta",
    foreignKeys = [
        ForeignKey(
            entity = RecipeTypeEntity::class,
            parentColumns = arrayOf("recipeTypeId"),
            childColumns = arrayOf("recipeTypeId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeMetaEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeMetaId: Long? = null,
    val title: String,
    val stuff: String,
    var imgPath: String,
    val recipeTypeId: Long
)



package com.kdjj.local

import androidx.room.*

// https://developer.android.com/training/data-storage/room/relationships
@Entity(tableName = "RecipeMeta")
data class RecipeMetaEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeMetaId: Int? = null,
    val title: String,
    val stuff: String,
    var imgPath: String,
)



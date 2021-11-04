package com.kdjj.local.model

import androidx.room.*

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
    @PrimaryKey
    val recipeMetaId: String,
    val title: String,
    val stuff: String,
    var imgPath: String,
    val uploaderId:String?,
    val isFavorite:  Boolean,
    val createTime: Long,
    val recipeTypeId: Long
)



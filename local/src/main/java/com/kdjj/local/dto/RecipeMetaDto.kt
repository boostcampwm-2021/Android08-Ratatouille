package com.kdjj.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState

@Entity(
    tableName = "RecipeMeta",
    foreignKeys = [
        ForeignKey(
            entity = RecipeTypeDto::class,
            parentColumns = arrayOf("recipeTypeId"),
            childColumns = arrayOf("recipeTypeId"),
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
internal data class RecipeMetaDto(
    @PrimaryKey
    val recipeMetaId: String,
    val title: String,
    val stuff: String,
    var imgPath: String,
    val authorId: String,
    val isFavorite: Boolean,
    val createTime: Long,
    val state: RecipeState,
    @ColumnInfo(defaultValue = "1")
    val recipeTypeId: Long
)

internal fun Recipe.toDto(): RecipeMetaDto =
    RecipeMetaDto(
        recipeId,
        title,
        stuff,
        imgPath,
        authorId,
        isFavorite,
        createTime,
        state,
        type.id.toLong()
    )

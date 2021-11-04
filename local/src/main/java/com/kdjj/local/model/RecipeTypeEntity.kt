package com.kdjj.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kdjj.domain.model.RecipeType
import com.kdjj.local.model.RecipeMetaEntity

@Entity(tableName = "RecipeType")
data class RecipeTypeEntity(
    @PrimaryKey
    val recipeTypeId: Long,
    val title: String
){
    companion object{
        val defaultTypes = mutableListOf<RecipeTypeEntity>(
            RecipeTypeEntity(1, "한식"),
            RecipeTypeEntity(2, "중식"),
            RecipeTypeEntity(3, "양식"),
            RecipeTypeEntity(4, "일식"),
            RecipeTypeEntity(5, "기타"),
        )
    }
}

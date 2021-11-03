package com.kdjj.local

import androidx.room.*

@Dao
interface RecipeDAO {
    //RecipeMeta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeMeta(recipeMeta: RecipeMetaEntity): Long

    //RecipeType
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeType(recipeType: RecipeTypeEntity)

    //RecipeStep
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeStep(recipeStep: RecipeStepEntity)

    @Transaction
    @Query("SELECT * FROM recipeMeta")
    fun getAllRecipe(): List<RecipeEntity>
}
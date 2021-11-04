package com.kdjj.local.DAO

import androidx.room.*
import com.kdjj.local.model.RecipeEntity
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity
import com.kdjj.local.model.RecipeTypeEntity

@Dao
interface RecipeDAO {
    //RecipeMeta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeMeta(recipeMeta: RecipeMetaEntity)

    //RecipeType
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeType(recipeType: RecipeTypeEntity)

    @Query("SELECT * FROM RecipeType")
    fun getAllRecipeTypes(): List<RecipeTypeEntity>

    //RecipeStep
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipeStep(recipeStep: RecipeStepEntity)

    @Transaction
    @Query("SELECT * FROM RecipeMeta")
    fun getAllRecipe(): List<RecipeEntity>
}
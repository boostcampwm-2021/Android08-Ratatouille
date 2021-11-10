package com.kdjj.local.DAO

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kdjj.local.model.RecipeEntity

@Dao
interface RecipeListDao {

    @Transaction
    @Query("SELECT * FROM RecipeMeta ORDER BY createTime LIMIT :pageSize OFFSET :page")
    suspend fun fetchLatestRecipeList(pageSize: Int, page: Int): List<RecipeEntity>

    @Transaction
    @Query("SELECT * FROM RecipeMeta WHERE isFavorite = :favorite ORDER BY createTime LIMIT :pageSize OFFSET :index")
    suspend fun fetchFavoriteRecipeList(pageSize: Int, index: Int, favorite:Boolean = true): List<RecipeEntity>
}
package com.kdjj.local.dataSource

import com.kdjj.data.common.errorMap
import com.kdjj.data.datasource.RecipeListLocalDataSource
import com.kdjj.domain.model.Recipe
import com.kdjj.local.DAO.RecipeListDao
import com.kdjj.local.model.toDomain
import java.lang.Exception
import javax.inject.Inject

internal class RecipeListLocalDataSourceImpl @Inject constructor(
    private val recipeListDao: RecipeListDao,
) : RecipeListLocalDataSource {

    override suspend fun fetchLatestRecipeListAfter(page: Int): Result<List<Recipe>> =
        runCatching {
            recipeListDao.fetchLatestRecipeList(PAGE_SIZE, page)
                .map { it.toDomain() }
        }.errorMap { throwable ->
            throwable?.let {
                Exception(it.message)
            } ?: Exception()
        }

    override suspend fun fetchFavoriteRecipeListAfter(index: Int): Result<List<Recipe>> =
        runCatching {
            recipeListDao.fetchFavoriteRecipeList(PAGE_SIZE, index)
                .map { it.toDomain() }
        }.errorMap { throwable ->
            throwable?.let {
                Exception(it.message)
            } ?: Exception()
        }

    override suspend fun fetchSearchRecipeListAfter(
        keyword: String,
        index: Int
    ): Result<List<Recipe>> =
        runCatching {
            recipeListDao.fetchSearchRecipeList(PAGE_SIZE, keyword, index)
                .map { it.toDomain() }
        }.errorMap { throwable ->
            throwable?.let {
                Exception(it.message)
            } ?: Exception()
        }

    companion object {
        const val PAGE_SIZE = 10
    }
}
package com.kdjj.local.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.kdjj.domain.model.RecipeStepType
import com.kdjj.local.DAO.RecipeDAO
import com.kdjj.local.database.RecipeDatabase
import com.kdjj.local.model.RecipeEntity
import com.kdjj.local.model.RecipeMetaEntity
import com.kdjj.local.model.RecipeStepEntity
import com.kdjj.local.model.RecipeTypeEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class RecipeDaoTest {

    private lateinit var database: RecipeDatabase
    private lateinit var recipeDao: RecipeDAO

    private val recipeType = RecipeTypeEntity(1, "한식")

    private val recipeMeta = RecipeMetaEntity(
        "recipeID01",
        "test",
        "apple, banana, orange",
        "image path sample",
        null,
        false,
        2000,
        1
    )

    private val recipeStep = RecipeStepEntity(
        "recipeStepID01",
        "test step 1",
        1,
        RecipeStepType.FRY,
        "this is recipe test sample 1",
        "image path sample 1",
        300,
        "recipeID01"
    )

    private val recipe = RecipeEntity(
        recipeMeta,
        recipeType,
        listOf(recipeStep)
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RecipeDatabase::class.java
        ).allowMainThreadQueries().build()

        recipeDao = database.getRecipeDao()
    }

    @Test
    fun insertRecipeType_insertRecipeTpeAndGetAllRecipeType_true() = runBlocking {
        recipeDao.insertRecipeType(recipeType)

        val recipeTypes = recipeDao.getAllRecipeTypes()

        assertThat(recipeTypes).contains(recipeType)
    }

    @Test
    fun insertRecipeMeta_insertRecipeMetaAndGetAllRecipeMeta_true() = runBlocking {
        recipeDao.insertRecipeType(recipeType)
        recipeDao.insertRecipeMeta(recipeMeta)
        recipeDao.insertRecipeStep(recipeStep)

        val recipeList = recipeDao.getAllRecipe()

        assertThat(recipeList[0].recipeMeta).isEqualTo(recipeMeta)
    }

    @Test
    fun insertRecipeStep_insertRecipeStepAndGetAllRecipeStep_true() = runBlocking {
        recipeDao.insertRecipeType(recipeType)
        recipeDao.insertRecipeMeta(recipeMeta)
        recipeDao.insertRecipeStep(recipeStep)

        val recipeList = recipeDao.getAllRecipe()

        assertThat(recipeList[0].steps).contains(recipeStep)
    }

    @Test
    fun getAllRecipe_insertRecipeAndGetAllRecipe_true() = runBlocking {
        recipeDao.insertRecipeType(recipeType)
        recipeDao.insertRecipeMeta(recipeMeta)
        recipeDao.insertRecipeStep(recipeStep)

        val recipeList = recipeDao.getAllRecipe()

        assertThat(recipeList).contains(recipe)
    }

    @After
    fun teardown() {
        database.close()
    }
}
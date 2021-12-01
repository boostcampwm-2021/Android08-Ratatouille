package com.kdjj.presentation.viewmodel.recipedetail

import android.media.Ringtone
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.request.FetchOthersRecipeRequest
import com.kdjj.domain.model.request.GetMyRecipeRequest
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.Notifications
import com.kdjj.presentation.viewmodel.common.MainCoroutineRule
import com.kdjj.presentation.viewmodel.common.getDummyRecipeList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Exception

class RecipeDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val mockRingtone = mock(Ringtone::class.java)
    private val mockGetMyRecipeUseCase = mock(ResultUseCase::class.java) as ResultUseCase<GetMyRecipeRequest, Recipe>
    private val mockFetchOthersRecipeUseCase = mock(ResultUseCase::class.java) as ResultUseCase<FetchOthersRecipeRequest, Recipe>
    private val mockNotifications = mock(Notifications::class.java)

    private lateinit var viewModel: RecipeDetailViewModel
    private val dummyRecipe = getDummyRecipeList()[0]

    @Before
    fun setUp() {
        viewModel = RecipeDetailViewModel(mockRingtone, mockGetMyRecipeUseCase, mockFetchOthersRecipeUseCase, mockNotifications)
    }

    @Test
    fun initializeWith_networkStateAndOnSuccess_updatedLiveData(): Unit = runBlocking {
        //given
        val state = RecipeState.NETWORK
        `when`(mockFetchOthersRecipeUseCase(FetchOthersRecipeRequest(dummyRecipe.recipeId))).thenReturn(
            Result.success(
                dummyRecipe
            )
        )

        //when
        viewModel.initializeWith(dummyRecipe.recipeId, state)

        //then
        assertEquals(dummyRecipe.stepList, viewModel.liveStepList.value)
        assertEquals(dummyRecipe.title, viewModel.liveTitle.value)
        assertEquals(dummyRecipe.stepList.first(), viewModel.liveStepBarSelected.value)
        assertEquals(false, viewModel.liveLoading.value)
    }

    @Test
    fun initializeWith_networkStateAndOnFailure_updatedEvent(): Unit = runBlocking {
        //given
        val state = RecipeState.NETWORK
        `when`(mockFetchOthersRecipeUseCase(FetchOthersRecipeRequest(dummyRecipe.recipeId))).thenReturn(
            Result.failure(
                Exception("Test Exception")
            )
        )

        //when
        viewModel.initializeWith(dummyRecipe.recipeId, state)

        //then
        assertEquals(RecipeDetailViewModel.RecipeDetailEvent.Error, viewModel.eventRecipeDetail.value?.getContentIfNotHandled())
    }

    @Test
    fun initializeWith_createStateAndOnSuccess_updatedLiveData(): Unit = runBlocking {
        //given
        val state = RecipeState.CREATE
        `when`(mockGetMyRecipeUseCase(GetMyRecipeRequest(dummyRecipe.recipeId))).thenReturn(
            Result.success(
                dummyRecipe
            )
        )

        //when
        viewModel.initializeWith(dummyRecipe.recipeId, state)

        //then
        assertEquals(dummyRecipe.stepList, viewModel.liveStepList.value)
        assertEquals(dummyRecipe.title, viewModel.liveTitle.value)
        assertEquals(dummyRecipe.stepList.first(), viewModel.liveStepBarSelected.value)
        assertEquals(false, viewModel.liveLoading.value)
    }

    @Test
    fun initializeWith_createStateAndOnFailure_updatedEvent(): Unit = runBlocking {
        //given
        val state = RecipeState.CREATE
        `when`(mockGetMyRecipeUseCase(GetMyRecipeRequest(dummyRecipe.recipeId))).thenReturn(
            Result.failure(
                Exception("Test Exception")
            )
        )

        //when
        viewModel.initializeWith(dummyRecipe.recipeId, state)

        //then
        assertEquals(RecipeDetailViewModel.RecipeDetailEvent.Error, viewModel.eventRecipeDetail.value?.getContentIfNotHandled())
    }

    @Test
    fun scrollToStep() {

    }

    @Test
    fun updateCurrentStepTo() {
    }

    @Test
    fun toggleTimer() {
    }

    @Test
    fun moveTimer() {
    }

    @Test
    fun removeTimerAt() {
    }

    @Test
    fun setBackground() {
    }

    @Test
    fun onCleared() {
    }
}
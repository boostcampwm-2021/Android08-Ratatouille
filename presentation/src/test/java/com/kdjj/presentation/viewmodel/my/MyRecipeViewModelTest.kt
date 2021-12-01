package com.kdjj.presentation.viewmodel.my

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.request.EmptyRequest
import com.kdjj.domain.model.request.FetchMyFavoriteRecipeListRequest
import com.kdjj.domain.model.request.FetchMyLatestRecipeListRequest
import com.kdjj.domain.model.request.FetchMyTitleRecipeListRequest
import com.kdjj.domain.usecase.FlowUseCase
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.model.SortType
import com.kdjj.presentation.viewmodel.common.MainCoroutineRule
import com.kdjj.presentation.viewmodel.common.getDummyRecipeList
import com.kdjj.presentation.viewmodel.common.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class MyRecipeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val mockLatestRecipeUseCase = mock(ResultUseCase::class.java) as ResultUseCase<FetchMyLatestRecipeListRequest, List<Recipe>>
    private val mockFavoriteRecipeUseCase = mock(ResultUseCase::class.java) as ResultUseCase<FetchMyFavoriteRecipeListRequest, List<Recipe>>
    private val mockTitleRecipeUseCase = mock(ResultUseCase::class.java) as ResultUseCase<FetchMyTitleRecipeListRequest, List<Recipe>>
    private val mockGetRecipeUpdateFlowUseCase = mock(FlowUseCase::class.java) as FlowUseCase<EmptyRequest, Int>
    private val mockDeleteUselessImageFileUseCase = mock(ResultUseCase::class.java) as ResultUseCase<EmptyRequest, Unit>

    private lateinit var viewModel: MyRecipeViewModel
    private val testRecipeFlow = MutableStateFlow(0)

    @Before
    fun setUp(): Unit = runBlocking {
        `when`(mockDeleteUselessImageFileUseCase(EmptyRequest)).thenReturn(Result.success(Unit))
        `when`(mockGetRecipeUpdateFlowUseCase(EmptyRequest)).thenReturn(testRecipeFlow)
        `when`(mockLatestRecipeUseCase(FetchMyLatestRecipeListRequest(0))).thenReturn(
            Result.success(
                getDummyRecipeList()
            )
        )
        `when`(mockTitleRecipeUseCase(FetchMyTitleRecipeListRequest(0))).thenReturn(
            Result.success(
                getDummyRecipeList()
            )
        )
        viewModel = MyRecipeViewModel(mockLatestRecipeUseCase, mockFavoriteRecipeUseCase,
            mockTitleRecipeUseCase, mockGetRecipeUpdateFlowUseCase, mockDeleteUselessImageFileUseCase)
    }

    @Test
    fun setSortType_typeToName_liveSortTypeChanged(): Unit = runBlocking {
        //given
        viewModel.setSortType(SortType.SORT_BY_TIME)
        //when
        viewModel.setSortType(SortType.SORT_BY_NAME)
        //then
        assertEquals(SortType.SORT_BY_NAME, viewModel.liveSortType.value)
    }

    @Test
    fun refreshRecipeList_callAfter_FetchOnlyOne(): Unit = runBlocking {
        //given
        viewModel.setSortType(SortType.SORT_BY_NAME)
        //when
        viewModel.refreshRecipeList()
        //then
        verify(mockTitleRecipeUseCase, times(2)).invoke(FetchMyTitleRecipeListRequest(0))
    }

    @Test
    fun fetchRecipeList_callAfter_liveDataUpdated(): Unit = runBlocking {
        //given
        viewModel.setSortType(SortType.SORT_BY_NAME)

        //when
        viewModel.fetchRecipeList(0)

        //then
        verify(mockTitleRecipeUseCase, times(2)).invoke(FetchMyTitleRecipeListRequest(0))
        assertEquals(true, viewModel.liveRecipeItemList.getOrAwaitValue().isNotEmpty())
    }

    @Test
    fun recipeItemSelected() {
    }

    @Test
    fun moveToRecipeEditorActivity() {
    }

    @Test
    fun moveToRecipeSearchFragment() {
    }
}
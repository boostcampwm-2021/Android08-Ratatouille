package com.kdjj.presentation.viewmodel.others

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kdjj.domain.model.*
import com.kdjj.domain.model.request.FetchOthersLatestRecipeListRequest
import com.kdjj.domain.model.request.FetchOthersPopularRecipeListRequest
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.viewmodel.common.MainCoroutineRule
import com.kdjj.presentation.viewmodel.common.getDummyRecipeList
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class OthersViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val mockFetchOthersLatestRecipeListUseCase = mock(ResultUseCase::class.java) as ResultUseCase<FetchOthersLatestRecipeListRequest, @JvmSuppressWildcards List<Recipe>>
    private val mockFetchOthersFavoriteRecipeListUseCase = mock(ResultUseCase::class.java) as ResultUseCase<FetchOthersPopularRecipeListRequest, @JvmSuppressWildcards List<Recipe>>
    private lateinit var viewModel: OthersViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {

        TestCoroutineScope(mainCoroutineRule.coroutineContext).launch {
            `when`(mockFetchOthersLatestRecipeListUseCase(
                FetchOthersLatestRecipeListRequest(true))
            ).thenReturn(
                Result.success(
                    getDummyRecipeList()
                )
            )
            `when`(mockFetchOthersFavoriteRecipeListUseCase(
                FetchOthersPopularRecipeListRequest(true))
            ).thenReturn(
                Result.success(
                    getDummyRecipeList()
                )
            )

            viewModel = OthersViewModel(mockFetchOthersLatestRecipeListUseCase, mockFetchOthersFavoriteRecipeListUseCase)
        }
    }
}
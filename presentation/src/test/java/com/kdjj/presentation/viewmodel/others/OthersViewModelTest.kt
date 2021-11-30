package com.kdjj.presentation.viewmodel.others

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kdjj.domain.model.*
import com.kdjj.domain.model.request.FetchOthersLatestRecipeListRequest
import com.kdjj.domain.model.request.FetchOthersPopularRecipeListRequest
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.viewmodel.common.MainCoroutineRule
import com.kdjj.presentation.viewmodel.common.getDummyRecipeList
import io.reactivex.rxjava3.observers.TestObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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

    @Test
    fun setChecked_typeToPopular_liveSortTypeChanged() {
        //given
        viewModel.setChecked(OthersViewModel.OthersSortType.LATEST)
        //when
        viewModel.setChecked(OthersViewModel.OthersSortType.POPULAR)
        //then
        assertEquals(OthersViewModel.OthersSortType.POPULAR, viewModel.liveSortType.value)
    }

    @Test
    fun fetchNextRecipeListPage() {
    }

    @Test
    fun moveToRecipeSearchFragment_subscribeOthersSubject_equalsValueSearchIconClicked() {
        //given
        val testObserver = TestObserver<OthersViewModel.ButtonClick>()
        viewModel.othersSubject
            .subscribe(testObserver)

        //when
        viewModel.moveToRecipeSearchFragment()
        viewModel.othersSubject.onComplete()

        //then
        //assertResult :
        //assertSubscribed(), assertValues(values) ,assertNoErrors(), assertComplete()를 연속으로 호출합니다.
        testObserver.assertResult(OthersViewModel.ButtonClick.SearchIconClicked)
    }

    @Test
    fun recipeItemClick() {
    }
}
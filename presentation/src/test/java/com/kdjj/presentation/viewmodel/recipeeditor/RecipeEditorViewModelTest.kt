package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kdjj.domain.model.RecipeType
import com.kdjj.domain.request.EmptyRequest
import com.kdjj.domain.request.RecipeRequest
import com.kdjj.domain.usecase.FetchRecipeTypesUseCase
import com.kdjj.domain.usecase.UseCase
import com.kdjj.presentation.common.IdGenerator
import com.kdjj.presentation.common.RecipeMapper
import com.kdjj.presentation.common.RecipeStepValidator
import com.kdjj.presentation.common.RecipeValidator
import com.kdjj.presentation.model.RecipeEditorItem
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.mock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import org.mockito.Mockito.`when`
import kotlin.coroutines.ContinuationInterceptor

@RunWith(Parameterized::class) // 반복적 테스트
class RecipeEditorViewModelTest(
    private val stepSize: Int,
    private val removeStepPosition: Int,
    private val moveStepFrom: Int,
    private val moveStepTo: Int,
) {

    companion object{
        //반복적 테스트를 위한 파라미터 생성
        @JvmStatic
        @Parameterized.Parameters
        fun data() : Collection<Array<Any>>{
            return listOf(
                arrayOf(5, 1, 2, 3),
                arrayOf(5, 4, 3, 2),
                arrayOf(5, 3, 2, 1),
            )
        }
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var saveRecipeUseCase: UseCase<RecipeRequest, Boolean>
    lateinit var fetchRecipeTypesUseCase: FetchRecipeTypesUseCase
    lateinit var recipeValidator: RecipeValidator
    lateinit var recipeStepValidator: RecipeStepValidator
    lateinit var recipeMapper: RecipeMapper
    lateinit var idGnerator: IdGenerator
    lateinit var viewModel: RecipeEditorViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        fetchRecipeTypesUseCase = mock(FetchRecipeTypesUseCase::class.java)
        recipeValidator = mock(RecipeValidator::class.java)
        recipeStepValidator = mock(RecipeStepValidator::class.java)
        recipeMapper = mock(RecipeMapper::class.java)
        idGnerator = mock(IdGenerator::class.java)
        saveRecipeUseCase = object : UseCase<RecipeRequest, Boolean> {
            override suspend fun invoke(request: RecipeRequest): Result<Boolean> =
                Result.success(true)
        }
        `when`(idGnerator.getDeviceId()).thenReturn("did")
        `when`(idGnerator.generateId()).thenReturn("id")

        TestCoroutineScope(mainCoroutineRule.coroutineContext).launch {
            `when`(fetchRecipeTypesUseCase.invoke(EmptyRequest())).thenReturn(Result.success(listOf(RecipeType(0, "중식"))))
            viewModel = RecipeEditorViewModel(recipeValidator, recipeStepValidator, saveRecipeUseCase, fetchRecipeTypesUseCase, recipeMapper, idGnerator)
//            viewModel.fetchRecipeTypes()
            repeat(stepSize) {
                viewModel.addRecipeStep()
            }
        }
    }

    @Test
    fun titleSwitchMap_titleSetValue_titleStateChanged() {
        `when`(recipeValidator.validateTitle("hi")).thenReturn(true)
        val recipeModel = viewModel.liveRecipeItemList.value!![0] as RecipeEditorItem.RecipeMetaModel
        recipeModel.liveTitle.value = "hi"
        assertEquals(
            recipeModel.liveTitleState.getOrAwaitValue(),
            true
        )
    }

    @Test
    fun stepSwitchMap_stepNameSetValue_stepNameStateChanged() {
        `when`(recipeStepValidator.validateName("h")).thenReturn(true)
        if (stepSize > 0) {
            val recipeModel = viewModel.liveRecipeItemList.value!![1] as RecipeEditorItem.RecipeStepModel
            recipeModel.liveName.value = "h"
            assertEquals(
                recipeModel.liveNameState.getOrAwaitValue(),
                true
            )
        }
    }

    @Test
    fun stepRemove_removeOne_stepSizeDecreased() {
        viewModel.removeRecipeStep(removeStepPosition)
        if (stepSize == 0) {
            assertEquals(0, viewModel.liveRecipeItemList.value?.size)
        } else {
            // meta item + default step + button item = 2
            assertEquals(stepSize + 3 - 1, viewModel.liveRecipeItemList.value?.size)
        }
    }

    @Test
    fun stepPositionChange_fromTo_fromEqualsTo() {
        if (stepSize != 0) {
            val from = viewModel.liveRecipeItemList.value!![moveStepFrom]
            viewModel.changeRecipeStepPosition(moveStepFrom, moveStepTo)
            val to = viewModel.liveRecipeItemList.value!![moveStepTo]
            assertEquals(from, to)
        }
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    try {
        afterObserve.invoke()
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}
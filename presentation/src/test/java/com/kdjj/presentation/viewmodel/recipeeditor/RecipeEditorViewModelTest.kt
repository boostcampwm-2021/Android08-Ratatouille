package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kdjj.domain.usecase.FetchRecipeTypesUseCase
import com.kdjj.domain.usecase.SaveRecipeUseCase
import com.kdjj.presentation.common.RecipeStepValidator
import com.kdjj.presentation.common.RecipeValidator
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.mock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import org.mockito.Mockito.`when`

@RunWith(Parameterized::class) // 반복적 테스트
class RecipeEditorViewModelTest(
    private val stepSize: Int,
    private val removeStepPosition: Int,
) {

    companion object{
        //반복적 테스트를 위한 파라미터 생성
        @JvmStatic
        @Parameterized.Parameters
        fun data() : Collection<Array<Any>>{
            return listOf(
                arrayOf(5, 0),
                arrayOf(5, 4),
                arrayOf(5, 3),
                arrayOf(1, 0),
                arrayOf(0, 0),
            )
        }
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var saveRecipeUseCase: SaveRecipeUseCase

    lateinit var fetchRecipeTypesUseCase: FetchRecipeTypesUseCase

    lateinit var recipeValidator: RecipeValidator

    lateinit var recipeStepValidator: RecipeStepValidator

    private lateinit var viewModel: RecipeEditorViewModel

    @Before
    fun setUp() {
        saveRecipeUseCase = mock(SaveRecipeUseCase::class.java)
        fetchRecipeTypesUseCase = mock(FetchRecipeTypesUseCase::class.java)
        recipeValidator = mock(RecipeValidator::class.java)
        recipeStepValidator = mock(RecipeStepValidator::class.java)
        viewModel = RecipeEditorViewModel(recipeValidator, recipeStepValidator)

        repeat(stepSize) {
            viewModel.addRecipeStep()
        }
    }

    @Test
    fun titleSwitchMap_titleSetValue_titleStateChanged() {
        `when`(recipeValidator.validateTitle("hi")).thenReturn(true)
        viewModel.liveTitle.value = "hi"
        assertEquals(
            viewModel.liveTitleState.getOrAwaitValue(),
            true
        )
    }

    @Test
    fun stepSwitchMap_stepNameSetValue_stepNameStateChanged() {
        `when`(recipeStepValidator.validateName("h")).thenReturn(true)
        if (stepSize > 0) {
            viewModel.liveStepList.value!![0].liveName.value = "h"
            assertEquals(
                viewModel.liveStepList.value!![0].liveNameState.getOrAwaitValue(),
                true
            )
        }
    }

    @Test
    fun stepRemove_removeOne_stepSizeDecreased() {
        viewModel.removeRecipeStep(removeStepPosition)
        if (stepSize == 0) {
            assertEquals(0, viewModel.liveStepList.value?.size)
        } else {
            assertEquals(stepSize - 1, viewModel.liveStepList.value?.size)
        }
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
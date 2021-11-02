package com.kdjj.presentation.viewmodel.recipeeditor

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kdjj.domain.usecase.FetchRecipeTypesUseCase
import com.kdjj.domain.usecase.SaveRecipeUseCase
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class RecipeEditorViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var saveRecipeUseCase: SaveRecipeUseCase

    lateinit var fetchRecipeTypesUseCase: FetchRecipeTypesUseCase

    private lateinit var viewModel: RecipeEditorViewModel

    @Before
    fun setUp() {
        saveRecipeUseCase = mock(SaveRecipeUseCase::class.java)
        fetchRecipeTypesUseCase = mock(FetchRecipeTypesUseCase::class.java)
        viewModel = RecipeEditorViewModel(fetchRecipeTypesUseCase, saveRecipeUseCase)
    }

    @Test
    fun titleSwitchMap_titleSetValue_titleStateChanged() {
        assertEquals(
            viewModel.titleState.getOrAwaitValue {
                viewModel.title.value = "hi"
            },
            true
        )
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
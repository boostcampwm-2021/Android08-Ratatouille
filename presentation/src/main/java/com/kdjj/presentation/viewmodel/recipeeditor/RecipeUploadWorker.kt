package com.kdjj.presentation.viewmodel.recipeeditor

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kdjj.domain.model.exception.NotExistRecipeException
import com.kdjj.domain.model.request.UpdateUploadedRecipeRequest
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.UPDATED_RECIPE_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RecipeUploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val updateUploadedRecipeUseCase: ResultUseCase<UpdateUploadedRecipeRequest, Unit>,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val updatedRecipeId = inputData.getString(UPDATED_RECIPE_ID) ?: return Result.failure()
        val result = updateUploadedRecipeUseCase(UpdateUploadedRecipeRequest(updatedRecipeId))
            .onFailure {
                when (it) {
                    is NotExistRecipeException -> {
                        return Result.failure()
                    }
                }
            }
        return if (result.isSuccess) Result.success() else Result.retry()
    }
}

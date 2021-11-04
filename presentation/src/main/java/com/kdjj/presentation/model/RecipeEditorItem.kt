package com.kdjj.presentation.model

import com.kdjj.domain.model.RecipeStepType
import com.kdjj.domain.model.RecipeType

sealed class RecipeEditorItem {

    data class RecipeMeta(
        var title: String,
        var type: RecipeType,
        var imgPath: String,
        var stuff: String
    ) : RecipeEditorItem()

    data class RecipeStep(
        var title: String,
        var type: RecipeStepType,
        var description: String,
        var timer: Int,
        var imgPath: String
    ) : RecipeEditorItem()

    object AddStep : RecipeEditorItem()
}
package com.kdjj.presentation.viewmodel.common

import com.kdjj.domain.model.*

fun getDummyRecipeList() = listOf<Recipe>(
    Recipe(
        recipeId = "01",
        title = "딸빙",
        stuff = "딸기 3개",
        state = RecipeState.CREATE,
        isFavorite = false,
        stepList = listOf(
            RecipeStep(
                stepId = "s01",
                name = "썰기",
                type = RecipeStepType.FRY,
                description = "썰어",
                imgPath = "contents/dfsdfsf.png",
                seconds = 10
            )
        ),
        viewCount = 0,
        authorId = "device01",
        imgPath = "contents/dsfsfdsf.png",
        createTime = System.currentTimeMillis(),
        type = RecipeType(1, "기타")
    )
)
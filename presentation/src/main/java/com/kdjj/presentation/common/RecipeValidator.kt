package com.kdjj.presentation.common

import javax.inject.Inject

class RecipeValidator @Inject constructor() {

    fun validateTitle(title: String): Boolean {
        return title.isNotBlank()
    }

    fun validateStuff(stuff: String): Boolean {
        return stuff.isNotBlank()
    }
}
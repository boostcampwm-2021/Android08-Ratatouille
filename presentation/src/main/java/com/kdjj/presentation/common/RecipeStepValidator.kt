package com.kdjj.presentation.common

import javax.inject.Inject

class RecipeStepValidator @Inject constructor() {

    fun validateName(name: String): Boolean {
        return name.isNotBlank()
    }

    fun validateDescription(description: String): Boolean {
        return description.isNotBlank()
    }

    fun validateMinutes(min: Int?): Boolean {
        return true
    }

    fun validateSeconds(sec: Int?): Boolean {
        return sec?.let { it in 0..59 } ?: true
    }
}
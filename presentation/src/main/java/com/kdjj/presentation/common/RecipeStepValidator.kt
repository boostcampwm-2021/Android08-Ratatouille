package com.kdjj.presentation.common

import javax.inject.Inject

class RecipeStepValidator @Inject constructor() {

    fun validateName(name: String): Boolean {
        // to-do
        return true
    }

    fun validateDescription(description: String): Boolean {
        // to-do
        return true
    }

    fun validateMinutes(min: Int): Boolean {
        // to-do
        return true
    }

    fun validateSeconds(sec: Int): Boolean {
        // to-do
        return true
    }
}
package com.kdjj.domain.model

interface Recipe {
    val title: String
    val type: Type
    val stuff: String
    val imgPath: String
    val stepList: List<Step>

    interface Step {
        val name: String
        val type: Type
        val description: String
        val imgPath: String
        val seconds: Int

        enum class Type {
            FRY,
        }
    }

    interface Type {
        val id: Int
        val title: String
    }
}


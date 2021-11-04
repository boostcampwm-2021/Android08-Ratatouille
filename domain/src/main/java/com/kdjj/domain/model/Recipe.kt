package com.kdjj.domain.model

data class Recipe(
	val recipeId: String,
	val title: String,
	val type: RecipeType,
	val stuff: String,
	val imgPath: String,
	val stepList: List<RecipeStep>,
	val uploaderId: String?,
	val viewCount: Int,
	val isFavorite: Boolean,
	val createTime: Long
)

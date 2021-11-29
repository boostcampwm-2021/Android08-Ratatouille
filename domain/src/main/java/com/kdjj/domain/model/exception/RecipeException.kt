package com.kdjj.domain.model.exception

class NotExistRecipeException(
    msg: String? = null
) : Exception(msg ?: "Not Exist Recipe")

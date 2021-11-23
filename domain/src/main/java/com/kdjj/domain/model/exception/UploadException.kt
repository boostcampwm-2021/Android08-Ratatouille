package com.kdjj.domain.model.exception

import com.kdjj.domain.model.Recipe
import java.lang.Exception

class UploadException(
    msg: String? = null,
    val recipe: Recipe
) : Exception(msg ?: "upload fail")
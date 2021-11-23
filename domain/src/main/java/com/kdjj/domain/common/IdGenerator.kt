package com.kdjj.domain.common

import java.util.*
import javax.inject.Inject

internal class IdGenerator @Inject constructor() {
    fun generateId() = UUID.randomUUID().toString().replace("-", "")
}
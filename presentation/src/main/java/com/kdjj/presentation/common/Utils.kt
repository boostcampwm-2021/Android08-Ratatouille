package com.kdjj.presentation.common

import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext

object Utils {

    fun calculateSeconds(min: Int, sec: Int): Int{
        return min*60 + sec
    }
}
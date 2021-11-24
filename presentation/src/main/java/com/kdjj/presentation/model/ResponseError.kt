package com.kdjj.presentation.model

import androidx.annotation.StringRes
import com.kdjj.presentation.R

enum class ResponseError(@StringRes val stringRes: Int) {
    NETWORK_CONNECTION(R.string.networkErrorMessage),
    SERVER(R.string.severErrorMessage),
    UNKNOWN(R.string.unknownErrorMessage)
}
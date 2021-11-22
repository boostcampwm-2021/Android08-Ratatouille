package com.kdjj.remote.common

import com.google.firebase.firestore.FirebaseFirestoreException
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import java.lang.Exception

internal fun fireStoreExceptionToDomain(throwable: Throwable) =
    when (throwable) {
        is FirebaseFirestoreException -> {
            when (throwable.code.value()) {
                14 -> NetworkException()
                else -> ApiException()
            }
        }
        else -> {
            Exception(throwable)
        }
    }
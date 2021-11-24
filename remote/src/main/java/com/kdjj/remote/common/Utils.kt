package com.kdjj.remote.common

import com.google.firebase.firestore.FirebaseFirestoreException
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import java.lang.Exception
import java.util.concurrent.CancellationException

internal fun fireStoreExceptionToDomain(throwable: Throwable) =
    when (throwable) {
        is FirebaseFirestoreException -> {
            when (throwable.code.value()) {
                14 -> NetworkException()
                else -> ApiException()
            }
        }
        is CancellationException -> throwable
        else -> {
            Exception(throwable)
        }
    }
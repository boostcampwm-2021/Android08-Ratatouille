package com.kdjj.remote.common

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code.UNAVAILABLE
import com.kdjj.domain.model.exception.ApiException
import com.kdjj.domain.model.exception.NetworkException
import java.util.concurrent.CancellationException

internal fun fireStoreExceptionToDomain(throwable: Throwable) =
    when (throwable) {
        is FirebaseFirestoreException -> {
            when (throwable.code) {
                UNAVAILABLE -> NetworkException()
                else -> ApiException()
            }
        }
        is CancellationException -> throwable
        else -> {
            Exception(throwable)
        }
    }
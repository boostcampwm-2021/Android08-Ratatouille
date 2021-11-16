package com.kdjj.domain.model.exception

class NetworkException(msg: String? = null) : Exception(msg ?: "Device is not connected to network.")

class ApiException(msg: String? = null) : Exception(msg ?: "There is a problem with the server.")
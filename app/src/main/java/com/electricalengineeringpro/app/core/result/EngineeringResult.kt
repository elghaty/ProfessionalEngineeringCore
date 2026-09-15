package com.electricalengineeringpro.app.core.result

sealed class EngineeringResult<out T> {

    data class Success<T>(
        val value: T
    ) : EngineeringResult<T>()

    data class Error(
        val message: String
    ) : EngineeringResult<Nothing>()
}

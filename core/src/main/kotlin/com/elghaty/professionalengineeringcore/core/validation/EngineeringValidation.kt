package com.elghaty.professionalengineeringcore.core.validation

object EngineeringValidation {

    fun positive(value: Double, name: String): String? =
        if (value > 0.0) null else "$name must be greater than zero."

    fun nonNegative(value: Double, name: String): String? =
        if (value >= 0.0) null else "$name cannot be negative."

    fun range(
        value: Double,
        min: Double,
        max: Double,
        name: String
    ): String? =
        if (value in min..max) null
        else "$name must be between $min and $max."

    fun combine(vararg messages: String?): String =
        messages.filterNotNull().joinToString(" ")
}

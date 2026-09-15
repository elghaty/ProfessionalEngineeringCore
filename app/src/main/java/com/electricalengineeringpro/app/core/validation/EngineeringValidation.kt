package com.electricalengineeringpro.app.core.validation

object EngineeringValidation {

    fun positive(value: Double, name: String) {
        require(value > 0.0) {
            "$name must be greater than zero."
        }
    }

    fun nonNegative(value: Double, name: String) {
        require(value >= 0.0) {
            "$name cannot be negative."
        }
    }

    fun percentage(value: Double, name: String) {
        require(value in 0.0..100.0) {
            "$name must be between 0 and 100%."
        }
    }

    fun powerFactor(value: Double) {
        require(value in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.00."
        }
    }
}

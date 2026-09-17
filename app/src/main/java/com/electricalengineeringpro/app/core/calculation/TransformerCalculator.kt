package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.TransformerInput
import com.electricalengineeringpro.app.core.model.TransformerResult
import kotlin.math.sqrt

class TransformerCalculator {

    fun calculate(input: TransformerInput): TransformerResult {

        require(input.ratingKva > 0.0) {
            "Transformer rating must be greater than zero."
        }

        require(input.primaryVoltage > 0.0) {
            "Primary voltage must be greater than zero."
        }

        require(input.secondaryVoltage > 0.0) {
            "Secondary voltage must be greater than zero."
        }

        require(input.impedancePercent > 0.0) {
            "Transformer impedance must be greater than zero."
        }

        val primaryCurrent =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) * input.primaryVoltage)

        val secondaryCurrent =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) * input.secondaryVoltage)

        val faultCurrentA =
            secondaryCurrent /
                (input.impedancePercent / 100.0)

        return TransformerResult(
            primaryCurrentA = primaryCurrent,
            secondaryCurrentA = secondaryCurrent,
            shortCircuitCurrentKA = faultCurrentA / 1000.0
        )
    }
}

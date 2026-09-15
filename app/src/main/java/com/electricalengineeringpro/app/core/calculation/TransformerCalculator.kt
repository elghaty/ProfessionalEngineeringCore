package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.TransformerInput
import com.electricalengineeringpro.app.core.model.TransformerResult
import kotlin.math.sqrt

class TransformerCalculator {

    fun calculate(input: TransformerInput): TransformerResult {
        require(input.ratingKva > 0)
        require(input.primaryVoltage > 0)
        require(input.secondaryVoltage > 0)
        require(input.impedancePercent > 0)

        val primaryCurrent =
            input.ratingKva * 1000.0 /
                    (sqrt(3.0) * input.primaryVoltage)

        val secondaryCurrent =
            input.ratingKva * 1000.0 /
                    (sqrt(3.0) * input.secondaryVoltage)

        val baseCurrent = secondaryCurrent
        val faultCurrent =
            baseCurrent / (input.impedancePercent / 100.0)

        return TransformerResult(
            primaryCurrentA = primaryCurrent,
            secondaryCurrentA = secondaryCurrent,
            shortCircuitCurrentKA = faultCurrent / 1000.0
        )
    }
}

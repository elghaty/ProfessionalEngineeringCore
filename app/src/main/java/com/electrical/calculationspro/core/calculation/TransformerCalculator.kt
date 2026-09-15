package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.TransformerInput
import com.electrical.calculationspro.core.model.TransformerResult
import kotlin.math.sqrt

class TransformerCalculator {

    fun calculate(
        input: TransformerInput
    ): TransformerResult {

        require(input.ratingKva > 0.0)
        require(input.primaryVoltageV > 0.0)
        require(input.secondaryVoltageV > 0.0)
        require(input.impedancePercent > 0.0)

        val primaryCurrent =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) *
                    input.primaryVoltageV)

        val secondaryCurrent =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) *
                    input.secondaryVoltageV)

        val faultCurrent =
            secondaryCurrent /
                (input.impedancePercent / 100.0)

        val faultMva =
            sqrt(3.0) *
                input.secondaryVoltageV *
                faultCurrent /
                1_000_000.0

        return TransformerResult(
            primaryCurrentA = primaryCurrent,
            secondaryCurrentA = secondaryCurrent,
            shortCircuitCurrentKA =
                faultCurrent / 1000.0,
            faultMva = faultMva
        )
    }
}

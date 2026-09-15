package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.GeneratorInput
import com.electrical.calculationspro.core.model.GeneratorResult
import kotlin.math.sqrt

class GeneratorCalculator {

    fun calculate(
        input: GeneratorInput
    ): GeneratorResult {

        require(input.ratingKva > 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.xdPercent > 0.0)

        val current =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) *
                    input.voltageV)

        val initialFault =
            current /
                (input.xdPercent / 100.0)

        val faultMva =
            sqrt(3.0) *
                input.voltageV *
                initialFault /
                1_000_000.0

        return GeneratorResult(
            fullLoadCurrentA = current,
            initialShortCircuitCurrentKA =
                initialFault / 1000.0,
            faultMva = faultMva
        )
    }
}

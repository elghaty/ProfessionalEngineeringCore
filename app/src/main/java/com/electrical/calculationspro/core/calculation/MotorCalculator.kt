package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.MotorInput
import com.electrical.calculationspro.core.model.MotorResult
import kotlin.math.sqrt

class MotorCalculator {

    fun calculate(
        input: MotorInput
    ): MotorResult {

        require(input.powerKw >= 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.efficiency in 0.01..1.0)
        require(input.startingMultiplier >= 1.0)

        val inputPowerKw =
            input.powerKw /
                input.efficiency

        val kva =
            inputPowerKw /
                input.powerFactor

        val current =
            inputPowerKw * 1000.0 /
                (sqrt(3.0) *
                    input.voltageV *
                    input.powerFactor)

        return MotorResult(
            fullLoadCurrentA = current,
            startingCurrentA =
                current *
                    input.startingMultiplier,
            apparentPowerKva = kva
        )
    }
}

package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.PumpInput
import com.electrical.calculationspro.core.model.PumpResult
import kotlin.math.sqrt

class PumpCalculator {

    fun calculate(
        input: PumpInput
    ): PumpResult {

        require(input.hydraulicPowerKw >= 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.motorEfficiency in 0.01..1.0)
        require(input.pumpEfficiency in 0.01..1.0)

        val shaftPowerKw =
            input.hydraulicPowerKw /
                input.pumpEfficiency

        val motorInputKw =
            shaftPowerKw /
                input.motorEfficiency

        val kva =
            motorInputKw /
                input.powerFactor

        val current =
            motorInputKw * 1000.0 /
                (
                    sqrt(3.0) *
                        input.voltageV *
                        input.powerFactor
                    )

        return PumpResult(
            motorInputPowerKw = motorInputKw,
            currentA = current,
            apparentPowerKva = kva
        )
    }
}

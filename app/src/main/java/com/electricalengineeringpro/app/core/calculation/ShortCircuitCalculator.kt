package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ShortCircuitInput
import com.electricalengineeringpro.app.core.model.ShortCircuitResult
import kotlin.math.sqrt

class ShortCircuitCalculator {

    fun calculate(input: ShortCircuitInput): ShortCircuitResult {
        require(input.sourceVoltage > 0)
        require(input.transformerKva > 0)
        require(input.transformerImpedancePercent > 0)

        val transformerBaseCurrent =
            input.transformerKva * 1000.0 /
                    (sqrt(3.0) * input.sourceVoltage)

        val transformerFault =
            transformerBaseCurrent /
                    (input.transformerImpedancePercent / 100.0)

        val transformerFaultMva =
            sqrt(3.0) *
                    input.sourceVoltage *
                    transformerFault /
                    1_000_000.0

        if (input.sourceShortCircuitMva == null) {
            return ShortCircuitResult(
                faultCurrentKA = transformerFault / 1000.0,
                faultMva = transformerFaultMva
            )
        }

        val sourceMva = input.sourceShortCircuitMva
        require(sourceMva > 0)

        val combinedMva =
            1.0 / (1.0 / sourceMva + 1.0 / transformerFaultMva)

        val current =
            combinedMva * 1_000_000.0 /
                    (sqrt(3.0) * input.sourceVoltage)

        return ShortCircuitResult(
            faultCurrentKA = current / 1000.0,
            faultMva = combinedMva
        )
    }
}

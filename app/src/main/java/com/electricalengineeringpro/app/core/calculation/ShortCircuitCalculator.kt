package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ShortCircuitInput
import com.electricalengineeringpro.app.core.model.ShortCircuitResult
import kotlin.math.sqrt

class ShortCircuitCalculator {

    fun calculate(input: ShortCircuitInput): ShortCircuitResult {

        require(input.sourceVoltage > 0.0) {
            "Source voltage must be greater than zero."
        }

        require(input.transformerKva > 0.0) {
            "Transformer rating must be greater than zero."
        }

        require(input.transformerImpedancePercent > 0.0) {
            "Transformer impedance must be greater than zero."
        }

        val transformerBaseCurrentA =
            input.transformerKva * 1000.0 /
                (sqrt(3.0) * input.sourceVoltage)

        val transformerFaultCurrentA =
            transformerBaseCurrentA /
                (input.transformerImpedancePercent / 100.0)

        val transformerFaultMva =
            sqrt(3.0) *
                input.sourceVoltage *
                transformerFaultCurrentA /
                1_000_000.0

        val sourceMva = input.sourceShortCircuitMva

        if (sourceMva == null) {
            return ShortCircuitResult(
                faultCurrentKA =
                    transformerFaultCurrentA / 1000.0,
                faultMva = transformerFaultMva
            )
        }

        require(sourceMva > 0.0) {
            "Source short-circuit MVA must be greater than zero."
        }

        val combinedFaultMva =
            1.0 /
                (
                    1.0 / sourceMva +
                        1.0 / transformerFaultMva
                    )

        val faultCurrentA =
            combinedFaultMva * 1_000_000.0 /
                (sqrt(3.0) * input.sourceVoltage)

        return ShortCircuitResult(
            faultCurrentKA = faultCurrentA / 1000.0,
            faultMva = combinedFaultMva
        )
    }
}

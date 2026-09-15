package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.MotorInput
import com.electricalengineeringpro.app.core.model.MotorResult
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

class MotorCalculator {

    fun calculate(input: MotorInput): MotorResult {
        require(input.powerKw > 0)
        require(input.voltage > 0)
        require(input.powerFactor in 0.01..1.0)
        require(input.efficiency in 0.01..1.0)
        require(input.startingMultiplier >= 1.0)

        val current = if (input.phase == Phase.THREE) {
            input.powerKw * 1000.0 /
                    (sqrt(3.0) * input.voltage * input.powerFactor * input.efficiency)
        } else {
            input.powerKw * 1000.0 /
                    (input.voltage * input.powerFactor * input.efficiency)
        }

        return MotorResult(
            fullLoadCurrentA = current,
            startingCurrentA = current * input.startingMultiplier
        )
    }
}

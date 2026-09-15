package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.PumpInput
import com.electricalengineeringpro.app.core.model.PumpResult
import kotlin.math.sqrt

class PumpCalculator {

    private val waterDensity = 1000.0
    private val gravity = 9.80665

    fun calculate(input: PumpInput): PumpResult {
        require(input.flowM3s >= 0)
        require(input.headM >= 0)
        require(input.pumpEfficiency in 0.01..1.0)
        require(input.motorEfficiency in 0.01..1.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.voltage > 0)

        val hydraulic =
            waterDensity * gravity * input.flowM3s * input.headM / 1000.0

        val motorPower =
            hydraulic / input.pumpEfficiency / input.motorEfficiency

        val current =
            motorPower * 1000.0 /
                    (sqrt(3.0) * input.voltage * input.powerFactor)

        return PumpResult(
            hydraulicPowerKw = hydraulic,
            motorPowerKw = motorPower,
            currentA = current
        )
    }
}

package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.PumpInput
import com.electricalengineeringpro.app.core.model.PumpResult
import kotlin.math.sqrt

class PumpCalculator {

    private companion object {
        const val WATER_DENSITY_KG_M3 = 1000.0
        const val GRAVITY_M_S2 = 9.80665
        const val KW_CONVERSION = 1000.0
    }

    fun calculate(input: PumpInput): PumpResult {

        require(input.flowM3s >= 0.0) {
            "Flow must not be negative."
        }

        require(input.headM >= 0.0) {
            "Head must not be negative."
        }

        require(input.pumpEfficiency in 0.01..1.0) {
            "Pump efficiency must be between 0.01 and 1.0."
        }

        require(input.motorEfficiency in 0.01..1.0) {
            "Motor efficiency must be between 0.01 and 1.0."
        }

        require(input.powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(input.voltage > 0.0) {
            "Voltage must be greater than zero."
        }

        val hydraulicPowerKw =
            WATER_DENSITY_KG_M3 *
                GRAVITY_M_S2 *
                input.flowM3s *
                input.headM /
                KW_CONVERSION

        val motorPowerKw =
            if (hydraulicPowerKw == 0.0) {
                0.0
            } else {
                hydraulicPowerKw /
                    input.pumpEfficiency /
                    input.motorEfficiency
            }

        val currentA =
            if (motorPowerKw == 0.0) {
                0.0
            } else {
                motorPowerKw * KW_CONVERSION /
                    (
                        sqrt(3.0) *
                            input.voltage *
                            input.powerFactor
                        )
            }

        return PumpResult(
            hydraulicPowerKw = hydraulicPowerKw,
            motorPowerKw = motorPowerKw,
            currentA = currentA
        )
    }
}

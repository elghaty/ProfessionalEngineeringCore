package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

data class PowerResult(
    val currentA: Double,
    val apparentPowerKva: Double,
    val activePowerKw: Double
)

class PowerCalculator {

    fun fromKw(
        powerKw: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): PowerResult {

        require(powerKw >= 0.0) {
            "Power must not be negative."
        }

        require(voltage > 0.0) {
            "Voltage must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        val currentA = when (phase) {

            Phase.THREE ->
                powerKw * 1000.0 /
                    (
                        sqrt(3.0) *
                            voltage *
                            powerFactor
                        )

            Phase.SINGLE ->
                powerKw * 1000.0 /
                    (
                        voltage *
                            powerFactor
                        )
        }

        return PowerResult(
            currentA = currentA,
            apparentPowerKva =
                if (powerFactor > 0.0) {
                    powerKw / powerFactor
                } else {
                    0.0
                },
            activePowerKw = powerKw
        )
    }

    fun fromKva(
        kva: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): PowerResult {

        require(kva >= 0.0) {
            "Apparent power must not be negative."
        }

        require(voltage > 0.0) {
            "Voltage must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        val powerKw =
            kva * powerFactor

        return fromKw(
            powerKw = powerKw,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
    }
}

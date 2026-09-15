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

        require(powerKw >= 0.0)
        require(voltage > 0.0)
        require(powerFactor in 0.01..1.0)

        val current = if (phase == Phase.THREE) {
            powerKw * 1000.0 /
                    (sqrt(3.0) * voltage * powerFactor)
        } else {
            powerKw * 1000.0 /
                    (voltage * powerFactor)
        }

        return PowerResult(
            currentA = current,
            apparentPowerKva = powerKw / powerFactor,
            activePowerKw = powerKw
        )
    }

    fun fromKva(
        kva: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): PowerResult {
        require(kva >= 0.0)
        require(voltage > 0.0)
        require(powerFactor in 0.01..1.0)

        val kw = kva * powerFactor

        return fromKw(
            powerKw = kw,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
    }
}

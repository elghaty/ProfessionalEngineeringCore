package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.PowerResult
import kotlin.math.sqrt

class PowerCalculator {

    fun fromKw(
        powerKw: Double,
        voltageV: Double,
        powerFactor: Double,
        phase: Phase
    ): PowerResult {
        require(powerKw >= 0.0)
        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)

        val kva = powerKw / powerFactor
        val kvar = sqrt(
            (kva * kva - powerKw * powerKw)
                .coerceAtLeast(0.0)
        )

        val current = when (phase) {
            Phase.DC ->
                powerKw * 1000.0 / voltageV

            Phase.SINGLE ->
                powerKw * 1000.0 /
                    (voltageV * powerFactor)

            Phase.TWO ->
                powerKw * 1000.0 /
                    (2.0 * voltageV * powerFactor)

            Phase.THREE ->
                powerKw * 1000.0 /
                    (sqrt(3.0) * voltageV * powerFactor)
        }

        return PowerResult(
            activePowerKw = powerKw,
            apparentPowerKva = kva,
            reactivePowerKvar = kvar,
            currentA = current,
            powerFactor = powerFactor
        )
    }

    fun fromKva(
        apparentPowerKva: Double,
        voltageV: Double,
        powerFactor: Double,
        phase: Phase
    ): PowerResult {
        require(apparentPowerKva >= 0.0)
        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)

        return fromKw(
            powerKw = apparentPowerKva * powerFactor,
            voltageV = voltageV,
            powerFactor = powerFactor,
            phase = phase
        )
    }
}

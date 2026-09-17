package com.electrical.calculationspro.core.calculation

import kotlin.math.sqrt

class BasicElectricalCalculator {

    fun voltageFromPower(
        powerKw: Double,
        currentA: Double,
        powerFactor: Double,
        phaseCount: Int
    ): Double {

        require(powerKw >= 0.0)
        require(currentA > 0.0)
        require(powerFactor in 0.01..1.0)
        require(phaseCount in 1..3)

        return when (phaseCount) {

            1 ->
                powerKw * 1000.0 /
                    (currentA * powerFactor)

            2 ->
                powerKw * 1000.0 /
                    (2.0 * currentA * powerFactor)

            else ->
                powerKw * 1000.0 /
                    (sqrt(3.0) * currentA * powerFactor)
        }
    }

    fun resistance(
        voltageV: Double,
        currentA: Double
    ): Double {

        require(voltageV >= 0.0)
        require(currentA > 0.0)

        return voltageV / currentA
    }

    fun impedanceMagnitude(
        voltageV: Double,
        currentA: Double
    ): Double {

        require(voltageV >= 0.0)
        require(currentA > 0.0)

        return voltageV / currentA
    }

    fun reactivePowerKvar(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double {

        require(activePowerKw >= 0.0)
        require(apparentPowerKva >= 0.0)
        require(apparentPowerKva >= activePowerKw)

        return sqrt(
            (
                apparentPowerKva * apparentPowerKva -
                    activePowerKw * activePowerKw
            ).coerceAtLeast(0.0)
        )
    }

    fun powerFactor(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double {

        require(activePowerKw >= 0.0)
        require(apparentPowerKva > 0.0)
        require(activePowerKw <= apparentPowerKva)

        return activePowerKw / apparentPowerKva
    }
}

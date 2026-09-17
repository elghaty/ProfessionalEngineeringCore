package com.electrical.calculationspro.core.calculation

import kotlin.math.sqrt

/**
 * Basic electrical calculations used by
 * ProfessionalEngineeringCore.
 *
 * Units:
 * Power       -> kW / kVA / kvar
 * Voltage     -> V
 * Current     -> A
 * Resistance  -> ohm
 * Impedance   -> ohm
 */
class BasicElectricalCalculator {

    /**
     * Calculates voltage from active power and current.
     *
     * phaseCount:
     * 1 -> single phase
     * 3 -> three phase
     */
    fun voltageFromPower(
        powerKw: Double,
        currentA: Double,
        powerFactor: Double,
        phaseCount: Int
    ): Double {

        require(powerKw >= 0.0) {
            "Power cannot be negative."
        }

        require(currentA > 0.0) {
            "Current must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(
            phaseCount == 1 || phaseCount == 3
        ) {
            "Phase count must be 1 or 3."
        }

        return when (phaseCount) {

            3 -> {
                powerKw * 1000.0 /
                    (
                        sqrt(3.0) *
                            currentA *
                            powerFactor
                        )
            }

            else -> {
                powerKw * 1000.0 /
                    (
                        currentA *
                            powerFactor
                        )
            }
        }
    }

    /**
     * Calculates resistance using Ohm's law:
     *
     * R = V / I
     */
    fun resistance(
        voltageV: Double,
        currentA: Double
    ): Double {

        require(voltageV >= 0.0) {
            "Voltage cannot be negative."
        }

        require(currentA > 0.0) {
            "Current must be greater than zero."
        }

        return voltageV / currentA
    }

    /**
     * Calculates impedance magnitude:
     *
     * |Z| = V / I
     */
    fun impedanceMagnitude(
        voltageV: Double,
        currentA: Double
    ): Double {

        require(voltageV >= 0.0) {
            "Voltage cannot be negative."
        }

        require(currentA > 0.0) {
            "Current must be greater than zero."
        }

        return voltageV / currentA
    }

    /**
     * Calculates reactive power:
     *
     * kvar = sqrt(kVA² - kW²)
     */
    fun reactivePowerKvar(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double {

        require(activePowerKw >= 0.0) {
            "Active power cannot be negative."
        }

        require(apparentPowerKva >= 0.0) {
            "Apparent power cannot be negative."
        }

        require(
            apparentPowerKva + 1e-9 >= activePowerKw
        ) {
            "Apparent power cannot be lower than active power."
        }

        return sqrt(
            (
                apparentPowerKva * apparentPowerKva -
                    activePowerKw * activePowerKw
                ).coerceAtLeast(0.0)
        )
    }

    /**
     * Calculates power factor:
     *
     * PF = kW / kVA
     */
    fun powerFactor(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double {

        require(activePowerKw >= 0.0) {
            "Active power cannot be negative."
        }

        require(apparentPowerKva > 0.0) {
            "Apparent power must be greater than zero."
        }

        require(
            activePowerKw <= apparentPowerKva + 1e-9
        ) {
            "Active power cannot exceed apparent power."
        }

        return (
            activePowerKw /
                apparentPowerKva
            ).coerceIn(0.0, 1.0)
    }
}

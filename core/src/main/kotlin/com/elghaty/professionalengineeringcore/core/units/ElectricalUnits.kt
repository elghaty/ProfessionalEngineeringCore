package com.elghaty.professionalengineeringcore.core.units

import kotlin.math.sqrt

object ElectricalUnits {

    const val SQRT3 = 1.7320508075688772

    fun kvaToKw(kva: Double, powerFactor: Double): Double =
        kva * powerFactor

    fun kwToKva(kw: Double, powerFactor: Double): Double =
        kw / powerFactor

    fun threePhaseCurrentFromKw(
        kw: Double,
        voltage: Double,
        powerFactor: Double,
        efficiency: Double = 1.0
    ): Double {
        require(voltage > 0)
        require(powerFactor > 0)
        require(efficiency > 0)

        return kw * 1000.0 /
                (SQRT3 * voltage * powerFactor * efficiency)
    }

    fun singlePhaseCurrentFromKw(
        kw: Double,
        voltage: Double,
        powerFactor: Double,
        efficiency: Double = 1.0
    ): Double {
        require(voltage > 0)
        require(powerFactor > 0)
        require(efficiency > 0)

        return kw * 1000.0 /
                (voltage * powerFactor * efficiency)
    }

    fun currentFromKva(
        kva: Double,
        voltage: Double,
        phase: Int
    ): Double {
        require(kva >= 0)
        require(voltage > 0)

        return if (phase == 3) {
            kva * 1000.0 / (SQRT3 * voltage)
        } else {
            kva * 1000.0 / voltage
        }
    }

    fun sqrt3(): Double = sqrt(3.0)
}

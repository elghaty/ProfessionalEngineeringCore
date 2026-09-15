package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

class VoltageDropCalculator {

    fun calculate(
        currentA: Double,
        lengthM: Double,
        resistanceOhmPerKm: Double,
        reactanceOhmPerKm: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase,
        maximumPercent: Double
    ): com.electricalengineeringpro.app.core.model.VoltageDropResult {

        require(currentA >= 0)
        require(lengthM >= 0)
        require(voltage > 0)
        require(powerFactor in 0.0..1.0)

        val sinPhi = sqrt(1.0 - powerFactor * powerFactor)
        val lengthKm = lengthM / 1000.0

        val impedanceComponent =
            resistanceOhmPerKm * powerFactor +
                    reactanceOhmPerKm * sinPhi

        val drop = if (phase == Phase.THREE) {
            sqrt(3.0) * currentA * lengthKm * impedanceComponent
        } else {
            2.0 * currentA * lengthKm * impedanceComponent
        }

        val percent = drop / voltage * 100.0

        return com.electricalengineeringpro.app.core.model.VoltageDropResult(
            dropVolts = drop,
            dropPercent = percent,
            compliant = percent <= maximumPercent
        )
    }
}

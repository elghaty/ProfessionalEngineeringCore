package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.VoltageDropResult
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
    ): VoltageDropResult {

        require(currentA >= 0.0)
        require(lengthM >= 0.0)
        require(resistanceOhmPerKm >= 0.0)
        require(reactanceOhmPerKm >= 0.0)
        require(voltage > 0.0)
        require(powerFactor in 0.01..1.0)
        require(maximumPercent >= 0.0)

        val sinPhi =
            sqrt(
                (1.0 - powerFactor * powerFactor)
                    .coerceAtLeast(0.0)
            )

        val lengthKm =
            lengthM / 1000.0

        val impedanceComponent =
            resistanceOhmPerKm * powerFactor +
                    reactanceOhmPerKm * sinPhi

        val drop =
            when (phase) {
                Phase.THREE ->
                    sqrt(3.0) *
                            currentA *
                            lengthKm *
                            impedanceComponent

                Phase.SINGLE ->
                    2.0 *
                            currentA *
                            lengthKm *
                            impedanceComponent
            }

        val percent =
            drop / voltage * 100.0

        return VoltageDropResult(
            dropVolts = drop,
            dropPercent = percent,
            compliant = percent <= maximumPercent
        )
    }
}

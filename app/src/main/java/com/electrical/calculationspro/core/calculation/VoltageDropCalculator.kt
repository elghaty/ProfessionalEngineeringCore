package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.VoltageDropResult
import kotlin.math.sqrt

class VoltageDropCalculator {

    fun calculate(
        currentA: Double,
        lengthM: Double,
        resistanceOhmPerKm: Double,
        reactanceOhmPerKm: Double,
        voltageV: Double,
        powerFactor: Double,
        phase: Phase,
        maximumPercent: Double
    ): VoltageDropResult {

        require(currentA >= 0.0)
        require(lengthM >= 0.0)
        require(resistanceOhmPerKm >= 0.0)
        require(reactanceOhmPerKm >= 0.0)
        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)
        require(maximumPercent >= 0.0)

        val sinPhi =
            sqrt(
                (
                    1.0 -
                        powerFactor *
                        powerFactor
                    ).coerceAtLeast(0.0)
            )

        val resistance =
            resistanceOhmPerKm *
                lengthM /
                1000.0

        val reactance =
            reactanceOhmPerKm *
                lengthM /
                1000.0

        val dropVolts =
            when (phase) {

                Phase.DC ->
                    2.0 *
                        currentA *
                        resistance

                Phase.SINGLE ->
                    2.0 *
                        currentA *
                        (
                            resistance *
                                powerFactor +
                                reactance *
                                sinPhi
                            )

                Phase.TWO ->
                    2.0 *
                        currentA *
                        (
                            resistance *
                                powerFactor +
                                reactance *
                                sinPhi
                            )

                Phase.THREE ->
                    sqrt(3.0) *
                        currentA *
                        (
                            resistance *
                                powerFactor +
                                reactance *
                                sinPhi
                            )
            }

        val dropPercent =
            dropVolts /
                voltageV *
                100.0

        return VoltageDropResult(
            dropVolts = dropVolts,
            dropPercent = dropPercent,
            withinLimit =
                dropPercent <=
                    maximumPercent
        )
    }
}

package com.electrical.calculationspro.core.calculators

import org.junit.Assert.assertTrue
import org.junit.Test

class ShortCircuitCalculatorTest {

    @Test
    fun threePhaseShortCircuitIsCalculated() {

        val calculator =
            ShortCircuitCalculator()

        val result =
            calculator.calculate(
                ShortCircuitInput(
                    voltageV = 400.0,
                    sourceShortCircuitKA = 24.0,
                    cableLengthM = 50.0,
                    cableResistanceOhmPerKm = 0.125,
                    cableReactanceOhmPerKm = 0.080
                )
            )

        assertTrue(
            result.faultCurrentKA > 0.0
        )

        assertTrue(
            result.totalImpedanceOhm > 0.0
        )
    }

    @Test
    fun transformerShortCircuitIsCalculated() {

        val calculator =
            ShortCircuitCalculator()

        val result =
            calculator.calculate(
                ShortCircuitInput(
                    voltageV = 400.0,
                    transformerKVA = 1000.0,
                    transformerPercentZ = 6.0
                )
            )

        assertTrue(
            result.transformerShortCircuitKA > 0.0
        )

        assertTrue(
            result.faultCurrentKA > 0.0
        )
    }
}

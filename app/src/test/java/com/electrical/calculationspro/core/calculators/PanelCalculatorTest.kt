package com.electrical.calculationspro.core.calculators

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PanelCalculatorTest {

    @Test
    fun panelCalculationProducesMainBreaker() {

        val calculator =
            PanelCalculator()

        val result =
            calculator.calculate(
                PanelInput(
                    name = "MDB-01",
                    voltageV = 400.0,
                    feeders = listOf(
                        PanelFeederInput(
                            name = "PUMP-01",
                            loadKw = 30.0,
                            powerFactor = 0.90,
                            voltageV = 400.0,
                            demandFactor = 1.0,
                            cableCapacityA = 80.0
                        ),
                        PanelFeederInput(
                            name = "PUMP-02",
                            loadKw = 20.0,
                            powerFactor = 0.90,
                            voltageV = 400.0,
                            demandFactor = 0.80,
                            cableCapacityA = 63.0
                        )
                    )
                )
            )

        assertEquals(
            50.0,
            result.connectedLoadKw,
            0.001
        )

        assertTrue(
            result.demandLoadKw > 0.0
        )

        assertTrue(
            result.designCurrentA > 0.0
        )

        assertTrue(
            result.recommendedMainBreakerA >=
                result.designCurrentA
        )
    }
}

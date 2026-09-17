package com.electricalengineeringpro.app.core.calculation

import org.junit.Assert.assertTrue
import org.junit.Test

class MdbCalculatorTest {

    @Test
    fun mdb_selects_incomer_and_busbar() {

        val result =
            MdbCalculator().calculate(
                MdbInput(
                    connectedLoadKw = 400.0,
                    demandFactor = 0.8,
                    powerFactor = 0.9
                )
            )

        assertTrue(
            result.demandLoadKw > 0
        )

        assertTrue(
            result.apparentPowerKva > 0
        )

        assertTrue(
            result.designCurrentA > 0
        )

        assertTrue(
            result.recommendedBusbarA >=
                result.recommendedIncomerA
        )
    }
}

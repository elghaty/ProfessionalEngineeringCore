package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.PowerCalculator
import com.electricalengineeringpro.app.core.model.Phase
import org.junit.Assert.assertEquals
import org.junit.Test

class PowerCalculatorTest {

    @Test
    fun calculatesThreePhaseCurrentFromKw() {
        val result = PowerCalculator().fromKw(
            powerKw = 100.0,
            voltage = 400.0,
            powerFactor = 0.8,
            phase = Phase.THREE
        )

        assertEquals(180.42, result.currentA, 0.2)
        assertEquals(125.0, result.apparentPowerKva, 0.001)
    }
}

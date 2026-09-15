package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.MotorCalculator
import com.electricalengineeringpro.app.core.model.MotorInput
import com.electricalengineeringpro.app.core.model.Phase
import org.junit.Assert.assertEquals
import org.junit.Test

class MotorCalculatorTest {

    @Test
    fun motorCurrentIsCalculated() {
        val result = MotorCalculator().calculate(
            MotorInput(
                powerKw = 75.0,
                voltage = 400.0,
                powerFactor = 0.85,
                efficiency = 0.93,
                phase = Phase.THREE,
                startingMultiplier = 6.0
            )
        )

        assertEquals(137.15, result.fullLoadCurrentA, 0.5)
        assertEquals(822.9, result.startingCurrentA, 3.0)
    }
}

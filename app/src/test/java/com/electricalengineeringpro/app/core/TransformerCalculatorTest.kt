package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.TransformerCalculator
import com.electricalengineeringpro.app.core.model.TransformerInput
import org.junit.Assert.assertEquals
import org.junit.Test

class TransformerCalculatorTest {

    @Test
    fun transformerCurrentAndFaultAreCalculated() {
        val result = TransformerCalculator().calculate(
            TransformerInput(
                ratingKva = 630.0,
                primaryVoltage = 11000.0,
                secondaryVoltage = 400.0,
                impedancePercent = 6.0
            )
        )

        assertEquals(33.06, result.primaryCurrentA, 0.1)
        assertEquals(909.33, result.secondaryCurrentA, 1.0)
        assertEquals(15.16, result.shortCircuitCurrentKA, 0.1)
    }
}

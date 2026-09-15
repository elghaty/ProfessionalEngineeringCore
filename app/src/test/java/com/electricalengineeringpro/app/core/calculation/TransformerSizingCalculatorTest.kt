package com.electricalengineeringpro.app.core.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TransformerSizingCalculatorTest {

    @Test
    fun selectsStandardTransformerRating() {

        val result =
            TransformerSizingCalculator().calculate(
                TransformerSizingInput(
                    designLoadKW = 400.0,
                    powerFactor = 0.90,
                    spareCapacityFactor = 1.15
                )
            )

        assertTrue(result.requiredKVA > 400.0)
        assertEquals(630.0, result.recommendedRatingKVA, 0.001)
        assertTrue(result.utilizationPercent > 0.0)
    }
}

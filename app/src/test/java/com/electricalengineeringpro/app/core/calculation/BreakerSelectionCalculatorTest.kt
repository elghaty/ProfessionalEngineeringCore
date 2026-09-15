package com.electricalengineeringpro.app.core.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreakerSelectionCalculatorTest {

    @Test
    fun selectsBreakerAboveDesignCurrent() {

        val result =
            BreakerSelectionCalculator().calculate(
                BreakerSelectionInput(
                    loadCurrentA = 350.0,
                    shortCircuitKA = 24.0,
                    utilizationFactor = 0.80
                )
            )

        assertEquals(500.0, result.recommendedRatingA, 0.001)
        assertTrue(
            result.recommendedBreakingCapacityKA >= 24.0
        )
        assertTrue(result.designCurrentA > 350.0)
    }
}

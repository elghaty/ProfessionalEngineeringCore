package com.electricalengineeringpro.app.core.calculation

import org.junit.Assert.assertTrue
import org.junit.Test

class BreakerSelectionCalculatorTest {

    @Test
    fun breaker_is_selected_above_design_current() {

        val result =
            BreakerSelectionCalculator().calculate(
                BreakerSelectionInput(
                    designCurrentA = 180.0,
                    shortCircuitKA = 18.0
                )
            )

        assertTrue(
            result.ratedCurrentA >= 180.0
        )

        assertTrue(
            result.breakingCapacityKA >= 18.0
        )
    }
}

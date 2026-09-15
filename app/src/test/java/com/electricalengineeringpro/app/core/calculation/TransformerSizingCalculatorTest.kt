package com.electricalengineeringpro.app.core.calculation

import org.junit.Assert.assertTrue
import org.junit.Test

class TransformerSizingCalculatorTest {

    @Test
    fun transformer_rating_is_selected_above_required_capacity() {

        val result =
            TransformerSizingCalculator().calculate(
                TransformerSizingInput(
                    demandLoadKw = 400.0,
                    powerFactor = 0.9,
                    spareCapacity = 0.2
                )
            )

        assertTrue(result.requiredKva > 400.0)
        assertTrue(result.selectedKva >= result.requiredKva)
        assertTrue(result.utilizationPercent > 0.0)
    }
}

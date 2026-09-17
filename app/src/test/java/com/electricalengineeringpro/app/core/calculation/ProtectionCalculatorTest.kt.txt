package com.electricalengineeringpro.app.core.calculation

import org.junit.Assert.assertTrue
import org.junit.Test

class ProtectionCalculatorTest {

    @Test
    fun protection_selects_breaker_above_design_current() {

        val result =
            ProtectionCalculator().calculate(
                ProtectionInput(
                    designCurrentA = 180.0,
                    cableAmpacityA = 250.0,
                    shortCircuitKA = 18.0,
                    voltageV = 400.0
                )
            )

        assertTrue(
            result.recommendedBreakerA >= 180.0
        )

        assertTrue(
            result.breakingCapacityKA >= 18.0
        )

        assertTrue(
            result.cableProtected
        )

        assertTrue(
            result.shortCircuitProtected
        )
    }
}

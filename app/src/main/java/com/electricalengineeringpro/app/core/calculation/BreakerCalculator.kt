package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.*

class BreakerCalculator {

    private val standardRatings = listOf(
        6.0, 10.0, 16.0, 20.0, 25.0, 32.0, 40.0, 50.0, 63.0,
        80.0, 100.0, 125.0, 160.0, 200.0, 250.0, 315.0, 400.0,
        500.0, 630.0, 800.0, 1000.0, 1250.0, 1600.0, 2000.0,
        2500.0, 3200.0, 4000.0
    )

    fun calculate(input: BreakerInput): BreakerResult {
        require(input.designCurrentA >= 0)
        require(input.shortCircuitCurrentKA >= 0)

        val rating = standardRatings.firstOrNull {
            it >= input.designCurrentA
        } ?: standardRatings.last()

        val minimumIcu = input.shortCircuitCurrentKA
        val breaking = standardBreakingCapacity(minimumIcu)

        return BreakerResult(
            ratedCurrentA = rating,
            breakingCapacityKA = breaking,
            type = input.preferredType,
            utilizationPercent =
                if (rating == 0.0) 0.0 else input.designCurrentA / rating * 100.0
        )
    }

    private fun standardBreakingCapacity(requiredKA: Double): Double {
        val values = listOf(3.0, 4.5, 6.0, 10.0, 15.0, 25.0, 36.0, 50.0, 65.0, 85.0, 100.0)
        return values.firstOrNull { it >= requiredKA } ?: 100.0
    }
}

package com.electricalengineeringpro.app.core.calculation

data class BreakerSelectionInput(
    val loadCurrentA: Double,
    val shortCircuitKA: Double = 0.0,
    val designMarginFactor: Double = 1.00
)

data class BreakerSelectionResult(
    val designCurrentA: Double,
    val recommendedRatingA: Double,
    val recommendedBreakingCapacityKA: Double,
    val utilizationPercent: Double
)

class BreakerSelectionCalculator {

    private val standardRatings = listOf(
        6.0,
        10.0,
        16.0,
        20.0,
        25.0,
        32.0,
        40.0,
        50.0,
        63.0,
        80.0,
        100.0,
        125.0,
        160.0,
        200.0,
        250.0,
        315.0,
        400.0,
        500.0,
        630.0,
        800.0,
        1000.0,
        1250.0,
        1600.0,
        2000.0,
        2500.0,
        3200.0,
        4000.0
    )

    private val standardBreakingCapacityKA = listOf(
        3.0,
        4.5,
        6.0,
        10.0,
        15.0,
        18.0,
        25.0,
        36.0,
        50.0,
        65.0,
        80.0,
        100.0
    )

    fun calculate(
        input: BreakerSelectionInput
    ): BreakerSelectionResult {

        require(input.loadCurrentA >= 0.0) {
            "Load current cannot be negative."
        }

        require(input.shortCircuitKA >= 0.0) {
            "Short-circuit current cannot be negative."
        }

        require(input.designMarginFactor >= 1.0) {
            "Design margin factor must be greater than or equal to 1.0."
        }

        val designCurrent =
            input.loadCurrentA *
                input.designMarginFactor

        val rating =
            standardRatings.firstOrNull {
                it >= designCurrent
            } ?: standardRatings.last()

        val breakingCapacity =
            if (input.shortCircuitKA > 0.0) {

                standardBreakingCapacityKA.firstOrNull {
                    it >= input.shortCircuitKA
                } ?: standardBreakingCapacityKA.last()

            } else {
                0.0
            }

        val utilization =
            if (rating > 0.0) {
                input.loadCurrentA /
                    rating *
                    100.0
            } else {
                0.0
            }

        return BreakerSelectionResult(
            designCurrentA = designCurrent,
            recommendedRatingA = rating,
            recommendedBreakingCapacityKA =
                breakingCapacity,
            utilizationPercent = utilization
        )
    }
}

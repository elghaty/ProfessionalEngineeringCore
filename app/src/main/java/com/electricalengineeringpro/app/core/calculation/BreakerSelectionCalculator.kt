package com.electricalengineeringpro.app.core.calculation

data class BreakerSelectionInput(
    val designCurrentA: Double,
    val shortCircuitKA: Double,
    val utilizationMargin: Double = 1.25
)

data class BreakerSelectionResult(
    val ratedCurrentA: Double,
    val breakingCapacityKA: Double,
    val recommendation: String
)

class BreakerSelectionCalculator {

    private val ratings = listOf(
        6.0, 10.0, 16.0, 20.0, 25.0, 32.0,
        40.0, 50.0, 63.0, 80.0, 100.0, 125.0,
        160.0, 200.0, 250.0, 315.0, 400.0,
        500.0, 630.0, 800.0, 1000.0, 1250.0,
        1600.0, 2000.0, 2500.0, 3200.0,
        4000.0
    )

    private val breakingCapacities = listOf(
        3.0, 4.5, 6.0, 10.0, 15.0, 18.0,
        25.0, 30.0, 36.0, 40.0, 50.0,
        65.0, 80.0, 100.0
    )

    fun calculate(
        input: BreakerSelectionInput
    ): BreakerSelectionResult {

        require(input.designCurrentA > 0.0)
        require(input.shortCircuitKA >= 0.0)
        require(input.utilizationMargin >= 1.0)

        val required =
            input.designCurrentA * input.utilizationMargin

        val rating =
            ratings.firstOrNull { it >= required }
                ?: required

        val breaking =
            breakingCapacities.firstOrNull {
                it >= input.shortCircuitKA
            } ?: 100.0

        return BreakerSelectionResult(
            ratedCurrentA = rating,
            breakingCapacityKA = breaking,
            recommendation =
                "Preliminary selection: ${rating.toInt()} A " +
                    "with minimum ${breaking.toInt()} kA breaking capacity."
        )
    }
}

package com.electrical.calculationspro.core.calculation

data class TransformerSizingResult(
    val requiredKva: Double,
    val recommendedKva: Double,
    val utilizationPercent: Double,
    val spareKva: Double
)

class TransformerSizingCalculator {

    private val standardRatings =
        listOf(
            50.0,
            100.0,
            160.0,
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
            3150.0,
            4000.0,
            5000.0,
            6300.0,
            8000.0,
            10000.0
        )

    fun calculate(
        requiredKva: Double,
        designMargin: Double = 1.15
    ): TransformerSizingResult {

        require(requiredKva >= 0.0)
        require(designMargin >= 1.0)

        val required =
            requiredKva * designMargin

        val selected =
            standardRatings.firstOrNull {
                it >= required
            } ?: required

        val utilization =
            if (selected == 0.0) {
                0.0
            } else {
                requiredKva /
                    selected *
                    100.0
            }

        return TransformerSizingResult(
            requiredKva = required,
            recommendedKva = selected,
            utilizationPercent = utilization,
            spareKva =
                selected - required
        )
    }
}

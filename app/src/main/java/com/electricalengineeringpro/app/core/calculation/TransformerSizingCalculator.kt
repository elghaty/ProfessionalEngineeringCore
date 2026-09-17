package com.electricalengineeringpro.app.core.calculation

import kotlin.math.ceil

data class TransformerSizingInput(
    val designLoadKW: Double,
    val powerFactor: Double = 0.90,
    val spareCapacityFactor: Double = 1.15
)

data class TransformerSizingResult(
    val designLoadKW: Double,
    val requiredKVA: Double,
    val recommendedRatingKVA: Double,
    val utilizationPercent: Double
)

class TransformerSizingCalculator {

    private val standardRatings =
        listOf(
            50.0, 63.0, 100.0, 160.0, 200.0,
            250.0, 315.0, 400.0, 500.0, 630.0,
            800.0, 1000.0, 1250.0, 1600.0,
            2000.0, 2500.0, 3150.0, 4000.0,
            5000.0
        )

    fun calculate(
        input: TransformerSizingInput
    ): TransformerSizingResult {

        require(input.designLoadKW >= 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.spareCapacityFactor >= 1.0)

        val required =
            if (input.designLoadKW == 0.0) {
                0.0
            } else {
                input.designLoadKW /
                        input.powerFactor *
                        input.spareCapacityFactor
            }

        val recommended =
            if (required == 0.0) {
                0.0
            } else {
                standardRatings.firstOrNull {
                    it >= required
                } ?: ceil(required / 500.0) * 500.0
            }

        val utilization =
            if (recommended > 0.0) {
                input.designLoadKW /
                        (recommended * input.powerFactor) *
                        100.0
            } else {
                0.0
            }

        return TransformerSizingResult(
            designLoadKW = input.designLoadKW,
            requiredKVA = required,
            recommendedRatingKVA = recommended,
            utilizationPercent = utilization
        )
    }
}

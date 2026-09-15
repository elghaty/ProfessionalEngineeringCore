package com.electricalengineeringpro.app.core.calculation

import kotlin.math.ceil

data class TransformerSizingInput(
    val demandLoadKw: Double,
    val powerFactor: Double = 0.9,
    val spareCapacity: Double = 0.2
)

data class TransformerSizingResult(
    val requiredKva: Double,
    val selectedKva: Double,
    val utilizationPercent: Double
)

class TransformerSizingCalculator {

    private val standardRatings = listOf(
        25.0,
        50.0,
        63.0,
        100.0,
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
        3150.0,
        4000.0
    )

    fun calculate(
        input: TransformerSizingInput
    ): TransformerSizingResult {

        require(input.demandLoadKw >= 0.0)
        require(input.powerFactor in 0.1..1.0)
        require(input.spareCapacity >= 0.0)

        val baseKva =
            if (input.powerFactor > 0.0) {
                input.demandLoadKw / input.powerFactor
            } else {
                0.0
            }

        val requiredKva =
            baseKva * (1.0 + input.spareCapacity)

        val selected =
            standardRatings.firstOrNull {
                it >= requiredKva
            } ?: ceil(requiredKva / 500.0) * 500.0

        val utilization =
            if (selected > 0.0) {
                baseKva / selected * 100.0
            } else {
                0.0
            }

        return TransformerSizingResult(
            requiredKva = requiredKva,
            selectedKva = selected,
            utilizationPercent = utilization
        )
    }
}

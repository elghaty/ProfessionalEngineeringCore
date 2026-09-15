package com.electrical.calculationspro.core.calculation

data class TransformerSizingInput(
    val designLoadKw: Double,
    val powerFactor: Double = 0.90,
    val designMargin: Double = 1.15,
    val minimumReservePercent: Double = 0.0
)

data class TransformerSizingResult(
    val calculatedKva: Double,
    val requiredKvaWithMargin: Double,
    val recommendedRatingKva: Double,
    val utilizationPercent: Double,
    val reservePercent: Double,
    val adequate: Boolean,
    val notes: List<String>
)

class TransformerSizingCalculator {

    private val standardRatingsKva =
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
            10000.0,
            12500.0,
            16000.0
        )

    fun calculate(
        input: TransformerSizingInput
    ): TransformerSizingResult {

        require(input.designLoadKw >= 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.designMargin >= 1.0)
        require(input.minimumReservePercent >= 0.0)

        val calculatedKva =
            input.designLoadKw /
                input.powerFactor

        val required =
            calculatedKva *
                input.designMargin

        val minimumRequired =
            required *
                (
                    1.0 +
                        input.minimumReservePercent /
                        100.0
                    )

        val recommended =
            standardRatingsKva.firstOrNull {
                it >= minimumRequired
            } ?: standardRatingsKva.last()

        val utilization =
            if (recommended > 0.0) {
                calculatedKva /
                    recommended *
                    100.0
            } else {
                0.0
            }

        val reserve =
            if (calculatedKva > 0.0) {
                (
                    recommended -
                        calculatedKva
                    ) /
                    calculatedKva *
                    100.0
            } else {
                100.0
            }

        val notes =
            listOf(
                "Calculated load = %.2f kVA"
                    .format(calculatedKva),
                "Required with design margin = %.2f kVA"
                    .format(required),
                "Recommended transformer = %.0f kVA"
                    .format(recommended),
                "Utilization = %.2f %%"
                    .format(utilization),
                "Available reserve = %.2f %%"
                    .format(reserve)
            )

        return TransformerSizingResult(
            calculatedKva = calculatedKva,
            requiredKvaWithMargin = minimumRequired,
            recommendedRatingKva = recommended,
            utilizationPercent = utilization,
            reservePercent = reserve,
            adequate =
                recommended >= minimumRequired,
            notes = notes
        )
    }

    fun calculateFromLoads(
        loads: List<com.electrical.calculationspro.core.model.ElectricalLoad>,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): TransformerSizingResult {

        val total =
            loads.sumOf {
                it.powerKw *
                    it.quantity *
                    it.demandFactor /
                    it.diversityFactor /
                    it.efficiency
            }

        return calculate(
            TransformerSizingInput(
                designLoadKw = total,
                powerFactor = powerFactor,
                designMargin = designMargin
            )
        )
    }
}

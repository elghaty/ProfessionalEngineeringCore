package com.electrical.calculationspro.core.calculation

data class ProtectionInput(
    val designCurrentA: Double,
    val cableCapacityA: Double,
    val shortCircuitCurrentKA: Double,
    val breakerRatingA: Double,
    val breakerIcuKA: Double
)

data class ProtectionResult(
    val overloadProtectionOk: Boolean,
    val shortCircuitProtectionOk: Boolean,
    val cableProtectionOk: Boolean,
    val overallOk: Boolean,
    val warnings: List<String>
)

class ProtectionCalculator {

    fun calculate(
        input: ProtectionInput
    ): ProtectionResult {

        require(input.designCurrentA >= 0.0)
        require(input.cableCapacityA >= 0.0)
        require(input.shortCircuitCurrentKA >= 0.0)
        require(input.breakerRatingA >= 0.0)
        require(input.breakerIcuKA >= 0.0)

        val overloadOk =
            input.breakerRatingA >= input.designCurrentA

        val cableOk =
            input.breakerRatingA <= input.cableCapacityA

        val shortCircuitOk =
            input.breakerIcuKA >=
                input.shortCircuitCurrentKA

        val warnings = mutableListOf<String>()

        if (!overloadOk) {
            warnings +=
                "Breaker rated current is below design current."
        }

        if (!cableOk) {
            warnings +=
                "Breaker rated current exceeds cable capacity."
        }

        if (!shortCircuitOk) {
            warnings +=
                "Breaker Icu is below prospective short-circuit current."
        }

        return ProtectionResult(
            overloadProtectionOk = overloadOk,
            shortCircuitProtectionOk = shortCircuitOk,
            cableProtectionOk = cableOk,
            overallOk =
                overloadOk &&
                    cableOk &&
                    shortCircuitOk,
            warnings = warnings
        )
    }
}

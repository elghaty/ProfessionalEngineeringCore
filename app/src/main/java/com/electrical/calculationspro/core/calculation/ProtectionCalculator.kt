package com.electrical.calculationspro.core.calculation

data class ProtectionInput(
    val designCurrentA: Double,
    val cableCapacityA: Double,
    val shortCircuitCurrentKA: Double,
    val breakerRatingA: Double,
    val breakerIcuKA: Double,
    val breakerIcsKA: Double = 0.0
)

data class ProtectionResult(
    val overloadProtected: Boolean,
    val cableProtected: Boolean,
    val shortCircuitProtected: Boolean,
    val breakingCapacityAdequate: Boolean,
    val overallAcceptable: Boolean,
    val utilizationPercent: Double,
    val warnings: List<String>
)

class ProtectionCalculator {

    fun calculate(
        input: ProtectionInput
    ): ProtectionResult {

        validate(input)

        val overload =
            input.breakerRatingA >=
                input.designCurrentA

        val cable =
            input.cableCapacityA <= 0.0 ||
                input.breakerRatingA <=
                input.cableCapacityA

        val shortCircuit =
            input.shortCircuitCurrentKA <= 0.0 ||
                input.breakerIcuKA >=
                input.shortCircuitCurrentKA

        val ics =
            input.shortCircuitCurrentKA <= 0.0 ||
                input.breakerIcsKA <= 0.0 ||
                input.breakerIcsKA >=
                input.shortCircuitCurrentKA

        val utilization =
            if (input.breakerRatingA > 0.0) {
                input.designCurrentA /
                    input.breakerRatingA *
                    100.0
            } else {
                0.0
            }

        val warnings =
            mutableListOf<String>()

        if (!overload) {
            warnings +=
                "Breaker rated current is below design current."
        }

        if (!cable) {
            warnings +=
                "Breaker rating exceeds cable capacity."
        }

        if (!shortCircuit) {
            warnings +=
                "Breaker Icu is below prospective short-circuit current."
        }

        if (!ics) {
            warnings +=
                "Breaker Ics is below prospective short-circuit current."
        }

        return ProtectionResult(
            overloadProtected = overload,
            cableProtected = cable,
            shortCircuitProtected = shortCircuit,
            breakingCapacityAdequate =
                shortCircuit && ics,
            overallAcceptable =
                overload &&
                    cable &&
                    shortCircuit &&
                    ics,
            utilizationPercent =
                utilization,
            warnings = warnings
        )
    }

    private fun validate(
        input: ProtectionInput
    ) {
        require(input.designCurrentA >= 0.0)
        require(input.cableCapacityA >= 0.0)
        require(input.shortCircuitCurrentKA >= 0.0)
        require(input.breakerRatingA > 0.0)
        require(input.breakerIcuKA >= 0.0)
        require(input.breakerIcsKA >= 0.0)
    }
}

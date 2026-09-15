package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.BreakerType

data class BreakerSelectionInput(
    val designCurrentA: Double,
    val cableCapacityA: Double,
    val shortCircuitKA: Double,
    val preferredType: BreakerType = BreakerType.MCCB
)

data class BreakerSelectionResult(
    val selectedRatingA: Double,
    val type: BreakerType,
    val icuKA: Double,
    val icsKA: Double,
    val acceptable: Boolean,
    val warnings: List<String>
)

class BreakerSelectionCalculator(
    private val breaker:
        BreakerCalculator =
        BreakerCalculator()
) {

    fun calculate(
        input: BreakerSelectionInput
    ): BreakerSelectionResult {

        val result =
            breaker.calculate(
                designCurrentA =
                    input.designCurrentA,
                cableCapacityA =
                    input.cableCapacityA,
                shortCircuitKA =
                    input.shortCircuitKA,
                preferredType =
                    input.preferredType
            )

        return BreakerSelectionResult(
            selectedRatingA =
                result.ratedCurrentA,
            type =
                result.type,
            icuKA =
                result.icuKA,
            icsKA =
                result.icsKA,
            acceptable =
                result.acceptable,
            warnings =
                result.warnings
        )
    }
}

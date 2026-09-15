package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.BreakerResult
import com.electrical.calculationspro.core.model.BreakerType

data class BreakerSelectionInput(
    val designCurrentA: Double,
    val cableCapacityA: Double,
    val shortCircuitCurrentKA: Double,
    val preferredType: BreakerType =
        BreakerType.MCCB
)

class BreakerSelectionCalculator(
    private val breakerCalculator:
        BreakerCalculator =
        BreakerCalculator()
) {

    fun calculate(
        input: BreakerSelectionInput
    ): BreakerResult {

        return breakerCalculator.calculate(
            designCurrentA =
                input.designCurrentA,

            cableCapacityA =
                input.cableCapacityA,

            shortCircuitKA =
                input.shortCircuitCurrentKA,

            preferredType =
                input.preferredType
        )
    }

    fun selectRating(
        designCurrentA: Double
    ): Double =
        breakerCalculator.selectRating(
            designCurrentA
        )

    fun selectBreakingCapacity(
        faultCurrentKA: Double
    ): Double =
        breakerCalculator.selectBreakingCapacity(
            faultCurrentKA
        )
}

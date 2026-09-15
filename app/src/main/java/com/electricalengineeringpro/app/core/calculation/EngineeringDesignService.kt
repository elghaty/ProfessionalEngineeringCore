package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad

data class CompleteDesignInput(
    val loads: List<ElectricalLoad>,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.90,
    val diversityFactor: Double = 0.85,
    val shortCircuitKA: Double = 0.0
)

data class CompleteDesignResult(
    val connectedLoadKW: Double,
    val demandLoadKW: Double,
    val designLoadKW: Double,
    val mainCurrentA: Double,
    val transformerRequiredKVA: Double,
    val transformerRecommendedKVA: Double,
    val mainBreakerA: Double,
    val breakerBreakingCapacityKA: Double
)

class EngineeringDesignService(
    private val core: ProfessionalEngineeringCore
) {

    fun calculate(
        input: CompleteDesignInput
    ): CompleteDesignResult {

        val network =
            core.network.calculate(
                loads = input.loads,
                voltageV = input.voltageV,
                powerFactor = input.powerFactor,
                diversityFactor = input.diversityFactor
            )

        val transformer =
            core.transformerSizing.calculate(
                TransformerSizingInput(
                    designLoadKW = network.designLoadKW,
                    powerFactor = input.powerFactor,
                    spareCapacityFactor = 1.15
                )
            )

        val breaker =
            core.breakerSelection.calculate(
                BreakerSelectionInput(
                    loadCurrentA = network.mainCurrentA,
                    shortCircuitKA = input.shortCircuitKA,
                    utilizationFactor = 0.80
                )
            )

        return CompleteDesignResult(
            connectedLoadKW = network.connectedLoadKW,
            demandLoadKW = network.demandLoadKW,
            designLoadKW = network.designLoadKW,
            mainCurrentA = network.mainCurrentA,
            transformerRequiredKVA = transformer.requiredKVA,
            transformerRecommendedKVA =
                transformer.recommendedRatingKVA,
            mainBreakerA = breaker.recommendedRatingA,
            breakerBreakingCapacityKA =
                breaker.recommendedBreakingCapacityKA
        )
    }
}

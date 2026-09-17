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

        require(input.voltageV > 0.0) {
            "Voltage must be greater than zero."
        }

        require(input.powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(input.diversityFactor > 0.0) {
            "Diversity factor must be greater than zero."
        }

        val loadResults =
            input.loads.map {
                core.loads.calculate(it)
            }

        val connectedLoadKw =
            loadResults.sumOf { it.connectedKw }

        val demandLoadKw =
            loadResults.sumOf { it.demandKw }

        val designLoadKw =
            demandLoadKw * input.diversityFactor

        val apparentPowerKva =
            if (input.powerFactor > 0.0) {
                designLoadKw / input.powerFactor
            } else {
                0.0
            }

        val mainCurrentA =
            designLoadKw * 1000.0 /
                (kotlin.math.sqrt(3.0) *
                    input.voltageV *
                    input.powerFactor)

        val transformer =
            core.transformerSizing.calculate(
                TransformerSizingInput(
                    designLoadKW = designLoadKw,
                    powerFactor = input.powerFactor,
                    spareCapacityFactor = 1.15
                )
            )

        val breaker =
            core.breakerSelection.calculate(
                BreakerSelectionInput(
                    loadCurrentA = mainCurrentA,
                    shortCircuitKA = input.shortCircuitKA
                )
            )

        return CompleteDesignResult(
            connectedLoadKW = connectedLoadKw,
            demandLoadKW = demandLoadKw,
            designLoadKW = designLoadKw,
            mainCurrentA = mainCurrentA,
            transformerRequiredKVA =
                apparentPowerKva,
            transformerRecommendedKVA =
                transformer.recommendedRatingKVA,
            mainBreakerA =
                breaker.recommendedRatingA,
            breakerBreakingCapacityKA =
                breaker.recommendedBreakingCapacityKA
        )
    }
}

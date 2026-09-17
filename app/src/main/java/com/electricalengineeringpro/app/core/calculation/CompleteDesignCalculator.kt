package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

data class CompleteDesignInput(
    val loads: List<ElectricalLoad>,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.90,
    val diversityFactor: Double = 0.85,
    val shortCircuitKA: Double = 0.0,
    val phase: Phase = Phase.THREE
)

data class CompleteDesignResult(
    val connectedLoadKW: Double,
    val demandLoadKW: Double,
    val designLoadKW: Double,
    val apparentPowerKVA: Double,
    val mainCurrentA: Double,
    val transformerRequiredKVA: Double,
    val transformerRecommendedKVA: Double,
    val mainBreakerA: Double,
    val breakerBreakingCapacityKA: Double
)

class CompleteDesignCalculator(
    private val core: ProfessionalEngineeringCore
) {

    fun calculate(
        input: CompleteDesignInput
    ): CompleteDesignResult {

        require(input.loads.isNotEmpty()) {
            "At least one electrical load is required."
        }

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

        val connectedLoadKW =
            loadResults.sumOf {
                it.connectedKw
            }

        val demandLoadKW =
            loadResults.sumOf {
                it.demandKw
            }

        val designLoadKW =
            demandLoadKW *
                input.diversityFactor

        val apparentPowerKVA =
            designLoadKW /
                input.powerFactor

        val mainCurrentA =
            when (input.phase) {

                Phase.THREE ->
                    designLoadKW * 1000.0 /
                        (
                            sqrt(3.0) *
                                input.voltageV *
                                input.powerFactor
                        )

                Phase.SINGLE ->
                    designLoadKW * 1000.0 /
                        (
                            input.voltageV *
                                input.powerFactor
                        )
            }

        val transformer =
            core.transformerSizing.calculate(
                TransformerSizingInput(
                    designLoadKW = designLoadKW,
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
            connectedLoadKW =
                connectedLoadKW,

            demandLoadKW =
                demandLoadKW,

            designLoadKW =
                designLoadKW,

            apparentPowerKVA =
                apparentPowerKVA,

            mainCurrentA =
                mainCurrentA,

            transformerRequiredKVA =
                apparentPowerKVA,

            transformerRecommendedKVA =
                transformer.recommendedRatingKVA,

            mainBreakerA =
                breaker.recommendedRatingA,

            breakerBreakingCapacityKA =
                breaker.recommendedBreakingCapacityKA
        )
    }
}

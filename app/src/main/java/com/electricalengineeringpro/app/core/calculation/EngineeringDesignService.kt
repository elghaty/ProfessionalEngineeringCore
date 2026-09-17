package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad

data class CompleteDesignInput(
    val loads: List<ElectricalLoad>,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.90,
    val diversityFactor: Double = 1.0,
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
    private val core: ProfessionalEngineeringCore =
        ProfessionalEngineeringCore.instance
) {

    fun calculate(
        input: CompleteDesignInput
    ): CompleteDesignResult {

        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.diversityFactor > 0.0)
        require(input.shortCircuitKA >= 0.0)

        val schedule =
            core.loadSchedule.calculate(input.loads)

        val designLoad =
            schedule.totalDemandKw /
                    input.diversityFactor

        val phase =
            input.loads.firstOrNull()?.phase
                ?: com.electricalengineeringpro.app.core.model.Phase.THREE

        val summary =
            core.designSummary.calculate(
                loads = input.loads,
                voltage = input.voltageV,
                powerFactor = input.powerFactor,
                phase = phase
            )

        val transformer =
            core.transformerSizing.calculate(
                TransformerSizingInput(
                    designLoadKW = designLoad,
                    powerFactor = input.powerFactor,
                    spareCapacityFactor = 1.15
                )
            )

        val breaker =
            core.breakerSelection.calculate(
                BreakerSelectionInput(
                    loadCurrentA = summary.totalCurrentA,
                    shortCircuitKA = input.shortCircuitKA,
                    utilizationFactor = 1.0
                )
            )

        return CompleteDesignResult(
            connectedLoadKW =
                schedule.totalConnectedKw,

            demandLoadKW =
                schedule.totalDemandKw,

            designLoadKW =
                designLoad,

            mainCurrentA =
                summary.totalCurrentA,

            transformerRequiredKVA =
                transformer.requiredKVA,

            transformerRecommendedKVA =
                transformer.recommendedRatingKVA,

            mainBreakerA =
                breaker.recommendedRatingA,

            breakerBreakingCapacityKA =
                breaker.recommendedBreakingCapacityKA
        )
    }
}

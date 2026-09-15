package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.ShortCircuitInput

data class CompleteDesignInput(
    val loads: List<ElectricalLoad>,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.9,
    val transformerImpedancePercent: Double = 6.0
)

data class CompleteDesignResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designCurrentA: Double,
    val transformerKva: Double,
    val mainBreakerA: Double,
    val breakerBreakingCapacityKA: Double,
    val shortCircuitKA: Double
)

class EngineeringDesignService(
    private val core: ProfessionalEngineeringCore =
        ProfessionalEngineeringCore.instance
) {

    fun calculate(
        input: CompleteDesignInput
    ): CompleteDesignResult {

        require(input.loads.isNotEmpty()) {
            "At least one electrical load is required."
        }

        val network =
            core.network.calculate(
                loads = input.loads,
                voltageV = input.voltageV,
                powerFactor = input.powerFactor
            )

        val transformer =
            core.transformerSizing.calculate(
                TransformerSizingInput(
                    demandLoadKw =
                        network.totalDemandKw,
                    powerFactor =
                        input.powerFactor,
                    spareCapacity = 0.20
                )
            )

        val shortCircuit =
            core.shortCircuit.calculate(
                ShortCircuitInput(
                    transformerKva =
                        transformer.selectedKva,
                    transformerImpedancePercent =
                        input.transformerImpedancePercent,
                    voltageV =
                        input.voltageV,
                    phase = Phase.THREE_PHASE
                )
            )

        val breaker =
            core.breakerSelection.calculate(
                BreakerSelectionInput(
                    designCurrentA =
                        network.mainCurrentA,
                    shortCircuitKA =
                        shortCircuit.prospectiveFaultCurrentKA
                )
            )

        return CompleteDesignResult(
            connectedLoadKw =
                network.totalConnectedKw,
            demandLoadKw =
                network.totalDemandKw,
            designCurrentA =
                network.mainCurrentA,
            transformerKva =
                transformer.selectedKva,
            mainBreakerA =
                breaker.ratedCurrentA,
            breakerBreakingCapacityKA =
                breaker.breakingCapacityKA,
            shortCircuitKA =
                shortCircuit.prospectiveFaultCurrentKA
        )
    }
}

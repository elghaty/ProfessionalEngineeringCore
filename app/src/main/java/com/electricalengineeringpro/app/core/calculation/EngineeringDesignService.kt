package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad

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
    val shortCircuitKA: Double
)

class EngineeringDesignService(
    private val core: ProfessionalEngineeringCore =
        ProfessionalEngineeringCore.instance
) {

    fun calculate(
        input: CompleteDesignInput
    ): CompleteDesignResult {

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
                com.electricalengineeringpro.app.core.model.ShortCircuitInput(
                    transformerKva =
                        transformer.selectedKva,
                    transformerImpedancePercent =
                        input.transformerImpedancePercent,
                    voltageV =
                        input.voltageV,
                    phase =
                        com.electricalengineeringpro.app.core.model.Phase.THREE_PHASE
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
            shortCircuitKA =
                shortCircuit.prospectiveFaultCurrentKA
        )
    }
}

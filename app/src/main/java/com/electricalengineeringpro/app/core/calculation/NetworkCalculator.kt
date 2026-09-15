package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad

data class NetworkCalculationResult(
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val estimatedMainCurrentA: Double,
    val estimatedTransformerKva: Double,
    val estimatedShortCircuitKA: Double
)

class NetworkCalculator(
    private val loadCalculator: LoadCalculator,
    private val designSummaryCalculator: DesignSummaryCalculator,
    private val shortCircuitCalculator: ShortCircuitCalculator
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.9,
        transformerKva: Double = 0.0,
        transformerImpedancePercent: Double = 6.0
    ): NetworkCalculationResult {

        val loadResults =
            loads.map { loadCalculator.calculate(it) }

        val totalConnected =
            loadResults.sumOf { it.connectedLoadKw }

        val totalDemand =
            loadResults.sumOf { it.demandLoadKw }

        val summary =
            designSummaryCalculator.calculate(loads)

        val shortCircuit =
            if (transformerKva > 0.0) {
                shortCircuitCalculator.calculate(
                    com.electricalengineeringpro.app.core.model.ShortCircuitInput(
                        transformerKva = transformerKva,
                        transformerImpedancePercent =
                            transformerImpedancePercent,
                        voltageV = voltageV,
                        phase =
                            com.electricalengineeringpro.app.core.model.Phase.THREE_PHASE
                    )
                )
            } else {
                null
            }

        return NetworkCalculationResult(
            totalConnectedKw = totalConnected,
            totalDemandKw = totalDemand,
            estimatedMainCurrentA = summary.designCurrentA,
            estimatedTransformerKva = summary.recommendedTransformerKva,
            estimatedShortCircuitKA =
                shortCircuit?.prospectiveFaultCurrentKA ?: 0.0
        )
    }
}

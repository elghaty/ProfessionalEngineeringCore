package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.ShortCircuitInput

data class NetworkCalculationResult(
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val estimatedMainCurrentA: Double,
    val estimatedTransformerKva: Double,
    val estimatedShortCircuitKA: Double
)

class NetworkCalculator(
    private val loadCalculator: LoadCalculator = LoadCalculator(),
    private val designSummaryCalculator: DesignSummaryCalculator =
        DesignSummaryCalculator(),
    private val shortCircuitCalculator: ShortCircuitCalculator =
        ShortCircuitCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        transformerKva: Double = 0.0,
        transformerImpedancePercent: Double = 6.0
    ): NetworkCalculationResult {

        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)
        require(transformerKva >= 0.0)
        require(transformerImpedancePercent > 0.0)

        val loadResults =
            loads.map {
                loadCalculator.calculate(it)
            }

        val totalConnected =
            loadResults.sumOf {
                it.connectedKw
            }

        val totalDemand =
            loadResults.sumOf {
                it.demandKw
            }

        val phase =
            loads.firstOrNull()?.phase ?: Phase.THREE

        val summary =
            designSummaryCalculator.calculate(
                loads = loads,
                voltage = voltageV,
                powerFactor = powerFactor,
                phase = phase
            )

        val shortCircuit =
            if (transformerKva > 0.0) {

                shortCircuitCalculator.calculate(
                    ShortCircuitInput(
                        sourceVoltage = voltageV,
                        transformerKva = transformerKva,
                        transformerImpedancePercent =
                            transformerImpedancePercent
                    )
                )

            } else {
                null
            }

        return NetworkCalculationResult(
            totalConnectedKw = totalConnected,
            totalDemandKw = totalDemand,
            estimatedMainCurrentA = summary.totalCurrentA,
            estimatedTransformerKva =
                if (transformerKva > 0.0) {
                    transformerKva
                } else {
                    summary.recommendedTransformerKva
                },
            estimatedShortCircuitKA =
                shortCircuit?.faultCurrentKA ?: 0.0
        )
    }
}

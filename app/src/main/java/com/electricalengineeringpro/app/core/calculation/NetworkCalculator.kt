package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.ShortCircuitInput

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
        powerFactor: Double = 0.90,
        transformerKva: Double = 0.0,
        transformerImpedancePercent: Double = 6.0
    ): NetworkCalculationResult {

        require(voltageV > 0.0) {
            "System voltage must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(transformerKva >= 0.0) {
            "Transformer rating cannot be negative."
        }

        require(transformerImpedancePercent > 0.0) {
            "Transformer impedance must be greater than zero."
        }

        val loadResults = loads.map {
            loadCalculator.calculate(it)
        }

        val totalConnectedKw =
            loadResults.sumOf { it.connectedKw }

        val totalDemandKw =
            loadResults.sumOf { it.demandKw }

        val summary =
            designSummaryCalculator.calculate(loads)

        val shortCircuitResult =
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
            totalConnectedKw = totalConnectedKw,
            totalDemandKw = totalDemandKw,
            estimatedMainCurrentA =
                summary.totalCurrentA,
            estimatedTransformerKva =
                summary.recommendedTransformerKva,
            estimatedShortCircuitKA =
                shortCircuitResult?.faultCurrentKA ?: 0.0
        )
    }
}

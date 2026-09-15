package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad

data class ElectricalNetworkResult(
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val totalDesignKw: Double,
    val totalKva: Double,
    val mainCurrentA: Double,
    val recommendedTransformerKva: Double,
    val recommendedMainBreakerA: Double
)

class ElectricalNetworkCalculator(
    private val summary:
        DesignSummaryCalculator =
        DesignSummaryCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90
    ): ElectricalNetworkResult {

        val result =
            summary.calculate(
                loads = loads,
                voltageV = voltageV,
                powerFactor = powerFactor
            )

        return ElectricalNetworkResult(
            totalConnectedKw =
                result.connectedLoadKw,
            totalDemandKw =
                result.demandLoadKw,
            totalDesignKw =
                result.designLoadKw,
            totalKva =
                result.designKva,
            mainCurrentA =
                result.designCurrentA,
            recommendedTransformerKva =
                result.recommendedTransformerKva,
            recommendedMainBreakerA =
                result.recommendedMainBreakerA
        )
    }
}

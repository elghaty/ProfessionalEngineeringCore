package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import kotlin.math.sqrt

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
    private val summary: DesignSummaryCalculator =
        DesignSummaryCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): ElectricalNetworkResult {

        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)

        val result =
            summary.calculate(
                loads = loads,
                voltageV = voltageV,
                powerFactor = powerFactor,
                designMargin = designMargin
            )

        val current =
            if (result.designKva > 0.0) {
                result.designKva * 1000.0 /
                    (sqrt(3.0) * voltageV)
            } else {
                0.0
            }

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
                current,
            recommendedTransformerKva =
                result.recommendedTransformerKva,
            recommendedMainBreakerA =
                result.recommendedMainBreakerA
        )
    }
}

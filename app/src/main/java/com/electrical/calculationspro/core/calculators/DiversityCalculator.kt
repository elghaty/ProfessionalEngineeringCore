package com.electrical.calculationspro.core.calculators

import kotlin.math.max

data class DiversityLoad(
    val connectedKw: Double,
    val demandFactor: Double = 1.0
)

data class DiversityResult(
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val effectiveDiversityFactor: Double,
    val utilizationPercent: Double,
    val notes: List<String>
)

class DiversityCalculator {

    fun calculate(
        loads: List<DiversityLoad>,
        additionalDiversityFactor: Double = 1.0
    ): DiversityResult {

        require(additionalDiversityFactor > 0.0) {
            "Additional diversity factor must be greater than zero."
        }

        require(additionalDiversityFactor <= 1.0) {
            "Additional diversity factor must not exceed 1.0."
        }

        loads.forEach {
            require(it.connectedKw >= 0.0) {
                "Connected load cannot be negative."
            }

            require(it.demandFactor >= 0.0) {
                "Demand factor cannot be negative."
            }

            require(it.demandFactor <= 1.0) {
                "Demand factor cannot exceed 1.0."
            }
        }

        val connected =
            loads.sumOf {
                it.connectedKw
            }

        val demandBeforeDiversity =
            loads.sumOf {
                it.connectedKw * it.demandFactor
            }

        val totalDemand =
            demandBeforeDiversity *
                additionalDiversityFactor

        val effectiveDiversity =
            if (connected > 0.0) {
                totalDemand / connected
            } else {
                0.0
            }

        val utilization =
            if (connected > 0.0) {
                totalDemand /
                    connected *
                    100.0
            } else {
                0.0
            }

        return DiversityResult(
            totalConnectedKw = connected,
            totalDemandKw = totalDemand,
            effectiveDiversityFactor =
                effectiveDiversity.coerceIn(
                    0.0,
                    1.0
                ),
            utilizationPercent =
                max(
                    0.0,
                    utilization
                ),
            notes = listOf(
                "Connected load = %.2f kW"
                    .format(connected),

                "Demand before diversity = %.2f kW"
                    .format(demandBeforeDiversity),

                "Applied diversity factor = %.3f"
                    .format(additionalDiversityFactor),

                "Final demand load = %.2f kW"
                    .format(totalDemand),

                "Effective utilization = %.2f %%"
                    .format(utilization)
            )
        )
    }
}

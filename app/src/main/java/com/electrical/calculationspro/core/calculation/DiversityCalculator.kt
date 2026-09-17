package com.electrical.calculationspro.core.calculation

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

        require(additionalDiversityFactor in 0.0..1.0) {
            "Diversity factor must be between 0 and 1."
        }

        loads.forEach {
            require(it.connectedKw >= 0.0) {
                "Connected load must be >= 0 kW."
            }

            require(it.demandFactor in 0.0..1.0) {
                "Demand factor must be between 0 and 1."
            }
        }

        val connected =
            loads.sumOf { it.connectedKw }

        val demandBeforeDiversity =
            loads.sumOf {
                it.connectedKw * it.demandFactor
            }

        val finalDemand =
            demandBeforeDiversity *
                additionalDiversityFactor

        val effective =
            if (connected > 0.0) {
                finalDemand / connected
            } else {
                0.0
            }

        return DiversityResult(
            totalConnectedKw = connected,
            totalDemandKw = finalDemand,
            effectiveDiversityFactor =
                effective.coerceIn(0.0, 1.0),
            utilizationPercent =
                if (connected > 0.0) {
                    finalDemand / connected * 100.0
                } else {
                    0.0
                },
            notes = listOf(
                "Connected load = %.2f kW"
                    .format(connected),
                "Demand before diversity = %.2f kW"
                    .format(demandBeforeDiversity),
                "Applied diversity factor = %.3f"
                    .format(additionalDiversityFactor),
                "Final demand load = %.2f kW"
                    .format(finalDemand)
            )
        )
    }
}

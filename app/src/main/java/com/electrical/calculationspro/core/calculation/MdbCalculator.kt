package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import kotlin.math.sqrt

data class MdbInput(
    val name: String,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.90,
    val designMargin: Double = 1.15,
    val loads: List<ElectricalLoad> = emptyList()
)

data class MdbResult(
    val name: String,
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val designKva: Double,
    val incomerCurrentA: Double,
    val recommendedIncomerA: Double,
    val recommendedTransformerKva: Double,
    val outgoingCount: Int,
    val notes: List<String>
)

class MdbCalculator(
    private val summary:
        DesignSummaryCalculator =
        DesignSummaryCalculator()
) {

    fun calculate(
        input: MdbInput
    ): MdbResult {

        require(input.name.isNotBlank())
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.designMargin >= 1.0)

        val result =
            summary.calculate(
                loads = input.loads,
                voltageV = input.voltageV,
                powerFactor = input.powerFactor,
                designMargin = input.designMargin
            )

        val current =
            if (result.apparentPowerKva > 0.0) {
                result.apparentPowerKva *
                    1000.0 /
                    (
                        sqrt(3.0) *
                            input.voltageV
                        )
            } else {
                0.0
            }

        return MdbResult(
            name = input.name,
            connectedLoadKw =
                result.connectedLoadKw,
            demandLoadKw =
                result.demandLoadKw,
            designLoadKw =
                result.designLoadKw,
            designKva =
                result.apparentPowerKva,
            incomerCurrentA =
                current,
            recommendedIncomerA =
                result.recommendedMainBreakerA,
            recommendedTransformerKva =
                result.recommendedTransformerKva,
            outgoingCount =
                input.loads.size,
            notes = listOf(
                "MDB = ${input.name}",
                "Connected load = %.2f kW"
                    .format(result.connectedLoadKw),
                "Demand load = %.2f kW"
                    .format(result.demandLoadKw),
                "Design load = %.2f kW"
                    .format(result.designLoadKw),
                "Design power = %.2f kVA"
                    .format(result.apparentPowerKva),
                "Incomer current = %.2f A"
                    .format(current),
                "Recommended incomer = %.0f A"
                    .format(result.recommendedMainBreakerA),
                "Recommended transformer = %.0f kVA"
                    .format(result.recommendedTransformerKva)
            )
        )
    }
}

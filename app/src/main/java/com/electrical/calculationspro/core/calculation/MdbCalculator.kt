package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import kotlin.math.sqrt

data class MdbInput(
    val loads: List<ElectricalLoad>,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.90,
    val designMargin: Double = 1.15
)

data class MdbResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val designKva: Double,
    val incomerCurrentA: Double,
    val recommendedIncomerA: Double,
    val recommendedTransformerKva: Double,
    val outgoingCount: Int
)

class MdbCalculator(
    private val summary:
        DesignSummaryCalculator =
        DesignSummaryCalculator()
) {

    private val breakerRatings =
        listOf(
            16.0,
            20.0,
            25.0,
            32.0,
            40.0,
            50.0,
            63.0,
            80.0,
            100.0,
            125.0,
            160.0,
            200.0,
            250.0,
            315.0,
            400.0,
            500.0,
            630.0,
            800.0,
            1000.0,
            1250.0,
            1600.0,
            2000.0,
            2500.0,
            3200.0,
            4000.0,
            5000.0,
            6300.0
        )

    fun calculate(
        input: MdbInput
    ): MdbResult {

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

        val incomer =
            breakerRatings.firstOrNull {
                it >= result.designCurrentA
            } ?: result.designCurrentA

        return MdbResult(
            connectedLoadKw =
                result.connectedLoadKw,
            demandLoadKw =
                result.demandLoadKw,
            designLoadKw =
                result.designLoadKw,
            designKva =
                result.designKva,
            incomerCurrentA =
                result.designCurrentA,
            recommendedIncomerA =
                incomer,
            recommendedTransformerKva =
                result.recommendedTransformerKva,
            outgoingCount =
                input.loads.size
        )
    }
}

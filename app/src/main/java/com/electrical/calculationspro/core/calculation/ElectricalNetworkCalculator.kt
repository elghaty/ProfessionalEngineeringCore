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
    val recommendedMainBreakerA: Double,
    val feederResults: List<LoadResult>
)

class ElectricalNetworkCalculator(
    private val loadScheduleCalculator:
        LoadScheduleCalculator =
        LoadScheduleCalculator(),

    private val transformerSizingCalculator:
        TransformerSizingCalculator =
        TransformerSizingCalculator(),

    private val breakerCalculator:
        BreakerCalculator =
        BreakerCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): ElectricalNetworkResult {

        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)
        require(designMargin >= 1.0)

        val schedule =
            loadScheduleCalculator.calculate(
                loads
            )

        val designKva =
            schedule.designLoadKw /
                powerFactor *
                designMargin

        val mainCurrent =
            if (designKva > 0.0) {
                designKva * 1000.0 /
                    (
                        sqrt(3.0) *
                            voltageV
                        )
            } else {
                0.0
            }

        val transformer =
            transformerSizingCalculator.calculate(
                TransformerSizingInput(
                    designLoadKw =
                        schedule.designLoadKw,
                    powerFactor =
                        powerFactor,
                    designMargin =
                        designMargin
                )
            )

        val mainBreaker =
            breakerCalculator.selectRating(
                mainCurrent
            )

        return ElectricalNetworkResult(
            totalConnectedKw =
                schedule.connectedLoadKw,

            totalDemandKw =
                schedule.demandLoadKw,

            totalDesignKw =
                schedule.designLoadKw,

            totalKva =
                designKva,

            mainCurrentA =
                mainCurrent,

            recommendedTransformerKva =
                transformer.recommendedRatingKva,

            recommendedMainBreakerA =
                mainBreaker,

            feederResults =
                schedule.individualResults
        )
    }
}

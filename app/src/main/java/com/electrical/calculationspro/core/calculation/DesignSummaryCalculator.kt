package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import kotlin.math.sqrt

class DesignSummaryCalculator(
    private val loadScheduleCalculator:
        LoadScheduleCalculator =
        LoadScheduleCalculator()
) {

    private val transformerRatingsKva =
        doubleArrayOf(
            50.0,
            100.0,
            160.0,
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
            3150.0,
            4000.0,
            5000.0,
            6300.0,
            8000.0,
            10000.0,
            12500.0,
            16000.0
        )

    private val breakerRatingsA =
        doubleArrayOf(
            6.0,
            10.0,
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
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): ProjectSummary {

        require(loads.isNotEmpty()) {
            "Project must contain at least one load."
        }

        require(voltageV > 0.0) {
            "Voltage must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(designMargin >= 1.0) {
            "Design margin must be >= 1.0."
        }

        val schedule =
            loadScheduleCalculator.calculate(loads)

        val designLoadKw =
            schedule.designLoadKw * designMargin

        val designKva =
            if (powerFactor > 0.0) {
                designLoadKw / powerFactor
            } else {
                0.0
            }

        val calculatedMainCurrentA =
            if (designKva > 0.0) {
                designKva * 1000.0 /
                    (
                        sqrt(3.0) *
                            voltageV
                        )
            } else {
                0.0
            }

        val recommendedTransformer =
            transformerRatingsKva.firstOrNull {
                it >= designKva
            } ?: transformerRatingsKva.last()

        val recommendedBreaker =
            breakerRatingsA.firstOrNull {
                it >= calculatedMainCurrentA
            } ?: breakerRatingsA.last()

        return ProjectSummary(
            connectedLoadKw =
                schedule.connectedLoadKw,

            demandLoadKw =
                schedule.demandLoadKw,

            designLoadKw =
                designLoadKw,

            apparentPowerKva =
                designKva,

            mainCurrentA =
                calculatedMainCurrentA,

            recommendedTransformerKva =
                recommendedTransformer,

            recommendedMainBreakerA =
                recommendedBreaker
        )
    }
}

data class ProjectSummary(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val apparentPowerKva: Double,
    val mainCurrentA: Double,
    val recommendedTransformerKva: Double,
    val recommendedMainBreakerA: Double
)

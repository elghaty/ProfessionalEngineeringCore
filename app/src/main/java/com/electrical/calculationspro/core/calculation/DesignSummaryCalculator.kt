package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import kotlin.math.ceil

data class DesignSummaryResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val designKva: Double,
    val designCurrentA: Double,
    val recommendedTransformerKva: Double,
    val recommendedMainBreakerA: Double
)

class DesignSummaryCalculator(
    private val schedule:
        LoadScheduleCalculator =
        LoadScheduleCalculator()
) {

    private val transformerRatings =
        listOf(
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
            5000.0
        )

    private val breakerRatings =
        listOf(
            16.0, 20.0, 25.0, 32.0,
            40.0, 50.0, 63.0, 80.0,
            100.0, 125.0, 160.0,
            200.0, 250.0, 315.0,
            400.0, 500.0, 630.0,
            800.0, 1000.0, 1250.0,
            1600.0, 2000.0, 2500.0
        )

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): DesignSummaryResult {

        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)
        require(designMargin >= 1.0)

        val result =
            schedule.calculate(loads)

        val designKw =
            result.designLoadKw *
                designMargin

        val kva =
            designKw /
                powerFactor

        val current =
            kva * 1000.0 /
                (kotlin.math.sqrt(3.0) *
                    voltageV)

        val transformer =
            transformerRatings.firstOrNull {
                it >= kva
            } ?: ceil(kva / 100.0) * 100.0

        val breaker =
            breakerRatings.firstOrNull {
                it >= current
            } ?: ceil(current / 100.0) * 100.0

        return DesignSummaryResult(
            connectedLoadKw =
                result.connectedLoadKw,
            demandLoadKw =
                result.demandLoadKw,
            designLoadKw =
                designKw,
            designKva = kva,
            designCurrentA = current,
            recommendedTransformerKva =
                transformer,
            recommendedMainBreakerA =
                breaker
        )
    }
}

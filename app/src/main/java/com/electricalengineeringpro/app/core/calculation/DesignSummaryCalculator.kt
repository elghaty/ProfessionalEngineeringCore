package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.DesignSummary
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

class DesignSummaryCalculator {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): DesignSummary {

        require(voltage > 0.0)
        require(powerFactor in 0.01..1.0)

        val calculator = LoadCalculator()

        val results = loads.map {
            calculator.calculate(it)
        }

        val connected = results.sumOf { it.connectedKw }
        val demand = results.sumOf { it.demandKw }
        val design = results.sumOf { it.designKw }

        val current = if (phase == Phase.THREE) {
            design * 1000.0 /
                    (sqrt(3.0) * voltage * powerFactor)
        } else {
            design * 1000.0 /
                    (voltage * powerFactor)
        }

        val breakerRatings = listOf(
            6.0, 10.0, 16.0, 20.0, 25.0, 32.0, 40.0, 50.0,
            63.0, 80.0, 100.0, 125.0, 160.0, 200.0, 250.0,
            315.0, 400.0, 500.0, 630.0, 800.0, 1000.0,
            1250.0, 1600.0, 2000.0, 2500.0, 3200.0
        )

        val mainBreaker = breakerRatings.firstOrNull {
            it >= current
        } ?: breakerRatings.last()

        val transformerKva =
            design / powerFactor * 1.15

        return DesignSummary(
            connectedLoadKw = connected,
            demandLoadKw = demand,
            designLoadKw = design,
            totalCurrentA = current,
            recommendedMainBreakerA = mainBreaker,
            recommendedTransformerKva = transformerKva
        )
    }
}

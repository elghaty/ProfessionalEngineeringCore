package com.electricalengineeringpro.app.core.network

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import kotlin.math.sqrt

data class NetworkCalculationResult(
    val connectedLoadKW: Double,
    val demandLoadKW: Double,
    val diversityFactor: Double,
    val designLoadKW: Double,
    val mainCurrentA: Double,
    val recommendedTransformerKVA: Double
)

class NetworkCalculator {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double,
        powerFactor: Double = 0.90,
        diversityFactor: Double = 0.85
    ): NetworkCalculationResult {

        require(voltageV > 0.0)
        require(powerFactor > 0.0 && powerFactor <= 1.0)
        require(diversityFactor > 0.0 && diversityFactor <= 1.0)

        val connected = loads.sumOf {
            it.quantity.coerceAtLeast(1) *
                it.powerKW.coerceAtLeast(0.0)
        }

        val demand = connected * diversityFactor
        val design = demand * 1.15

        val current = if (voltageV <= 0.0) {
            0.0
        } else {
            design * 1000.0 /
                (sqrt(3.0) * voltageV * powerFactor)
        }

        val transformerKVA =
            (design / powerFactor * 1.15)
                .coerceAtLeast(0.0)

        return NetworkCalculationResult(
            connectedLoadKW = connected,
            demandLoadKW = demand,
            diversityFactor = diversityFactor,
            designLoadKW = design,
            mainCurrentA = current,
            recommendedTransformerKVA = transformerKVA
        )
    }
}

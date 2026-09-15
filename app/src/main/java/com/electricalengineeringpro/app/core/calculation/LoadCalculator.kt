package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

data class LoadResult(
    val connectedKw: Double,
    val demandKw: Double,
    val designKw: Double,
    val currentA: Double
)

class LoadCalculator {

    fun calculate(load: ElectricalLoad): LoadResult {
        require(load.quantity > 0)
        require(load.powerKw >= 0.0)
        require(load.powerFactor in 0.01..1.0)
        require(load.efficiency in 0.01..1.0)
        require(load.demandFactor in 0.0..1.0)
        require(load.diversityFactor > 0.0)
        require(load.voltage > 0.0)

        val connected = load.powerKw * load.quantity
        val demand = connected * load.demandFactor
        val design = demand / load.diversityFactor

        val current = if (load.phase == Phase.THREE) {
            design * 1000.0 / (sqrt(3.0) * load.voltage * load.powerFactor)
        } else {
            design * 1000.0 / (load.voltage * load.powerFactor)
        }

        return LoadResult(
            connectedKw = connected,
            demandKw = demand,
            designKw = design,
            currentA = current
        )
    }
}

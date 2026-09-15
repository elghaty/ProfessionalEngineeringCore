package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.LoadResult

class LoadCalculator(
    private val powerCalculator: PowerCalculator =
        PowerCalculator()
) {

    fun calculate(
        load: ElectricalLoad
    ): LoadResult {

        require(load.id.isNotBlank())
        require(load.name.isNotBlank())
        require(load.powerKw >= 0.0)
        require(load.quantity > 0)
        require(load.voltageV > 0.0)
        require(load.powerFactor in 0.01..1.0)
        require(load.efficiency in 0.01..1.0)
        require(load.demandFactor in 0.0..1.0)
        require(load.diversityFactor > 0.0)

        val connectedKw =
            load.powerKw * load.quantity

        val demandKw =
            connectedKw * load.demandFactor

        val designKw =
            demandKw / load.diversityFactor

        val electricalInputKw =
            designKw / load.efficiency

        val power =
            powerCalculator.fromKw(
                powerKw = electricalInputKw,
                voltageV = load.voltageV,
                powerFactor = load.powerFactor,
                phase = load.phase
            )

        return LoadResult(
            connectedKw = connectedKw,
            demandKw = demandKw,
            designKw = designKw,
            currentA = power.currentA,
            apparentPowerKva = power.apparentPowerKva
        )
    }

    fun calculateAll(
        loads: List<ElectricalLoad>
    ): List<LoadResult> =
        loads.map(::calculate)
}

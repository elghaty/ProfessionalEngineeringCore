package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.LoadResult
import kotlin.math.sqrt

class LoadCalculator {

    fun calculate(load: ElectricalLoad): LoadResult {

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

        val electricalKw =
            designKw / load.efficiency

        val currentA =
            when (load.phase) {

                com.electrical.calculationspro.core.model.Phase.DC ->
                    electricalKw * 1000.0 /
                        load.voltageV

                com.electrical.calculationspro.core.model.Phase.SINGLE ->
                    electricalKw * 1000.0 /
                        (load.voltageV * load.powerFactor)

                com.electrical.calculationspro.core.model.Phase.TWO ->
                    electricalKw * 1000.0 /
                        (2.0 * load.voltageV * load.powerFactor)

                com.electrical.calculationspro.core.model.Phase.THREE ->
                    electricalKw * 1000.0 /
                        (sqrt(3.0) *
                            load.voltageV *
                            load.powerFactor)
            }

        return LoadResult(
            connectedKw = connectedKw,
            demandKw = demandKw,
            designKw = designKw,
            currentA = currentA,
            apparentPowerKva =
                electricalKw / load.powerFactor
        )
    }

    fun calculateAll(
        loads: List<ElectricalLoad>
    ): List<LoadResult> =
        loads.map { calculate(it) }
}

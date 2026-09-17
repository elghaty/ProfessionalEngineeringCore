package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad

class LoadScheduleCalculator(
    private val loadCalculator: LoadCalculator =
        LoadCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>
    ): LoadScheduleResult {

        require(loads.isNotEmpty()) {
            "Load schedule must contain at least one load."
        }

        val results =
            loads.map { loadCalculator.calculate(it) }

        val connectedLoadKw =
            results.sumOf { it.connectedKw }

        val demandLoadKw =
            results.sumOf { it.demandKw }

        val designLoadKw =
            results.sumOf { it.designKw }

        val apparentPowerKva =
            results.sumOf { it.apparentPowerKva }

        val effectiveDemandFactor =
            if (connectedLoadKw > 0.0) {
                demandLoadKw / connectedLoadKw
            } else {
                0.0
            }

        val effectiveDesignFactor =
            if (connectedLoadKw > 0.0) {
                designLoadKw / connectedLoadKw
            } else {
                0.0
            }

        return LoadScheduleResult(
            connectedLoadKw = connectedLoadKw,
            demandLoadKw = demandLoadKw,
            designLoadKw = designLoadKw,
            apparentPowerKva = apparentPowerKva,
            effectiveDemandFactor =
                effectiveDemandFactor.coerceIn(0.0, 1.0),
            effectiveDesignFactor =
                effectiveDesignFactor.coerceIn(0.0, 1.0),
            loadResults = results
        )
    }
}

data class LoadScheduleResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val apparentPowerKva: Double,
    val effectiveDemandFactor: Double,
    val effectiveDesignFactor: Double,
    val loadResults: List<LoadResult>
)

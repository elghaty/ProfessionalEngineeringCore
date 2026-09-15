package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad

data class LoadScheduleResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val apparentPowerKva: Double,
    val totalCurrentA: Double,
    val individualResults: List<LoadResult>
)

class LoadScheduleCalculator(
    private val loadCalculator: LoadCalculator =
        LoadCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>
    ): LoadScheduleResult {

        val results =
            loadCalculator.calculateAll(loads)

        return LoadScheduleResult(
            connectedLoadKw =
                results.sumOf { it.connectedKw },

            demandLoadKw =
                results.sumOf { it.demandKw },

            designLoadKw =
                results.sumOf { it.designKw },

            apparentPowerKva =
                results.sumOf { it.apparentPowerKva },

            totalCurrentA =
                results.sumOf { it.currentA },

            individualResults = results
        )
    }
}

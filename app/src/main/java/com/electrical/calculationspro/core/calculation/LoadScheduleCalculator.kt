package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad

data class LoadScheduleResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val totalCurrentA: Double,
    val totalKva: Double,
    val loadResults: List<
        com.electrical.calculationspro.core.model.LoadResult
    >
)

class LoadScheduleCalculator(
    private val loadCalculator:
        LoadCalculator =
        LoadCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>
    ): LoadScheduleResult {

        val results =
            loads.map {
                loadCalculator.calculate(it)
            }

        return LoadScheduleResult(
            connectedLoadKw =
                results.sumOf {
                    it.connectedKw
                },
            demandLoadKw =
                results.sumOf {
                    it.demandKw
                },
            designLoadKw =
                results.sumOf {
                    it.designKw
                },
            totalCurrentA =
                results.sumOf {
                    it.currentA
                },
            totalKva =
                results.sumOf {
                    it.apparentPowerKva
                },
            loadResults = results
        )
    }
}

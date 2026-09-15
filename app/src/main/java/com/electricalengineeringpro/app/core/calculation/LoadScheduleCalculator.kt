package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad

data class LoadScheduleRow(
    val load: ElectricalLoad,
    val connectedKw: Double,
    val demandKw: Double,
    val designCurrentA: Double
)

data class LoadScheduleResult(
    val rows: List<LoadScheduleRow>,
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val maximumDesignCurrentA: Double
)

class LoadScheduleCalculator(
    private val loadCalculator: LoadCalculator
) {

    fun calculate(loads: List<ElectricalLoad>): LoadScheduleResult {

        val rows = loads.map { load ->

            val result = loadCalculator.calculate(load)

            LoadScheduleRow(
                load = load,
                connectedKw = result.connectedLoadKw,
                demandKw = result.demandLoadKw,
                designCurrentA = result.designCurrentA
            )
        }

        return LoadScheduleResult(
            rows = rows,
            totalConnectedKw = rows.sumOf { it.connectedKw },
            totalDemandKw = rows.sumOf { it.demandKw },
            maximumDesignCurrentA =
                rows.maxOfOrNull { it.designCurrentA } ?: 0.0
        )
    }
}

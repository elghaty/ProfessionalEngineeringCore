package com.electricalengineeringpro.app.core.schedule

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import kotlin.math.sqrt

data class LoadScheduleRow(
    val id: String,
    val name: String,
    val loadType: String,
    val quantity: Int,
    val unitPowerKW: Double,
    val connectedPowerKW: Double,
    val demandFactor: Double,
    val demandPowerKW: Double,
    val currentA: Double
)

data class LoadScheduleResult(
    val rows: List<LoadScheduleRow>,
    val connectedLoadKW: Double,
    val demandLoadKW: Double,
    val totalCurrentA: Double
)

class LoadScheduleCalculator {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90
    ): LoadScheduleResult {

        require(voltageV > 0.0)
        require(powerFactor > 0.0 && powerFactor <= 1.0)

        val rows = loads.map { load ->

            val quantity = load.quantity.coerceAtLeast(1)

            val connected =
                load.powerKW.coerceAtLeast(0.0) * quantity

            val demand =
                connected *
                    load.demandFactor.coerceIn(0.0, 1.0)

            val current =
                if (voltageV > 0.0) {
                    demand * 1000.0 /
                        (sqrt(3.0) * voltageV * powerFactor)
                } else {
                    0.0
                }

            LoadScheduleRow(
                id = load.id,
                name = load.name,
                loadType = load.loadType.name,
                quantity = quantity,
                unitPowerKW = load.powerKW,
                connectedPowerKW = connected,
                demandFactor = load.demandFactor,
                demandPowerKW = demand,
                currentA = current
            )
        }

        val connectedTotal =
            rows.sumOf { it.connectedPowerKW }

        val demandTotal =
            rows.sumOf { it.demandPowerKW }

        val totalCurrent =
            if (voltageV > 0.0) {
                demandTotal * 1000.0 /
                    (sqrt(3.0) * voltageV * powerFactor)
            } else {
                0.0
            }

        return LoadScheduleResult(
            rows = rows,
            connectedLoadKW = connectedTotal,
            demandLoadKW = demandTotal,
            totalCurrentA = totalCurrent
        )
    }
}

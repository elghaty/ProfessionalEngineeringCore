package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad

data class LoadScheduleRow(
    val load: ElectricalLoad,
    val connectedKw: Double,
    val demandKw: Double,
    val designKw: Double,
    val apparentPowerKva: Double,
    val reactivePowerKvar: Double,
    val designCurrentA: Double,
    val startingCurrentA: Double
)

data class LoadScheduleResult(
    val rows: List<LoadScheduleRow>,
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val totalDesignKw: Double,
    val totalApparentPowerKva: Double,
    val totalReactivePowerKvar: Double,
    val totalDesignCurrentA: Double,
    val maximumDesignCurrentA: Double,
    val maximumStartingCurrentA: Double,
    val overallPowerFactor: Double
)

class LoadScheduleCalculator(
    private val loadCalculator: LoadCalculator = LoadCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>
    ): LoadScheduleResult {

        val rows =
            loads.map { load ->

                val result =
                    loadCalculator.calculate(load)

                LoadScheduleRow(
                    load = load,
                    connectedKw = result.connectedKw,
                    demandKw = result.demandKw,
                    designKw = result.designKw,
                    apparentPowerKva = result.apparentPowerKva,
                    reactivePowerKvar = result.reactivePowerKvar,
                    designCurrentA = result.currentA,
                    startingCurrentA = result.startingCurrentA
                )
            }

        val totalConnectedKw =
            rows.sumOf { it.connectedKw }

        val totalDemandKw =
            rows.sumOf { it.demandKw }

        val totalDesignKw =
            rows.sumOf { it.designKw }

        val totalApparentPowerKva =
            rows.sumOf { it.apparentPowerKva }

        val totalReactivePowerKvar =
            rows.sumOf { it.reactivePowerKvar }

        val totalDesignCurrentA =
            rows.sumOf { it.designCurrentA }

        val maximumDesignCurrentA =
            rows.maxOfOrNull { it.designCurrentA } ?: 0.0

        val maximumStartingCurrentA =
            rows.maxOfOrNull { it.startingCurrentA } ?: 0.0

        val overallPowerFactor =
            if (totalApparentPowerKva > 0.0) {
                totalDesignKw / totalApparentPowerKva
            } else {
                1.0
            }

        return LoadScheduleResult(
            rows = rows,
            totalConnectedKw = totalConnectedKw,
            totalDemandKw = totalDemandKw,
            totalDesignKw = totalDesignKw,
            totalApparentPowerKva = totalApparentPowerKva,
            totalReactivePowerKvar = totalReactivePowerKvar,
            totalDesignCurrentA = totalDesignCurrentA,
            maximumDesignCurrentA = maximumDesignCurrentA,
            maximumStartingCurrentA = maximumStartingCurrentA,
            overallPowerFactor = overallPowerFactor
        )
    }
}

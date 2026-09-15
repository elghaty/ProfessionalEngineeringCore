package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad

data class NetworkDesignResult(
    val network: ElectricalNetworkResult,
    val loadSchedule: LoadScheduleResult,
    val mdb: MdbResult,
    val transformer: TransformerSizingResult
)

class DesignNetworkCalculator(
    private val network:
        ElectricalNetworkCalculator =
        ElectricalNetworkCalculator(),

    private val schedule:
        LoadScheduleCalculator =
        LoadScheduleCalculator(),

    private val mdb:
        MdbCalculator =
        MdbCalculator(),

    private val transformer:
        TransformerSizingCalculator =
        TransformerSizingCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90
    ): NetworkDesignResult {

        val networkResult =
            network.calculate(
                loads = loads,
                voltageV = voltageV,
                powerFactor = powerFactor
            )

        val scheduleResult =
            schedule.calculate(loads)

        val mdbResult =
            mdb.calculate(
                MdbInput(
                    loads = loads,
                    voltageV = voltageV,
                    powerFactor = powerFactor
                )
            )

        val transformerResult =
            transformer.calculate(
                requiredKva =
                    networkResult.totalKva
            )

        return NetworkDesignResult(
            network = networkResult,
            loadSchedule = scheduleResult,
            mdb = mdbResult,
            transformer = transformerResult
        )
    }
}

package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import kotlin.math.sqrt

data class NetworkBusResult(
    val busName: String,
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val currentA: Double
)

data class ElectricalNetworkResult(
    val buses: List<NetworkBusResult>,
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val mainCurrentA: Double,
    val estimatedTransformerKva: Double
)

class ElectricalNetworkCalculator(
    private val loadCalculator: LoadCalculator
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.9
    ): ElectricalNetworkResult {

        require(voltageV > 0.0)
        require(powerFactor in 0.1..1.0)

        val grouped =
            loads.groupBy { it.panelName.ifBlank { "MDB" } }

        val buses =
            grouped.map { (panel, panelLoads) ->

                val results =
                    panelLoads.map {
                        loadCalculator.calculate(it)
                    }

                val connected =
                    results.sumOf {
                        it.connectedLoadKw
                    }

                val demand =
                    results.sumOf {
                        it.demandLoadKw
                    }

                val current =
                    demand * 1000.0 /
                        (sqrt(3.0) * voltageV * powerFactor)

                NetworkBusResult(
                    busName = panel,
                    connectedLoadKw = connected,
                    demandLoadKw = demand,
                    currentA = current
                )
            }

        val totalConnected =
            buses.sumOf { it.connectedLoadKw }

        val totalDemand =
            buses.sumOf { it.demandLoadKw }

        val mainCurrent =
            totalDemand * 1000.0 /
                (sqrt(3.0) * voltageV * powerFactor)

        val transformerKva =
            totalDemand / powerFactor * 1.20

        return ElectricalNetworkResult(
            buses = buses,
            totalConnectedKw = totalConnected,
            totalDemandKw = totalDemand,
            mainCurrentA = mainCurrent,
            estimatedTransformerKva = transformerKva
        )
    }
}

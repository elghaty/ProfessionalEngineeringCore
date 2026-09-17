package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

data class NetworkBusResult(
    val busName: String,
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val apparentPowerKva: Double,
    val currentA: Double
)

data class ElectricalNetworkResult(
    val buses: List<NetworkBusResult>,
    val totalConnectedKw: Double,
    val totalDemandKw: Double,
    val totalDesignKw: Double,
    val totalApparentPowerKva: Double,
    val mainCurrentA: Double,
    val estimatedTransformerKva: Double
)

class ElectricalNetworkCalculator(
    private val loadCalculator: LoadCalculator = LoadCalculator()
) {

    fun calculate(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): ElectricalNetworkResult {

        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)

        val grouped =
            loads.groupBy {
                it.name.ifBlank { "MDB" }
            }

        val buses =
            grouped.map { (panel, panelLoads) ->

                val results =
                    panelLoads.map {
                        loadCalculator.calculate(it)
                    }

                val connected =
                    results.sumOf {
                        it.connectedKw
                    }

                val demand =
                    results.sumOf {
                        it.demandKw
                    }

                val design =
                    results.sumOf {
                        it.designKw
                    }

                val apparent =
                    results.sumOf {
                        it.apparentPowerKva
                    }

                val current =
                    when (phase) {
                        Phase.THREE ->
                            design * 1000.0 /
                                    (
                                        sqrt(3.0) *
                                                voltageV *
                                                powerFactor
                                        )

                        Phase.SINGLE ->
                            design * 1000.0 /
                                    (
                                        voltageV *
                                                powerFactor
                                        )
                    }

                NetworkBusResult(
                    busName = panel,
                    connectedLoadKw = connected,
                    demandLoadKw = demand,
                    designLoadKw = design,
                    apparentPowerKva = apparent,
                    currentA = current
                )
            }

        val totalConnected =
            buses.sumOf {
                it.connectedLoadKw
            }

        val totalDemand =
            buses.sumOf {
                it.demandLoadKw
            }

        val totalDesign =
            buses.sumOf {
                it.designLoadKw
            }

        val totalKva =
            buses.sumOf {
                it.apparentPowerKva
            }

        val mainCurrent =
            when (phase) {
                Phase.THREE ->
                    totalDesign * 1000.0 /
                            (
                                sqrt(3.0) *
                                        voltageV *
                                        powerFactor
                                )

                Phase.SINGLE ->
                    totalDesign * 1000.0 /
                            (
                                voltageV *
                                        powerFactor
                                )
            }

        val transformerKva =
            totalKva * 1.20

        return ElectricalNetworkResult(
            buses = buses,
            totalConnectedKw = totalConnected,
            totalDemandKw = totalDemand,
            totalDesignKw = totalDesign,
            totalApparentPowerKva = totalKva,
            mainCurrentA = mainCurrent,
            estimatedTransformerKva = transformerKva
        )
    }
}

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

        require(voltageV > 0.0) {
            "Voltage must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        if (loads.isEmpty()) {
            return ElectricalNetworkResult(
                buses = emptyList(),
                totalConnectedKw = 0.0,
                totalDemandKw = 0.0,
                totalDesignKw = 0.0,
                totalApparentPowerKva = 0.0,
                mainCurrentA = 0.0,
                estimatedTransformerKva = 0.0
            )
        }

        val loadResults = loads.map { load ->
            loadCalculator.calculate(load)
        }

        val connectedKw =
            loadResults.sumOf { it.connectedKw }

        val demandKw =
            loadResults.sumOf { it.demandKw }

        val designKw =
            loadResults.sumOf { it.designKw }

        val apparentPowerKva =
            loadResults.sumOf { it.apparentPowerKva }

        val mainCurrentA =
            when (phase) {

                Phase.THREE ->
                    designKw * 1000.0 /
                        (
                            sqrt(3.0) *
                                voltageV *
                                powerFactor
                        )

                Phase.SINGLE ->
                    designKw * 1000.0 /
                        (
                            voltageV *
                                powerFactor
                        )
            }

        val mainBus =
            NetworkBusResult(
                busName = "MAIN",
                connectedLoadKw = connectedKw,
                demandLoadKw = demandKw,
                designLoadKw = designKw,
                apparentPowerKva = apparentPowerKva,
                currentA = mainCurrentA
            )

        /*
         * This is only an estimated transformer requirement.
         * Final transformer selection must pass through
         * TransformerSizingCalculator.
         */
        val estimatedTransformerKva =
            apparentPowerKva * 1.15

        return ElectricalNetworkResult(
            buses = listOf(mainBus),
            totalConnectedKw = connectedKw,
            totalDemandKw = demandKw,
            totalDesignKw = designKw,
            totalApparentPowerKva = apparentPowerKva,
            mainCurrentA = mainCurrentA,
            estimatedTransformerKva = estimatedTransformerKva
        )
    }
}

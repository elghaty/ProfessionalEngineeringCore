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
    private val loadCalculator: LoadCalculator =
        LoadCalculator()
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

        val buses =
            listOf(
                NetworkBusResult(
                    busName = "MAIN",
                    connectedLoadKw =
                        loads.sumOf {
                            loadCalculator.calculate(it).connectedKw
                        },
                    demandLoadKw =
                        loads.sumOf {
                            loadCalculator.calculate(it).demandKw
                        },
                    designLoadKw =
                        loads.sumOf {
                            loadCalculator.calculate(it).designKw
                        },
                    apparentPowerKva =
                        loads.sumOf {
                            loadCalculator.calculate(it).apparentPowerKva
                        },
                    currentA = 0.0
                )
            ).map { bus ->

                val current =
                    when (phase) {

                        Phase.THREE ->
                            bus.designLoadKw * 1000.0 /
                                (
                                    sqrt(3.0) *
                                        voltageV *
                                        powerFactor
                                )

                        Phase.SINGLE ->
                            bus.designLoadKw * 1000.0 /
                                (
                                    voltageV *
                                        powerFactor
                                )
                    }

                bus.copy(
                    currentA = current
                )
            }

        val main =
            buses.first()

        val transformerKva =
            if (main.apparentPowerKva > 0.0) {
                main.apparentPowerKva * 1.15
            } else {
                0.0
            }

        return ElectricalNetworkResult(
            buses = buses,
            totalConnectedKw =
                main.connectedLoadKw,
            totalDemandKw =
                main.demandLoadKw,
            totalDesignKw =
                main.designLoadKw,
            totalApparentPowerKva =
                main.apparentPowerKva,
            mainCurrentA =
                main.currentA,
            estimatedTransformerKva =
                transformerKva
        )
    }
}

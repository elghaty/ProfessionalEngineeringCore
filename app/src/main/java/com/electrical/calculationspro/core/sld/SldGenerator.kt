package com.electrical.calculationspro.core.sld

import com.electrical.calculationspro.core.calculation.LoadCalculator
import com.electrical.calculationspro.core.calculation.LoadResult
import com.electrical.calculationspro.core.calculation.SldShortCircuitCalculator
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.SldElement
import com.electrical.calculationspro.core.model.SldElementResult
import com.electrical.calculationspro.core.model.SldElementType
import com.electrical.calculationspro.core.model.SldNetwork
import com.electrical.calculationspro.core.model.SldResult

class SldGenerator(
    private val loadCalculator:
        LoadCalculator =
        LoadCalculator(),

    private val shortCircuitCalculator:
        SldShortCircuitCalculator =
        SldShortCircuitCalculator()
) {

    fun generate(
        network: SldNetwork,
        sourceFaultMva: Double = 1000.0,
        voltageFactor: Double = 1.05
    ): SldResult {

        require(network.elements.isNotEmpty())

        val loadResults =
            linkedMapOf<String, LoadResult>()

        network.elements.forEach { element ->

            if (
                element.type !=
                SldElementType.LOAD &&
                element.type !=
                SldElementType.MOTOR &&
                element.type !=
                SldElementType.PUMP
            ) {
                return@forEach
            }

            val load =
                ElectricalLoad(
                    id = element.id,
                    name = element.name,
                    powerKw = element.powerKw,
                    quantity = element.quantity,
                    voltageV = element.voltageV,
                    phase = element.phase,
                    powerFactor =
                        element.powerFactor,
                    efficiency =
                        element.efficiency,
                    demandFactor =
                        element.demandFactor,
                    diversityFactor =
                        element.diversityFactor,
                    lengthM =
                        element.cableLengthM
                )

            loadResults[element.id] =
                loadCalculator.calculate(
                    load
                )
        }

        val shortCircuit =
            shortCircuitCalculator.calculate(
                network =
                    network,

                sourceShortCircuitMva =
                    sourceFaultMva,

                voltageFactor =
                    voltageFactor
            )

        val elementResults =
            network.elements.mapNotNull { element ->

                val load =
                    loadResults[element.id]

                if (load == null) {
                    null
                } else {
                    SldElementResult(
                        elementId =
                            element.id,

                        powerKw =
                            load.designKw,

                        apparentPowerKva =
                            load.apparentPowerKva,

                        currentA =
                            load.currentA,

                        shortCircuitKA =
                            shortCircuit.results[
                                element.id
                            ]?.faultCurrentKA
                                ?: 0.0
                    )
                }
            }

        val totalLoad =
            elementResults.sumOf {
                it.powerKw
            }

        val totalDemand =
            elementResults.sumOf {
                it.powerKw
            }

        val source =
            findSource(network)

        val sourceVoltage =
            source?.voltageV
                ?: 400.0

        val sourcePowerFactor =
            source?.powerFactor
                ?: 0.90

        val sourceCurrent =
            if (totalDemand > 0.0) {
                totalDemand * 1000.0 /
                    (
                        kotlin.math.sqrt(3.0) *
                            sourceVoltage *
                            sourcePowerFactor
                        )
            } else {
                0.0
            }

        return SldResult(
            elements =
                elementResults,

            totalLoadKw =
                totalLoad,

            totalDemandKw =
                totalDemand,

            sourceCurrentA =
                sourceCurrent,

            sourceFaultCurrentKA =
                shortCircuit.maximumFaultCurrentKA
        )
    }

    fun createBasicNetwork(): SldNetwork {

        val utility =
            SldElement(
                id = "SOURCE",
                name = "Utility",
                type = SldElementType.UTILITY,
                x = 100f,
                y = 100f,
                voltageV = 400.0
            )

        val mdb =
            SldElement(
                id = "MDB-01",
                name = "MDB",
                type = SldElementType.MDB,
                x = 350f,
                y = 100f,
                voltageV = 400.0
            )

        return SldNetwork(
            elements =
                listOf(
                    utility,
                    mdb
                ),

            connections =
                listOf(
                    com.electrical.calculationspro.core.model.SldConnection(
                        id = "SOURCE-MDB",
                        fromId = "SOURCE",
                        toId = "MDB-01"
                    )
                )
        )
    }

    private fun findSource(
        network: SldNetwork
    ): SldElement? {

        return network.elements.firstOrNull {
            it.type ==
                SldElementType.UTILITY ||
                it.type ==
                SldElementType.TRANSFORMER ||
                it.type ==
                SldElementType.GENERATOR
        }
    }
}

package com.electrical.calculationspro.core.sld

import com.electrical.calculationspro.core.calculation.LoadCalculator
import com.electrical.calculationspro.core.calculation.ShortCircuitCalculator
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.SldElement
import com.electrical.calculationspro.core.model.SldElementResult
import com.electrical.calculationspro.core.model.SldElementType
import com.electrical.calculationspro.core.model.SldNetwork
import com.electrical.calculationspro.core.model.SldResult
import kotlin.math.sqrt

class SldGenerator(
    private val loadCalculator:
        LoadCalculator =
        LoadCalculator(),

    private val shortCircuit:
        ShortCircuitCalculator =
        ShortCircuitCalculator()
) {

    fun generate(
        network: SldNetwork,
        sourceFaultMva: Double = 1000.0
    ): SldResult {

        require(sourceFaultMva > 0.0)

        val resultMap =
            mutableMapOf<String, SldElementResult>()

        val loads =
            network.elements.filter {
                it.type == SldElementType.LOAD ||
                    it.type == SldElementType.MOTOR ||
                    it.type == SldElementType.PUMP
            }

        var totalConnectedKw = 0.0
        var totalDemandKw = 0.0

        loads.forEach { element ->

            val load =
                ElectricalLoad(
                    id = element.id,
                    name = element.name,
                    powerKw = element.powerKw,
                    voltageV = element.voltageV
                )

            val loadResult =
                loadCalculator.calculate(load)

            totalConnectedKw +=
                loadResult.connectedKw

            totalDemandKw +=
                loadResult.demandKw

            val fault =
                shortCircuit.fromSourceFaultLevel(
                    voltageV =
                        element.voltageV,
                    sourceFaultMva =
                        sourceFaultMva
                )

            resultMap[element.id] =
                SldElementResult(
                    elementId =
                        element.id,
                    powerKw =
                        loadResult.designKw,
                    apparentPowerKva =
                        loadResult.apparentPowerKva,
                    currentA =
                        loadResult.currentA,
                    shortCircuitKA =
                        fault.initialSymmetricalCurrentKA
                )
        }

        val source =
            network.elements.firstOrNull {
                it.sourceType != null
            }

        val sourceVoltage =
            source?.voltageV ?: 400.0

        val sourceCurrent =
            if (totalDemandKw <= 0.0) {
                0.0
            } else {
                totalDemandKw * 1000.0 /
                    (
                        sqrt(3.0) *
                            sourceVoltage *
                            0.90
                        )
            }

        val sourceFault =
            shortCircuit.fromSourceFaultLevel(
                voltageV = sourceVoltage,
                sourceFaultMva = sourceFaultMva
            )

        return SldResult(
            elements =
                resultMap.values.toList(),
            totalLoadKw =
                totalConnectedKw,
            totalDemandKw =
                totalDemandKw,
            sourceCurrentA =
                sourceCurrent,
            sourceFaultCurrentKA =
                sourceFault.initialSymmetricalCurrentKA
        )
    }

    fun createBasicNetwork(
        loads: List<ElectricalLoad>,
        sourceVoltageV: Double = 400.0
    ): SldNetwork {

        val source =
            SldElement(
                id = "SOURCE",
                name = "UTILITY",
                type = SldElementType.UTILITY,
                x = 100f,
                y = 100f,
                voltageV = sourceVoltageV
            )

        val bus =
            SldElement(
                id = "MDB",
                name = "MDB",
                type = SldElementType.MDB,
                x = 300f,
                y = 100f,
                voltageV = sourceVoltageV
            )

        val elements =
            mutableListOf(
                source,
                bus
            )

        val connections =
            mutableListOf<
                com.electrical.calculationspro
                    .core.model.SldConnection
                >()

        connections +=
            com.electrical.calculationspro
                .core.model.SldConnection(
                    id = "SOURCE-MDB",
                    fromId = source.id,
                    toId = bus.id
                )

        loads.forEachIndexed { index, load ->

            val element =
                SldElement(
                    id = load.id,
                    name = load.name,
                    type =
                        when {
                            load.name.contains(
                                "motor",
                                ignoreCase = true
                            ) ->
                                SldElementType.MOTOR

                            load.name.contains(
                                "pump",
                                ignoreCase = true
                            ) ->
                                SldElementType.PUMP

                            else ->
                                SldElementType.LOAD
                        },
                    x = 500f,
                    y =
                        100f +
                            index * 120f,
                    powerKw =
                        load.powerKw *
                            load.quantity,
                    voltageV =
                        load.voltageV,
                    parentId =
                        bus.id
                )

            elements += element

            connections +=
                com.electrical.calculationspro
                    .core.model.SldConnection(
                        id =
                            "MDB-${element.id}",
                        fromId =
                            bus.id,
                        toId =
                            element.id,
                        lengthM =
                            load.lengthM
                    )
        }

        return SldNetwork(
            elements = elements,
            connections = connections
        )
    }
}

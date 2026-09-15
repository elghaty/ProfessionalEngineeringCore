package com.electrical.calculationspro.core.calculators

import com.electrical.calculationspro.data.SldConnection
import com.electrical.calculationspro.data.SldNetwork
import com.electrical.calculationspro.data.SldNode
import com.electrical.calculationspro.data.SldNodeType

/**
 * Professional SLD structure generator.
 *
 * This class creates the electrical topology.
 * Electrical calculations are performed by calculators
 * inside ProfessionalEngineeringCore.
 */
class SldGenerator {

    fun createEmpty(): SldNetwork {
        return SldNetwork(
            nodes = emptyList(),
            connections = emptyList()
        )
    }

    fun createSource(
        id: String,
        name: String,
        voltage: Double = 400.0,
        sourceShortCircuitMva: Double = 0.0
    ): SldNode {

        return SldNode(
            id = id,
            name = name,
            type = SldNodeType.SOURCE,
            x = 0f,
            y = 0f,
            voltage = voltage,
            sourceShortCircuitMva =
                sourceShortCircuitMva
        )
    }

    fun createTransformer(
        id: String,
        name: String,
        voltage: Double = 400.0,
        ratedKva: Double,
        percentZ: Double
    ): SldNode {

        require(ratedKva > 0.0) {
            "Transformer rating must be greater than zero."
        }

        require(percentZ > 0.0) {
            "Transformer impedance must be greater than zero."
        }

        return SldNode(
            id = id,
            name = name,
            type = SldNodeType.TRANSFORMER,
            x = 0f,
            y = 0f,
            voltage = voltage,
            ratedKva = ratedKva,
            transformerPercentZ = percentZ
        )
    }

    fun createGenerator(
        id: String,
        name: String,
        voltage: Double = 400.0,
        ratedKva: Double,
        xdSubtransient: Double = 0.0
    ): SldNode {

        require(ratedKva > 0.0) {
            "Generator rating must be greater than zero."
        }

        return SldNode(
            id = id,
            name = name,
            type = SldNodeType.GENERATOR,
            x = 0f,
            y = 0f,
            voltage = voltage,
            ratedKva = ratedKva,
            generatorXdSubtransient =
                xdSubtransient
        )
    }

    fun createBus(
        id: String,
        name: String,
        voltage: Double = 400.0
    ): SldNode {

        return SldNode(
            id = id,
            name = name,
            type = SldNodeType.BUS,
            x = 0f,
            y = 0f,
            voltage = voltage
        )
    }

    fun createPanel(
        id: String,
        name: String,
        voltage: Double = 400.0
    ): SldNode {

        return SldNode(
            id = id,
            name = name,
            type = SldNodeType.PANEL,
            x = 0f,
            y = 0f,
            voltage = voltage
        )
    }

    fun createLoad(
        id: String,
        name: String,
        loadKw: Double,
        voltage: Double = 400.0,
        powerFactor: Double = 0.90,
        demandFactor: Double = 1.0
    ): SldNode {

        require(loadKw >= 0.0) {
            "Load power cannot be negative."
        }

        require(powerFactor > 0.0) {
            "Power factor must be greater than zero."
        }

        require(powerFactor <= 1.0) {
            "Power factor cannot exceed 1.0."
        }

        require(demandFactor >= 0.0) {
            "Demand factor cannot be negative."
        }

        require(demandFactor <= 1.0) {
            "Demand factor cannot exceed 1.0."
        }

        return SldNode(
            id = id,
            name = name,
            type = SldNodeType.LOAD,
            x = 0f,
            y = 0f,
            voltage = voltage,
            loadKw = loadKw,
            powerFactor = powerFactor,
            demandFactor = demandFactor
        )
    }

    fun connect(
        id: String,
        from: SldNode,
        to: SldNode,
        lengthMeters: Double = 0.0,
        resistanceOhmPerKm: Double = 0.0,
        reactanceOhmPerKm: Double = 0.0,
        cableSizeMm2: Double = 0.0,
        parallelRuns: Int = 1
    ): SldConnection {

        require(lengthMeters >= 0.0) {
            "Cable length cannot be negative."
        }

        require(resistanceOhmPerKm >= 0.0) {
            "Cable resistance cannot be negative."
        }

        require(reactanceOhmPerKm >= 0.0) {
            "Cable reactance cannot be negative."
        }

        require(parallelRuns >= 1) {
            "Parallel runs must be at least one."
        }

        return SldConnection(
            id = id,
            fromNodeId = from.id,
            toNodeId = to.id,
            lengthMeters = lengthMeters,
            resistanceOhmPerKm =
                resistanceOhmPerKm,
            reactanceOhmPerKm =
                reactanceOhmPerKm,
            cableSizeMm2 =
                cableSizeMm2,
            parallelRuns =
                parallelRuns
        )
    }

    fun build(
        nodes: List<SldNode>,
        connections: List<SldConnection>
    ): SldNetwork {

        require(nodes.isNotEmpty()) {
            "SLD must contain at least one node."
        }

        val ids =
            nodes.map { it.id }

        require(ids.distinct().size == ids.size) {
            "SLD node IDs must be unique."
        }

        val nodeSet =
            ids.toSet()

        connections.forEach { connection ->

            require(
                connection.fromNodeId in nodeSet
            ) {
                "Connection ${connection.id} references an unknown source node."
            }

            require(
                connection.toNodeId in nodeSet
            ) {
                "Connection ${connection.id} references an unknown destination node."
            }

            require(
                connection.fromNodeId !=
                    connection.toNodeId
            ) {
                "An SLD connection cannot connect a node to itself."
            }
        }

        return SldNetwork(
            nodes = nodes,
            connections = connections
        )
    }
}

package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.SldConnection
import com.electrical.calculationspro.core.model.SldElement
import com.electrical.calculationspro.core.model.SldElementType
import com.electrical.calculationspro.core.model.SldNetwork
import com.electrical.calculationspro.core.model.SourceType

/**
 * Generates and validates the SLD topology.
 *
 * This class does not contain engineering formulas.
 * Engineering calculations are delegated to the
 * appropriate calculators through ProfessionalEngineeringCore.
 */
class SldGenerator {

    fun createEmpty(): SldNetwork =
        SldNetwork()

    fun createUtility(
        id: String,
        name: String = "UTILITY",
        voltageV: Double = 400.0,
        shortCircuitMva: Double = 1000.0,
        xOverR: Double = 10.0
    ): SldElement {

        require(id.isNotBlank())
        require(voltageV > 0.0)
        require(shortCircuitMva >= 0.0)
        require(xOverR >= 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.UTILITY,
            voltageV = voltageV,
            sourceType = SourceType.UTILITY,
            sourceShortCircuitMva = shortCircuitMva,
            sourceXOverR = xOverR
        )
    }

    fun createTransformer(
        id: String,
        name: String,
        ratingKva: Double,
        voltageV: Double = 400.0,
        percentZ: Double = 6.0
    ): SldElement {

        require(id.isNotBlank())
        require(ratingKva > 0.0)
        require(voltageV > 0.0)
        require(percentZ > 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.TRANSFORMER,
            voltageV = voltageV,
            transformerKva = ratingKva,
            transformerPercentZ = percentZ,
            sourceType = SourceType.TRANSFORMER
        )
    }

    fun createGenerator(
        id: String,
        name: String,
        ratingKva: Double,
        voltageV: Double = 400.0,
        xdPercent: Double = 15.0,
        xOverR: Double = 10.0
    ): SldElement {

        require(id.isNotBlank())
        require(ratingKva > 0.0)
        require(voltageV > 0.0)
        require(xdPercent > 0.0)
        require(xOverR >= 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.GENERATOR,
            voltageV = voltageV,
            generatorKva = ratingKva,
            generatorXdPercent = xdPercent,
            generatorXOverR = xOverR,
            sourceType = SourceType.GENERATOR
        )
    }

    fun createBus(
        id: String,
        name: String,
        voltageV: Double = 400.0
    ): SldElement {

        require(id.isNotBlank())
        require(voltageV > 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.BUS,
            voltageV = voltageV
        )
    }

    fun createPanel(
        id: String,
        name: String,
        type: SldElementType = SldElementType.MDB,
        voltageV: Double = 400.0
    ): SldElement {

        require(id.isNotBlank())
        require(
            type == SldElementType.MDB ||
                type == SldElementType.SMDB ||
                type == SldElementType.DB ||
                type == SldElementType.MCC
        )
        require(voltageV > 0.0)

        return SldElement(
            id = id,
            name = name,
            type = type,
            voltageV = voltageV
        )
    }

    fun createBreaker(
        id: String,
        name: String,
        voltageV: Double = 400.0
    ): SldElement {

        require(id.isNotBlank())
        require(voltageV > 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.BREAKER,
            voltageV = voltageV
        )
    }

    fun createFeeder(
        id: String,
        name: String,
        voltageV: Double = 400.0
    ): SldElement {

        require(id.isNotBlank())
        require(voltageV > 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.FEEDER,
            voltageV = voltageV
        )
    }

    fun createLoad(
        load: ElectricalLoad
    ): SldElement {

        require(load.id.isNotBlank())
        require(load.powerKw >= 0.0)
        require(load.quantity > 0)
        require(load.voltageV > 0.0)
        require(load.powerFactor in 0.01..1.0)
        require(load.efficiency in 0.01..1.0)

        val elementType =
            when {
                load.name.contains("motor", true) ->
                    SldElementType.MOTOR

                load.name.contains("pump", true) ->
                    SldElementType.PUMP

                else ->
                    SldElementType.LOAD
            }

        return SldElement(
            id = load.id,
            name = load.name,
            type = elementType,
            powerKw = load.powerKw * load.quantity,
            voltageV = load.voltageV,
            powerFactor = load.powerFactor,
            phase = load.phase,
            quantity = load.quantity,
            demandFactor = load.demandFactor,
            diversityFactor = load.diversityFactor,
            efficiency = load.efficiency
        )
    }

    fun connect(
        id: String,
        from: SldElement,
        to: SldElement,
        lengthM: Double = 0.0,
        resistanceOhmPerKm: Double = 0.0,
        reactanceOhmPerKm: Double = 0.0,
        cableSizeMm2: Double = 0.0,
        parallelRuns: Int = 1
    ): SldConnection {

        require(id.isNotBlank())
        require(from.id.isNotBlank())
        require(to.id.isNotBlank())
        require(from.id != to.id)
        require(lengthM >= 0.0)
        require(resistanceOhmPerKm >= 0.0)
        require(reactanceOhmPerKm >= 0.0)
        require(cableSizeMm2 >= 0.0)
        require(parallelRuns > 0)

        return SldConnection(
            id = id,
            fromId = from.id,
            toId = to.id,
            lengthM = lengthM,
            cableResistanceOhmPerKm =
                resistanceOhmPerKm,
            cableReactanceOhmPerKm =
                reactanceOhmPerKm,
            parallelRuns =
                parallelRuns
        )
    }

    fun build(
        elements: List<SldElement>,
        connections: List<SldConnection>
    ): SldNetwork {

        require(elements.isNotEmpty()) {
            "SLD must contain at least one element."
        }

        val ids =
            elements.map {
                it.id
            }

        require(ids.all { it.isNotBlank() }) {
            "SLD element IDs cannot be blank."
        }

        require(
            ids.distinct().size == ids.size
        ) {
            "SLD element IDs must be unique."
        }

        val validIds =
            ids.toSet()

        val connectionIds =
            connections.map {
                it.id
            }

        require(
            connectionIds.distinct().size ==
                connectionIds.size
        ) {
            "SLD connection IDs must be unique."
        }

        connections.forEach { connection ->

            require(
                connection.fromId in validIds
            ) {
                "Invalid connection source: ${connection.fromId}"
            }

            require(
                connection.toId in validIds
            ) {
                "Invalid connection destination: ${connection.toId}"
            }

            require(
                connection.fromId != connection.toId
            ) {
                "An SLD connection cannot connect an element to itself."
            }

            require(
                connection.lengthM >= 0.0
            )

            require(
                connection.cableResistanceOhmPerKm >= 0.0
            )

            require(
                connection.cableReactanceOhmPerKm >= 0.0
            )

            require(
                connection.parallelRuns > 0
            )
        }

        return SldNetwork(
            elements = elements,
            connections = connections
        )
    }

    fun createBasicNetwork(
        loads: List<ElectricalLoad>,
        sourceVoltageV: Double = 400.0
    ): SldNetwork {

        require(sourceVoltageV > 0.0)

        val utility =
            createUtility(
                id = "UTILITY",
                name = "UTILITY",
                voltageV = sourceVoltageV
            )

        val mdb =
            createBus(
                id = "MDB",
                name = "MDB",
                voltageV = sourceVoltageV
            )

        val elements =
            mutableListOf<SldElement>()

        val connections =
            mutableListOf<SldConnection>()

        elements += utility
        elements += mdb

        connections +=
            connect(
                id = "UTILITY-MDB",
                from = utility,
                to = mdb
            )

        loads.forEachIndexed { index, load ->

            val element =
                createLoad(load).copy(
                    x = 500f,
                    y = 120f + index * 120f,
                    parentId = mdb.id
                )

            elements += element

            connections +=
                connect(
                    id = "MDB-${element.id}",
                    from = mdb,
                    to = element,
                    lengthM = load.lengthM
                )
        }

        return build(
            elements = elements,
            connections = connections
        )
    }
}

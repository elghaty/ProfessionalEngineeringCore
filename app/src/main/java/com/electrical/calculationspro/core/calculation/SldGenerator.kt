package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.SldConnection
import com.electrical.calculationspro.core.model.SldElement
import com.electrical.calculationspro.core.model.SldElementType
import com.electrical.calculationspro.core.model.SldNetwork

class SldGenerator {

    fun createEmpty(): SldNetwork =
        SldNetwork()

    fun createUtility(
        id: String,
        name: String = "UTILITY",
        voltageV: Double = 400.0
    ): SldElement =
        SldElement(
            id = id,
            name = name,
            type = SldElementType.UTILITY,
            voltageV = voltageV,
            sourceType =
                com.electrical.calculationspro.core.model.SourceType.UTILITY
        )

    fun createTransformer(
        id: String,
        name: String,
        ratingKva: Double,
        voltageV: Double = 400.0,
        percentZ: Double = 6.0
    ): SldElement {
        require(ratingKva > 0.0)
        require(percentZ > 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.TRANSFORMER,
            voltageV = voltageV,
            transformerKva = ratingKva,
            transformerPercentZ = percentZ,
            sourceType =
                com.electrical.calculationspro.core.model.SourceType.TRANSFORMER
        )
    }

    fun createGenerator(
        id: String,
        name: String,
        ratingKva: Double,
        voltageV: Double = 400.0,
        xdSubtransient: Double = 15.0
    ): SldElement {
        require(ratingKva > 0.0)

        return SldElement(
            id = id,
            name = name,
            type = SldElementType.GENERATOR,
            voltageV = voltageV,
            generatorKva = ratingKva,
            generatorXdSubtransient = xdSubtransient,
            sourceType =
                com.electrical.calculationspro.core.model.SourceType.GENERATOR
        )
    }

    fun createBus(
        id: String,
        name: String,
        voltageV: Double = 400.0
    ): SldElement =
        SldElement(
            id = id,
            name = name,
            type = SldElementType.BUS,
            voltageV = voltageV
        )

    fun createPanel(
        id: String,
        name: String,
        type: SldElementType = SldElementType.MDB,
        voltageV: Double = 400.0
    ): SldElement =
        SldElement(
            id = id,
            name = name,
            type = type,
            voltageV = voltageV
        )

    fun createLoad(
        load: ElectricalLoad
    ): SldElement =
        SldElement(
            id = load.id,
            name = load.name,
            type =
                when {
                    load.name.contains("motor", true) ->
                        SldElementType.MOTOR

                    load.name.contains("pump", true) ->
                        SldElementType.PUMP

                    else ->
                        SldElementType.LOAD
                },
            powerKw =
                load.powerKw * load.quantity,
            voltageV = load.voltageV
        )

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

        require(lengthM >= 0.0)
        require(resistanceOhmPerKm >= 0.0)
        require(reactanceOhmPerKm >= 0.0)
        require(cableSizeMm2 >= 0.0)
        require(parallelRuns > 0)

        return SldConnection(
            id = id,
            fromId = from.id,
            toId = to.id,
            lengthM = lengthM
        )
    }

    fun build(
        elements: List<SldElement>,
        connections: List<SldConnection>
    ): SldNetwork {

        require(elements.isNotEmpty())

        val ids =
            elements.map { it.id }

        require(ids.distinct().size == ids.size)

        val validIds = ids.toSet()

        connections.forEach {
            require(it.fromId in validIds)
            require(it.toId in validIds)
            require(it.fromId != it.toId)
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

        val utility =
            createUtility(
                id = "UTILITY",
                voltageV = sourceVoltageV
            )

        val mdb =
            createBus(
                id = "MDB",
                name = "MDB",
                voltageV = sourceVoltageV
            )

        val elements =
            mutableListOf(
                utility,
                mdb
            )

        val connections =
            mutableListOf<SldConnection>()

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

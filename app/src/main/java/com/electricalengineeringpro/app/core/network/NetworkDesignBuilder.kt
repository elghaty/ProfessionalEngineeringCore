package com.electricalengineeringpro.app.core.network

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.ElectricalNetwork
import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType

class NetworkDesignBuilder {

    fun build(
        loads: List<ElectricalLoad>,
        sourceName: String = "UTILITY",
        mainPanelName: String = "MDB"
    ): ElectricalNetwork {

        val elements =
            mutableListOf<NetworkElement>()

        elements += NetworkElement(
            id = "SOURCE",
            name = sourceName,
            type = NetworkElementType.SOURCE,
            parentId = null
        )

        elements += NetworkElement(
            id = "MDB",
            name = mainPanelName,
            type = NetworkElementType.PANEL,
            parentId = "SOURCE"
        )

        loads.forEachIndexed { index, load ->

            val loadId =
                "LOAD-${index + 1}"

            elements += NetworkElement(
                id = loadId,
                name = load.name,
                type =
                    when (load.type) {
                        com.electricalengineeringpro.app.core.model.LoadType.MOTOR ->
                            NetworkElementType.MOTOR

                        com.electricalengineeringpro.app.core.model.LoadType.PUMP,
                        com.electricalengineeringpro.app.core.model.LoadType.FIRE_PUMP ->
                            NetworkElementType.PUMP

                        else ->
                            NetworkElementType.LOAD
                    },
                parentId = "MDB",
                ratingKva =
                    if (load.powerFactor > 0.0) {
                        load.powerKw *
                            load.quantity /
                            load.powerFactor
                    } else {
                        0.0
                    }
            )
        }

        return ElectricalNetwork(
            id = "NETWORK",
            name = "Electrical Network",
            elements = elements
        )
    }
}

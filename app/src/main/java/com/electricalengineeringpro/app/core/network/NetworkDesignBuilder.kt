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

        val elements = mutableListOf<NetworkElement>()

        elements += NetworkElement(
            id = "SOURCE",
            name = sourceName,
            type = NetworkElementType.SOURCE
        )

        elements += NetworkElement(
            id = "MDB",
            name = mainPanelName,
            type = NetworkElementType.PANEL
        )

        loads
            .groupBy {
                it.panelName.ifBlank {
                    "MDB"
                }
            }
            .keys
            .filter {
                it != "MDB"
            }
            .forEachIndexed { index, panelName ->

                elements += NetworkElement(
                    id = "PANEL-${index + 1}",
                    name = panelName,
                    type = NetworkElementType.PANEL
                )
            }

        loads.forEachIndexed { index, load ->

            elements += NetworkElement(
                id = "LOAD-${index + 1}",
                name = load.name,
                type = NetworkElementType.LOAD
            )
        }

        return ElectricalNetwork(
            id = "NETWORK",
            name = "Electrical Network",
            elements = elements
        )
    }
}

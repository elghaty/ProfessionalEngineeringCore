package com.electricalengineeringpro.app.core.sld

import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType

class SldGenerator {

    fun generate(
        source: NetworkElement,
        panels: List<NetworkElement>,
        feeders: List<NetworkElement>
    ): SingleLineDiagram {

        val nodes = mutableListOf<SldNode>()
        val connections = mutableListOf<SldConnection>()

        val sourceType =
            when (source.type) {
                NetworkElementType.SOURCE ->
                    SldSymbolType.UTILITY

                NetworkElementType.TRANSFORMER ->
                    SldSymbolType.TRANSFORMER

                NetworkElementType.GENERATOR ->
                    SldSymbolType.GENERATOR

                else ->
                    SldSymbolType.UTILITY
            }

        nodes += SldNode(
            id = source.id,
            name = source.name,
            type = sourceType,
            x = 0f,
            y = 0f
        )

        var previousId = source.id
        var index = 1

        panels.forEach { panel ->

            val type =
                when (panel.type) {
                    NetworkElementType.MCC ->
                        SldSymbolType.MCC

                    NetworkElementType.PANEL ->
                        if (index == 1)
                            SldSymbolType.MAIN_SWITCHBOARD
                        else
                            SldSymbolType.PANEL

                    NetworkElementType.TRANSFORMER ->
                        SldSymbolType.TRANSFORMER

                    NetworkElementType.GENERATOR ->
                        SldSymbolType.GENERATOR

                    else ->
                        SldSymbolType.PANEL
                }

            nodes += SldNode(
                id = panel.id,
                name = panel.name,
                type = type,
                x = 0f,
                y = index * 150f
            )

            connections += SldConnection(
                fromId = previousId,
                toId = panel.id,
                label = ""
            )

            previousId = panel.id
            index++
        }

        feeders.forEach { feeder ->

            nodes += SldNode(
                id = feeder.id,
                name = feeder.name,
                type = SldSymbolType.LOAD,
                x = 0f,
                y = index * 150f
            )

            connections += SldConnection(
                fromId = previousId,
                toId = feeder.id
            )

            previousId = feeder.id
            index++
        }

        return SingleLineDiagram(
            nodes = nodes,
            connections = connections
        )
    }
}

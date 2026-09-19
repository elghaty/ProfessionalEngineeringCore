package com.electricalengineeringpro.app.core.sld

import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType

/**
 * Generic SLD generator for the existing network model.
 *
 * The generator preserves the existing network API while
 * allowing electrical data to be attached to SLD nodes.
 */
class SldGenerator {

    fun generate(
        source: NetworkElement,
        panels: List<NetworkElement>,
        feeders: List<NetworkElement>
    ): SingleLineDiagram {

        val nodes =
            mutableListOf<SldNode>()

        val connections =
            mutableListOf<SldConnection>()

        val sourceType =
            when (source.type) {

                NetworkElementType.SOURCE ->
                    SldSymbolType.UTILITY

                NetworkElementType.TRANSFORMER ->
                    SldSymbolType.TRANSFORMER

                NetworkElementType.GENERATOR ->
                    SldSymbolType.GENERATOR

                NetworkElementType.PANEL ->
                    SldSymbolType.PANEL

                else ->
                    SldSymbolType.UTILITY
            }

        nodes +=
            SldNode(
                id = source.id,
                name = source.name,
                type = sourceType,
                x = 0f,
                y = 0f,
                electricalData =
                    SldElectricalData(
                        currentA =
                            source.currentA
                    ).asMap() +
                        if (source.ratingKva > 0.0) {
                            mapOf(
                                "Rating" =
                                    "%.0f kVA".format(
                                        source.ratingKva
                                    )
                            )
                        } else {
                            emptyMap()
                        }
            )

        var previousId =
            source.id

        var index =
            1

        panels.forEach { panel ->

            val symbolType =
                when (panel.type) {

                    NetworkElementType.MCC ->
                        SldSymbolType.MCC

                    NetworkElementType.TRANSFORMER ->
                        SldSymbolType.TRANSFORMER

                    NetworkElementType.GENERATOR ->
                        SldSymbolType.GENERATOR

                    NetworkElementType.PANEL ->
                        if (index == 1) {
                            SldSymbolType.MAIN_SWITCHBOARD
                        } else {
                            SldSymbolType.PANEL
                        }

                    else ->
                        SldSymbolType.PANEL
                }

            nodes +=
                SldNode(
                    id = panel.id,
                    name = panel.name,
                    type = symbolType,
                    x = 0f,
                    y = index * 160f,
                    electricalData =
                        SldElectricalData(
                            currentA =
                                panel.currentA
                        ).asMap() +
                            if (
                                panel.ratingKva > 0.0
                            ) {
                                mapOf(
                                    "Rating" =
                                        "%.0f kVA".format(
                                            panel.ratingKva
                                        )
                                )
                            } else {
                                emptyMap()
                            }
                )

            connections +=
                SldConnection(
                    fromId =
                        previousId,

                    toId =
                        panel.id,

                    label =
                        if (
                            panel.currentA > 0.0
                        ) {
                            "%.1f A".format(
                                panel.currentA
                            )
                        } else {
                            ""
                        }
                )

            previousId =
                panel.id

            index++
        }

        feeders.forEach { feeder ->

            val symbolType =
                when (feeder.type) {

                    NetworkElementType.MOTOR ->
                        SldSymbolType.MOTOR

                    NetworkElementType.PUMP ->
                        SldSymbolType.PUMP

                    NetworkElementType.PANEL ->
                        SldSymbolType.PANEL

                    NetworkElementType.MCC ->
                        SldSymbolType.MCC

                    else ->
                        SldSymbolType.LOAD
                }

            nodes +=
                SldNode(
                    id = feeder.id,
                    name = feeder.name,
                    type = symbolType,
                    x = 0f,
                    y = index * 160f,
                    electricalData =
                        SldElectricalData(
                            currentA =
                                feeder.currentA,

                            cableSizeMm2 =
                                0.0,

                            breakerA =
                                0.0
                        ).asMap()
                )

            connections +=
                SldConnection(
                    fromId =
                        previousId,

                    toId =
                        feeder.id
                )

            previousId =
                feeder.id

            index++
        }

        return SingleLineDiagram(
            nodes =
                nodes,

            connections =
                connections
        )
    }
}

package com.electricalengineeringpro.app.core.sld

import com.electricalengineeringpro.app.core.model.*

class SldGenerator {

    fun generate(
        source: SupplySource,
        panels: List<Panel>,
        feeders: List<Feeder>
    ): SingleLineDiagram {

        val nodes = mutableListOf<SldNode>()
        val connections = mutableListOf<SldConnection>()

        val sourceId = "SOURCE"

        nodes += SldNode(
            id = sourceId,
            label = source.name,
            type = when (source) {
                SupplySource.UTILITY -> SldSymbolType.UTILITY
                SupplySource.TRANSFORMER -> SldSymbolType.TRANSFORMER
                SupplySource.GENERATOR -> SldSymbolType.GENERATOR
                SupplySource.TRANSFORMER_GENERATOR -> SldSymbolType.TRANSFORMER
            },
            x = 500f,
            y = 60f
        )

        panels.forEachIndexed { index, panel ->

            val panelY = 180f + index * 160f

            nodes += SldNode(
                id = panel.id,
                label = panel.name,
                type = when (panel.type) {
                    NetworkElementType.MDB -> SldSymbolType.MDB
                    NetworkElementType.DB -> SldSymbolType.DB
                    NetworkElementType.MCC -> SldSymbolType.MCC
                    else -> SldSymbolType.MDB
                },
                x = 500f,
                y = panelY,
                parentId = sourceId,
                electricalData = mapOf(
                    "Voltage" to "${panel.voltage} V",
                    "Breaker" to "${panel.incomingBreakerA} A"
                )
            )

            if (index == 0) {
                connections += SldConnection(
                    fromId = sourceId,
                    toId = panel.id
                )
            }
        }

        feeders.forEachIndexed { index, feeder ->

            val destination = feeder.destinationPanelId ?: return@forEachIndexed

            nodes += SldNode(
                id = feeder.id,
                label = feeder.name,
                type = SldSymbolType.FEEDER,
                x = 180f + index * 140f,
                y = 420f,
                parentId = feeder.sourcePanelId,
                electricalData = mapOf(
                    "Current" to "${"%.1f".format(feeder.designCurrentA)} A",
                    "Cable" to "${feeder.cableSizeMm2} mm²",
                    "Breaker" to "${feeder.breakerRatingA} A",
                    "Voltage Drop" to "${"%.2f".format(feeder.voltageDropPercent)} %",
                    "Isc" to "${"%.2f".format(feeder.shortCircuitKA)} kA"
                )
            )

            connections += SldConnection(
                fromId = feeder.sourcePanelId,
                toId = feeder.id
            )

            connections += SldConnection(
                fromId = feeder.id,
                toId = destination
            )
        }

        return SingleLineDiagram(
            nodes = nodes,
            connections = connections
        )
    }
}

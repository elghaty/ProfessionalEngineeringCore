package com.electricalengineeringpro.app.core.network

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.ElectricalNetwork
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

class NetworkDesignBuilder {

    fun build(
        loads: List<ElectricalLoad>,
        sourceName: String = "UTILITY",
        mainPanelName: String = "MDB"
    ): ElectricalNetwork {

        require(sourceName.isNotBlank()) {
            "Source name must not be blank."
        }

        require(mainPanelName.isNotBlank()) {
            "Main panel name must not be blank."
        }

        val elements = mutableListOf<NetworkElement>()

        val sourceId = "SOURCE"

        elements += NetworkElement(
            id = sourceId,
            name = sourceName,
            type = NetworkElementType.SOURCE,
            parentId = null
        )

        val mainPanelId = normalizeId(mainPanelName)

        elements += NetworkElement(
            id = mainPanelId,
            name = mainPanelName,
            type = NetworkElementType.PANEL,
            parentId = sourceId
        )

        /*
         * Collect all panels referenced by the loads.
         *
         * Every panel other than MDB is connected to MDB.
         * This establishes a real hierarchical structure instead
         * of placing every load directly under MDB.
         */
        val panelNames = loads
            .map { it.panelName.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { normalizeId(it) }

        panelNames.forEach { panelName ->

            val panelId = normalizeId(panelName)

            if (panelId == mainPanelId) {
                return@forEach
            }

            elements += NetworkElement(
                id = panelId,
                name = panelName,
                type = NetworkElementType.PANEL,
                parentId = mainPanelId
            )
        }

        /*
         * Add every load below its assigned panel.
         */
        loads.forEachIndexed { index, load ->

            require(load.name.isNotBlank()) {
                "Load name must not be blank."
            }

            require(load.quantity > 0) {
                "Load quantity must be greater than zero."
            }

            require(load.powerKw >= 0.0) {
                "Load power must not be negative."
            }

            require(load.powerFactor > 0.0 &&
                load.powerFactor <= 1.0
            ) {
                "Load power factor must be between 0 and 1."
            }

            val requestedPanel =
                load.panelName.trim()

            val parentId =
                if (
                    requestedPanel.isBlank() ||
                    normalizeId(requestedPanel) == mainPanelId
                ) {
                    mainPanelId
                } else {
                    normalizeId(requestedPanel)
                }

            val loadId =
                "LOAD-${index + 1}"

            val type =
                when (load.type) {

                    LoadType.MOTOR ->
                        NetworkElementType.MOTOR

                    LoadType.PUMP,
                    LoadType.FIRE_PUMP ->
                        NetworkElementType.PUMP

                    else ->
                        NetworkElementType.LOAD
                }

            val totalPowerKw =
                load.powerKw *
                    load.quantity

            val ratingKva =
                if (load.powerFactor > 0.0) {
                    totalPowerKw /
                        load.powerFactor
                } else {
                    0.0
                }

            val currentA =
                calculateCurrent(
                    powerKw = totalPowerKw,
                    voltage = load.voltage,
                    powerFactor = load.powerFactor,
                    phase = load.phase
                )

            elements += NetworkElement(
                id = loadId,
                name = load.name,
                type = type,
                parentId = parentId,
                ratingKva = ratingKva,
                currentA = currentA
            )
        }

        return ElectricalNetwork(
            id = "NETWORK",
            name = "Electrical Network",
            elements = elements
        )
    }

    private fun calculateCurrent(
        powerKw: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): Double {

        if (powerKw <= 0.0) {
            return 0.0
        }

        require(voltage > 0.0) {
            "Load voltage must be greater than zero."
        }

        require(powerFactor > 0.0 &&
            powerFactor <= 1.0
        ) {
            "Power factor must be between 0 and 1."
        }

        return when (phase) {

            Phase.THREE -> {
                powerKw * 1000.0 /
                    (
                        sqrt(3.0) *
                            voltage *
                            powerFactor
                        )
            }

            Phase.SINGLE -> {
                powerKw * 1000.0 /
                    (
                        voltage *
                            powerFactor
                        )
            }
        }
    }

    private fun normalizeId(value: String): String {

        return value
            .trim()
            .uppercase()
            .replace(
                Regex("[^A-Z0-9]+"),
                "_"
            )
            .trim('_')
            .ifBlank {
                "ELEMENT"
            }
    }
}

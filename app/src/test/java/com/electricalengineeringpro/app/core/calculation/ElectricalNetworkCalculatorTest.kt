package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.Phase
import org.junit.Assert.assertTrue
import org.junit.Test

class ElectricalNetworkCalculatorTest {

    @Test
    fun network_calculates_total_load() {

        val loads = listOf(
            ElectricalLoad(
                id = "L1",
                name = "Lighting",
                type = LoadType.LIGHTING,
                quantity = 1,
                unitPowerKw = 20.0,
                powerFactor = 0.95,
                demandFactor = 0.9,
                phase = Phase.THREE_PHASE,
                voltageV = 400.0,
                panelName = "MDB"
            ),
            ElectricalLoad(
                id = "L2",
                name = "Sockets",
                type = LoadType.SOCKET,
                quantity = 1,
                unitPowerKw = 30.0,
                powerFactor = 0.9,
                demandFactor = 0.8,
                phase = Phase.THREE_PHASE,
                voltageV = 400.0,
                panelName = "MDB"
            )
        )

        val calculator =
            ElectricalNetworkCalculator(
                LoadCalculator()
            )

        val result =
            calculator.calculate(loads)

        assertTrue(
            result.totalConnectedKw > 0.0
        )

        assertTrue(
            result.totalDemandKw > 0.0
        )

        assertTrue(
            result.mainCurrentA > 0.0
        )

        assertTrue(
            result.estimatedTransformerKva > 0.0
        )
    }
}

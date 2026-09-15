package com.electricalengineeringpro.app.core.network

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.LoadType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkCalculatorTest {

    @Test
    fun calculateNetwork() {

        val loads = listOf(
            ElectricalLoad(
                id = "1",
                name = "Lighting",
                loadType = LoadType.LIGHTING,
                powerKW = 10.0,
                quantity = 1,
                demandFactor = 0.80
            ),
            ElectricalLoad(
                id = "2",
                name = "Sockets",
                loadType = LoadType.SOCKET,
                powerKW = 20.0,
                quantity = 1,
                demandFactor = 0.60
            )
        )

        val result =
            NetworkCalculator().calculate(
                loads = loads,
                voltageV = 400.0,
                powerFactor = 0.90,
                diversityFactor = 0.85
            )

        assertEquals(30.0, result.connectedLoadKW, 0.001)
        assertEquals(25.5, result.designLoadKW, 0.001)
        assertTrue(result.mainCurrentA > 0.0)
        assertTrue(result.recommendedTransformerKVA > 0.0)
    }
}

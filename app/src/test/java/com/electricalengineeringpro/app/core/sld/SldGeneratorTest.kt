package com.electricalengineeringpro.app.core.sld

import com.electricalengineeringpro.app.core.model.ElectricalNetwork
import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SldGeneratorTest {

    @Test
    fun sld_preserves_network_hierarchy() {

        val network =
            ElectricalNetwork(
                id = "NETWORK",
                name = "Electrical Network",
                elements = listOf(

                    NetworkElement(
                        id = "SOURCE",
                        name = "Utility",
                        type = NetworkElementType.SOURCE
                    ),

                    NetworkElement(
                        id = "MDB",
                        name = "MDB",
                        type = NetworkElementType.PANEL,
                        parentId = "SOURCE"
                    ),

                    NetworkElement(
                        id = "DB_01",
                        name = "DB-01",
                        type = NetworkElementType.PANEL,
                        parentId = "MDB"
                    ),

                    NetworkElement(
                        id = "LOAD_01",
                        name = "Lighting",
                        type = NetworkElementType.LOAD,
                        parentId = "MDB",
                        ratingKva = 11.11,
                        currentA = 16.03
                    ),

                    NetworkElement(
                        id = "LOAD_02",
                        name = "Motor",
                        type = NetworkElementType.MOTOR,
                        parentId = "DB_01",
                        ratingKva = 22.22,
                        currentA = 32.08
                    )
                )
            )

        val generator =
            SldGenerator()

        val result =
            generator.generate(network)

        assertEquals(
            5,
            result.nodes.size
        )

        assertEquals(
            4,
            result.connections.size
        )

        assertTrue(
            result.nodes.any {
                it.type == SldSymbolType.UTILITY
            }
        )

        assertTrue(
            result.nodes.any {
                it.type == SldSymbolType.PANEL
            }
        )

        assertTrue(
            result.nodes.any {
                it.type == SldSymbolType.MOTOR
            }
        )

        assertTrue(
            result.connections.any {
                it.fromId == "MDB" &&
                    it.toId == "DB_01"
            }
        )

        assertTrue(
            result.connections.any {
                it.fromId == "DB_01" &&
                    it.toId == "LOAD_02"
            }
        )
    }
}

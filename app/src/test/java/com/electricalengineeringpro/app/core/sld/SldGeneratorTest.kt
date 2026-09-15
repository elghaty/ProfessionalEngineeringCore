package com.electricalengineeringpro.app.core.sld

import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SldGeneratorTest {

    @Test
    fun sld_contains_nodes_and_connections() {

        val generator = SldGenerator()

        val source =
            NetworkElement(
                id = "SOURCE",
                name = "Utility",
                type = NetworkElementType.SOURCE
            )

        val panels =
            listOf(
                NetworkElement(
                    id = "MDB",
                    name = "MDB",
                    type = NetworkElementType.PANEL
                ),
                NetworkElement(
                    id = "DB1",
                    name = "DB-01",
                    type = NetworkElementType.PANEL
                )
            )

        val result =
            generator.generate(
                source = source,
                panels = panels,
                feeders = emptyList()
            )

        assertEquals(
            3,
            result.nodes.size
        )

        assertEquals(
            2,
            result.connections.size
        )

        assertTrue(
            result.nodes.any {
                it.type == SldSymbolType.UTILITY
            }
        )

        assertTrue(
            result.nodes.any {
                it.type == SldSymbolType.MAIN_SWITCHBOARD
            }
        )
    }
}

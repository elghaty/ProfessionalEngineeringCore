package com.electricalengineeringpro.app.core.network

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.Phase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkDesignBuilderTest {

    @Test
    fun loads_are_attached_to_their_assigned_panels() {

        val builder =
            NetworkDesignBuilder()

        val loads =
            listOf(

                ElectricalLoad(
                    name = "MDB Lighting",
                    type = LoadType.LIGHTING,
                    powerKw = 10.0,
                    panelName = "MDB",
                    voltage = 400.0,
                    phase = Phase.THREE
                ),

                ElectricalLoad(
                    name = "DB-01 Motor",
                    type = LoadType.MOTOR,
                    powerKw = 20.0,
                    panelName = "DB-01",
                    voltage = 400.0,
                    phase = Phase.THREE
                )
            )

        val network =
            builder.build(
                loads = loads,
                sourceName = "UTILITY",
                mainPanelName = "MDB"
            )

        val source =
            network.elements.first {
                it.id == "SOURCE"
            }

        val mdb =
            network.elements.first {
                it.id == "MDB"
            }

        val db01 =
            network.elements.first {
                it.id == "DB_01"
            }

        val lighting =
            network.elements.first {
                it.name == "MDB Lighting"
            }

        val motor =
            network.elements.first {
                it.name == "DB-01 Motor"
            }

        assertEquals(
            null,
            source.parentId
        )

        assertEquals(
            "SOURCE",
            mdb.parentId
        )

        assertEquals(
            "MDB",
            db01.parentId
        )

        assertEquals(
            "MDB",
            lighting.parentId
        )

        assertEquals(
            "DB_01",
            motor.parentId
        )

        assertTrue(
            motor.currentA > 0.0
        )
    }
}

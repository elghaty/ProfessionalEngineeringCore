package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.Phase
import org.junit.Assert.assertTrue
import org.junit.Test

class EngineeringDesignServiceTest {

    @Test
    fun complete_design_produces_main_engineering_results() {

        val loads =
            listOf(
                ElectricalLoad(
                    id = "1",
                    name = "Lighting",
                    type = LoadType.LIGHTING,
                    quantity = 1,
                    unitPowerKw = 50.0,
                    powerFactor = 0.95,
                    demandFactor = 0.9,
                    phase = Phase.THREE_PHASE,
                    voltageV = 400.0,
                    panelName = "MDB"
                ),
                ElectricalLoad(
                    id = "2",
                    name = "Pumps",
                    type = LoadType.PUMP,
                    quantity = 2,
                    unitPowerKw = 30.0,
                    powerFactor = 0.85,
                    demandFactor = 0.8,
                    phase = Phase.THREE_PHASE,
                    voltageV = 400.0,
                    panelName = "MCC-01"
                )
            )

        val result =
            EngineeringDesignService().calculate(
                CompleteDesignInput(
                    loads = loads
                )
            )

        assertTrue(result.connectedLoadKw > 0.0)
        assertTrue(result.demandLoadKw > 0.0)
        assertTrue(result.designCurrentA > 0.0)
        assertTrue(result.transformerKva > 0.0)
        assertTrue(result.mainBreakerA > 0.0)
        assertTrue(result.breakerBreakingCapacityKA > 0.0)
        assertTrue(result.shortCircuitKA > 0.0)
    }
}

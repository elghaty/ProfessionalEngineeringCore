package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.LoadCalculator
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.Phase
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadCalculatorTest {

    @Test
    fun threePhaseLoadCalculation() {
        val load = ElectricalLoad(
            name = "Test Load",
            type = LoadType.MISCELLANEOUS,
            quantity = 1,
            powerKw = 100.0,
            powerFactor = 0.8,
            efficiency = 1.0,
            demandFactor = 1.0,
            diversityFactor = 1.0,
            voltage = 400.0,
            phase = Phase.THREE
        )

        val result = LoadCalculator().calculate(load)

        assertEquals(100.0, result.connectedKw, 0.001)
        assertEquals(100.0, result.designKw, 0.001)
        assertEquals(180.42, result.currentA, 0.2)
    }
}

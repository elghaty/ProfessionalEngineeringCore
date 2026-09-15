package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.CableCalculator
import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.CableInsulation
import com.electricalengineeringpro.app.core.model.ConductorMaterial
import com.electricalengineeringpro.app.core.model.InstallationMethod
import com.electricalengineeringpro.app.core.model.Phase
import org.junit.Assert.assertTrue
import org.junit.Test

class CableCalculatorTest {

    @Test
    fun cableIsSelectedAboveDesignCurrent() {

        val result = CableCalculator().calculate(
            CableInput(
                designCurrentA = 120.0,
                lengthM = 50.0,
                voltage = 400.0,
                powerFactor = 0.9,
                phase = Phase.THREE,
                material = ConductorMaterial.COPPER,
                insulation = CableInsulation.XLPE,
                installationMethod = InstallationMethod.TRAY
            )
        )

        assertTrue(result.ampacityA >= 120.0)
        assertTrue(result.selectedSizeMm2 >= 35.0)
    }
}

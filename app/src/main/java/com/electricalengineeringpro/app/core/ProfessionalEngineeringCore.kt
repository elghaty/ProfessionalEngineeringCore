package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.BreakerCalculator
import com.electricalengineeringpro.app.core.calculation.CableCalculator
import com.electricalengineeringpro.app.core.calculation.DesignSummaryCalculator
import com.electricalengineeringpro.app.core.calculation.LoadCalculator
import com.electricalengineeringpro.app.core.calculation.MotorCalculator
import com.electricalengineeringpro.app.core.calculation.PowerCalculator
import com.electricalengineeringpro.app.core.calculation.PumpCalculator
import com.electricalengineeringpro.app.core.calculation.ShortCircuitCalculator
import com.electricalengineeringpro.app.core.calculation.TransformerCalculator
import com.electricalengineeringpro.app.core.calculation.VoltageDropCalculator
import com.electricalengineeringpro.app.core.sld.SldGenerator

class ProfessionalEngineeringCore {

    val power = PowerCalculator()

    val loads = LoadCalculator()

    val designSummary = DesignSummaryCalculator()

    val cables = CableCalculator()

    val breakers = BreakerCalculator()

    val voltageDrop = VoltageDropCalculator()

    val shortCircuit = ShortCircuitCalculator()

    val transformers = TransformerCalculator()

    val motors = MotorCalculator()

    val pumps = PumpCalculator()

    val sld = SldGenerator()
}

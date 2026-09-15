package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculators.DiversityCalculator
import com.electrical.calculationspro.core.calculators.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculators.SldGenerator

/**
 * SINGLE ENGINEERING CORE
 *
 * All professional engineering calculations must be exposed
 * through this core.
 *
 * UI and screens must not contain engineering formulas.
 */
class ProfessionalEngineeringCore {

    val diversity = DiversityCalculator()

    val shortCircuit = ShortCircuitCalculator()

    val sld = SldGenerator()
}

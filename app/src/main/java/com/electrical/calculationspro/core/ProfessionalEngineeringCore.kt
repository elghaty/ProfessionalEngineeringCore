package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculators.DiversityCalculator
import com.electrical.calculationspro.core.calculators.PanelCalculator
import com.electrical.calculationspro.core.calculators.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculators.SldGenerator

/**
 * PROFESSIONAL ENGINEERING CORE
 *
 * The application has one logical engineering core.
 *
 * All engineering calculators are exposed from this boundary.
 *
 * UI -> Facade -> Core -> Calculator
 */
class ProfessionalEngineeringCore {

    val diversity =
        DiversityCalculator()

    val shortCircuit =
        ShortCircuitCalculator()

    val panel =
        PanelCalculator()

    val sld =
        SldGenerator()
}

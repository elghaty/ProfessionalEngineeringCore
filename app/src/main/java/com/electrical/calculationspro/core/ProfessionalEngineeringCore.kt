package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculators.DiversityCalculator
import com.electrical.calculationspro.core.calculators.PanelCalculator
import com.electrical.calculationspro.core.calculators.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculators.SldGenerator
import com.electrical.calculationspro.core.calculators.SldShortCircuitCalculator

/**
 * PROFESSIONAL ENGINEERING CORE
 *
 * Single logical engineering calculation core.
 *
 * UI
 *   ↓
 * ProfessionalEngineeringFacade
 *   ↓
 * ProfessionalEngineeringCore
 *   ↓
 * Engineering Calculators
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

    val sldShortCircuit =
        SldShortCircuitCalculator()
}

package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculation.BreakerCalculator
import com.electrical.calculationspro.core.calculation.CableCalculator
import com.electrical.calculationspro.core.calculation.DesignSummaryCalculator
import com.electrical.calculationspro.core.calculation.ElectricalNetworkCalculator
import com.electrical.calculationspro.core.calculation.GeneratorCalculator
import com.electrical.calculationspro.core.calculation.LoadCalculator
import com.electrical.calculationspro.core.calculation.LoadScheduleCalculator
import com.electrical.calculationspro.core.calculation.MotorCalculator
import com.electrical.calculationspro.core.calculation.PumpCalculator
import com.electrical.calculationspro.core.calculation.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.TransformerCalculator
import com.electrical.calculationspro.core.calculation.VoltageDropCalculator
import com.electrical.calculationspro.core.calculation.PowerCalculator

/**
 * SINGLE ENGINEERING FACADE.
 *
 * All engineering calculations must enter through this object.
 *
 * UI and ViewModels must not contain engineering formulas.
 */
class ProfessionalEngineeringCore private constructor() {

    val power =
        PowerCalculator()

    val loads =
        LoadCalculator()

    val loadSchedule =
        LoadScheduleCalculator(loads)

    val voltageDrop =
        VoltageDropCalculator()

    val shortCircuit =
        ShortCircuitCalculator()

    val breakers =
        BreakerCalculator()

    val cables =
        CableCalculator(
            power = power,
            voltageDrop = voltageDrop,
            shortCircuit = shortCircuit,
            breaker = breakers
        )

    val transformers =
        TransformerCalculator()

    val generators =
        GeneratorCalculator()

    val motors =
        MotorCalculator()

    val pumps =
        PumpCalculator()

    val designSummary =
        DesignSummaryCalculator(
            schedule = loadSchedule
        )

    val network =
        ElectricalNetworkCalculator(
            summary = designSummary
        )

    companion object {

        val instance:
            ProfessionalEngineeringCore by lazy {
                ProfessionalEngineeringCore()
            }
    }
}

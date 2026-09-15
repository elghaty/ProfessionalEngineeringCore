package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculation.BreakerCalculator
import com.electrical.calculationspro.core.calculation.BreakerSelectionCalculator
import com.electrical.calculationspro.core.calculation.CableCalculator
import com.electrical.calculationspro.core.calculation.DesignNetworkCalculator
import com.electrical.calculationspro.core.calculation.DesignSummaryCalculator
import com.electrical.calculationspro.core.calculation.ElectricalNetworkCalculator
import com.electrical.calculationspro.core.calculation.GeneratorCalculator
import com.electrical.calculationspro.core.calculation.LoadCalculator
import com.electrical.calculationspro.core.calculation.LoadScheduleCalculator
import com.electrical.calculationspro.core.calculation.MdbCalculator
import com.electrical.calculationspro.core.calculation.MotorCalculator
import com.electrical.calculationspro.core.calculation.ProtectionCalculator
import com.electrical.calculationspro.core.calculation.PumpCalculator
import com.electrical.calculationspro.core.calculation.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.TransformerCalculator
import com.electrical.calculationspro.core.calculation.TransformerSizingCalculator
import com.electrical.calculationspro.core.calculation.VoltageDropCalculator
import com.electrical.calculationspro.core.calculation.PowerCalculator
import com.electrical.calculationspro.core.sld.SldGenerator

/**
 * SINGLE PROFESSIONAL ENGINEERING FACADE.
 *
 * This is the only public entry point for engineering calculations.
 *
 * UI
 *  ↓
 * ProfessionalEngineeringCore
 *  ↓
 * Individual engineering calculators
 */
class ProfessionalEngineeringCore private constructor() {

    val power =
        PowerCalculator()

    val loads =
        LoadCalculator()

    val loadSchedule =
        LoadScheduleCalculator(
            loadCalculator = loads
        )

    val voltageDrop =
        VoltageDropCalculator()

    val shortCircuit =
        ShortCircuitCalculator()

    val breakers =
        BreakerCalculator()

    val breakerSelection =
        BreakerSelectionCalculator(
            breaker = breakers
        )

    val cables =
        CableCalculator(
            power = power,
            voltageDrop = voltageDrop,
            shortCircuit = shortCircuit,
            breaker = breakers
        )

    val transformers =
        TransformerCalculator()

    val transformerSizing =
        TransformerSizingCalculator()

    val generators =
        GeneratorCalculator()

    val motors =
        MotorCalculator()

    val pumps =
        PumpCalculator()

    val protection =
        ProtectionCalculator()

    val designSummary =
        DesignSummaryCalculator(
            schedule = loadSchedule
        )

    val network =
        ElectricalNetworkCalculator(
            summary = designSummary
        )

    val mdb =
        MdbCalculator(
            summary = designSummary
        )

    val designNetwork =
        DesignNetworkCalculator(
            network = network,
            schedule = loadSchedule,
            mdb = mdb,
            transformer = transformerSizing
        )

    val sld =
        SldGenerator(
            loadCalculator = loads,
            shortCircuit = shortCircuit
        )

    companion object {

        @JvmStatic
        val instance:
            ProfessionalEngineeringCore by lazy {
                ProfessionalEngineeringCore()
            }
    }
}

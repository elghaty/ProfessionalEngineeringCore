package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculation.BreakerCalculator
import com.electrical.calculationspro.core.calculation.BreakerSelectionCalculator
import com.electrical.calculationspro.core.calculation.CableCalculator
import com.electrical.calculationspro.core.calculation.DesignNetworkCalculator
import com.electrical.calculationspro.core.calculation.DesignSummaryCalculator
import com.electrical.calculationspro.core.calculation.DiversityCalculator
import com.electrical.calculationspro.core.calculation.ElectricalNetworkCalculator
import com.electrical.calculationspro.core.calculation.GeneratorCalculator
import com.electrical.calculationspro.core.calculation.LoadCalculator
import com.electrical.calculationspro.core.calculation.LoadScheduleCalculator
import com.electrical.calculationspro.core.calculation.MdbCalculator
import com.electrical.calculationspro.core.calculation.MotorCalculator
import com.electrical.calculationspro.core.calculation.PanelCalculator
import com.electrical.calculationspro.core.calculation.PowerCalculator
import com.electrical.calculationspro.core.calculation.ProtectionCalculator
import com.electrical.calculationspro.core.calculation.PumpCalculator
import com.electrical.calculationspro.core.calculation.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.SldShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.TransformerCalculator
import com.electrical.calculationspro.core.calculation.TransformerSizingCalculator
import com.electrical.calculationspro.core.calculation.VoltageDropCalculator
import com.electrical.calculationspro.core.sld.SldGenerator

object CoreProvider {

    val core: ProfessionalEngineeringCore
        get() = ProfessionalEngineeringCore.instance

    val power: PowerCalculator
        get() = core.power

    val load: LoadCalculator
        get() = core.load

    val loadSchedule: LoadScheduleCalculator
        get() = core.loadSchedule

    val diversity: DiversityCalculator
        get() = core.diversity

    val designSummary: DesignSummaryCalculator
        get() = core.designSummary

    val electricalNetwork: ElectricalNetworkCalculator
        get() = core.electricalNetwork

    val designNetwork: DesignNetworkCalculator
        get() = core.designNetwork

    val mdb: MdbCalculator
        get() = core.mdb

    val panel: PanelCalculator
        get() = core.panel

    val cable: CableCalculator
        get() = core.cable

    val breaker: BreakerCalculator
        get() = core.breaker

    val breakerSelection: BreakerSelectionCalculator
        get() = core.breakerSelection

    val voltageDrop: VoltageDropCalculator
        get() = core.voltageDrop

    val shortCircuit: ShortCircuitCalculator
        get() = core.shortCircuit

    val transformer: TransformerCalculator
        get() = core.transformer

    val transformerSizing: TransformerSizingCalculator
        get() = core.transformerSizing

    val generator: GeneratorCalculator
        get() = core.generator

    val motor: MotorCalculator
        get() = core.motor

    val pump: PumpCalculator
        get() = core.pump

    val protection: ProtectionCalculator
        get() = core.protection

    val sld: SldGenerator
        get() = core.sld

    val sldShortCircuit: SldShortCircuitCalculator
        get() = core.sldShortCircuit
}

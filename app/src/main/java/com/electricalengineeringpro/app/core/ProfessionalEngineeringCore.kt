package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.BreakerCalculator
import com.electricalengineeringpro.app.core.calculation.BreakerSelectionCalculator
import com.electricalengineeringpro.app.core.calculation.CableCalculator
import com.electricalengineeringpro.app.core.calculation.CompleteDesignCalculator
import com.electricalengineeringpro.app.core.calculation.DesignSummaryCalculator
import com.electricalengineeringpro.app.core.calculation.ElectricalNetworkCalculator
import com.electricalengineeringpro.app.core.calculation.GeneratorCalculator
import com.electricalengineeringpro.app.core.calculation.LoadCalculator
import com.electricalengineeringpro.app.core.calculation.LoadScheduleCalculator
import com.electricalengineeringpro.app.core.calculation.MdbCalculator
import com.electricalengineeringpro.app.core.calculation.MotorCalculator
import com.electricalengineeringpro.app.core.calculation.PowerCalculator
import com.electricalengineeringpro.app.core.calculation.ProtectionCalculator
import com.electricalengineeringpro.app.core.calculation.PumpCalculator
import com.electricalengineeringpro.app.core.calculation.ShortCircuitCalculator
import com.electricalengineeringpro.app.core.calculation.TransformerCalculator
import com.electricalengineeringpro.app.core.calculation.TransformerSizingCalculator
import com.electricalengineeringpro.app.core.calculation.VoltageDropCalculator
import com.electricalengineeringpro.app.core.sld.SldGenerator

class ProfessionalEngineeringCore private constructor() {

    val power =
        PowerCalculator()

    val loads =
        LoadCalculator()

    val designSummary =
        DesignSummaryCalculator()

    val loadSchedule =
        LoadScheduleCalculator(loads)

    val cable =
        CableCalculator()

    val breaker =
        BreakerCalculator()

    val breakerSelection =
        BreakerSelectionCalculator()

    val voltageDrop =
        VoltageDropCalculator()

    val shortCircuit =
        ShortCircuitCalculator()

    val transformer =
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

    val mdb =
        MdbCalculator()

    val network =
        ElectricalNetworkCalculator(loads)

    val completeDesign =
        CompleteDesignCalculator(this)

    val sld =
        SldGenerator()

    companion object {

        val instance: ProfessionalEngineeringCore by lazy {
            ProfessionalEngineeringCore()
        }
    }
}

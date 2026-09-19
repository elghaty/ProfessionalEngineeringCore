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
import com.electricalengineeringpro.app.core.calculation.PanelDesignCalculator
import com.electricalengineeringpro.app.core.calculation.PowerCalculator
import com.electricalengineeringpro.app.core.calculation.ProtectionCalculator
import com.electricalengineeringpro.app.core.calculation.PumpCalculator
import com.electricalengineeringpro.app.core.calculation.ShortCircuitCalculator
import com.electricalengineeringpro.app.core.calculation.TransformerCalculator
import com.electricalengineeringpro.app.core.calculation.TransformerSizingCalculator
import com.electricalengineeringpro.app.core.calculation.VoltageDropCalculator
import com.electricalengineeringpro.app.core.model.PanelDesignInput
import com.electricalengineeringpro.app.core.model.PanelDesignResult
import com.electricalengineeringpro.app.core.sld.SldGenerator

/**
 * ProfessionalEngineeringCore
 *
 * THE SINGLE ENGINEERING CALCULATION CORE.
 *
 * This class is the single public entry point for
 * all engineering calculations used by the application.
 *
 * UI classes must not implement engineering formulas.
 */
class ProfessionalEngineeringCore private constructor() {

    val power =
        PowerCalculator()

    val loads =
        LoadCalculator()

    val designSummary =
        DesignSummaryCalculator()

    val loadSchedule =
        LoadScheduleCalculator(
            loads
        )

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
        ElectricalNetworkCalculator(
            loads
        )

    val completeDesign =
        CompleteDesignCalculator(
            loadCalculator = loads,
            transformerSizingCalculator = transformerSizing,
            breakerSelectionCalculator = breakerSelection
        )

    /*
     * Panel design remains inside the single
     * ProfessionalEngineeringCore architecture.
     */
    private val panelDesignCalculator =
        PanelDesignCalculator(
            cableCalculator = cable,
            breakerCalculator = breakerSelection,
            transformerSizingCalculator = transformerSizing
        )

    val sld =
        SldGenerator()

    /**
     * Single public entry point for complete
     * panel feeder engineering design.
     *
     * All engineering calculations are delegated
     * to PanelDesignCalculator.
     */
    fun calculatePanelDesign(
        input: PanelDesignInput
    ): PanelDesignResult {

        return panelDesignCalculator.calculate(
            input
        )
    }

    companion object {

        val instance:
            ProfessionalEngineeringCore by lazy {
                ProfessionalEngineeringCore()
            }
    }
}

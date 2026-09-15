package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.BreakerCalculator
import com.electricalengineeringpro.app.core.calculation.BreakerSelectionCalculator
import com.electricalengineeringpro.app.core.calculation.CableCalculator
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
import com.electricalengineeringpro.app.core.report.EngineeringReportGenerator
import com.electricalengineeringpro.app.core.sld.SldGenerator

/**
 * ============================================================
 * PROFESSIONAL ENGINEERING CORE
 * ============================================================
 *
 * SINGLE ENGINEERING FACADE.
 *
 * This is the ONLY public entry point to the engineering
 * calculation system.
 *
 * The individual calculators remain independent modules.
 *
 * Architecture:
 *
 * UI
 *   ↓
 * ProfessionalEngineeringCore
 *   ↓
 * Individual Calculators
 *   ↓
 * Engineering Results
 *
 * IMPORTANT:
 *
 * 1. No engineering formulas are implemented here.
 * 2. No calculation logic is duplicated here.
 * 3. Each calculator remains responsible for its discipline.
 * 4. UI must not instantiate calculators directly.
 * 5. Database/repository classes must not contain formulas.
 *
 * Power is handled internally and at application level in kW
 * as the active-power engineering unit.
 */
class ProfessionalEngineeringCore private constructor() {

    // =========================================================
    // POWER
    // =========================================================

    val power: PowerCalculator =
        PowerCalculator()

    // =========================================================
    // LOADS
    // =========================================================

    val loads: LoadCalculator =
        LoadCalculator()

    val designSummary: DesignSummaryCalculator =
        DesignSummaryCalculator()

    val loadSchedule: LoadScheduleCalculator =
        LoadScheduleCalculator(loads)

    // =========================================================
    // CABLES
    // =========================================================

    val cables: CableCalculator =
        CableCalculator()

    // =========================================================
    // BREAKERS
    // =========================================================

    val breakers: BreakerCalculator =
        BreakerCalculator()

    val breakerSelection: BreakerSelectionCalculator =
        BreakerSelectionCalculator()

    // =========================================================
    // VOLTAGE DROP
    // =========================================================

    val voltageDrop: VoltageDropCalculator =
        VoltageDropCalculator()

    // =========================================================
    // SHORT CIRCUIT
    // =========================================================

    val shortCircuit: ShortCircuitCalculator =
        ShortCircuitCalculator()

    // =========================================================
    // TRANSFORMERS
    // =========================================================

    val transformers: TransformerCalculator =
        TransformerCalculator()

    val transformerSizing: TransformerSizingCalculator =
        TransformerSizingCalculator()

    // =========================================================
    // GENERATORS
    // =========================================================

    val generators: GeneratorCalculator =
        GeneratorCalculator()

    // =========================================================
    // MOTORS
    // =========================================================

    val motors: MotorCalculator =
        MotorCalculator()

    // =========================================================
    // PUMPS
    // =========================================================

    val pumps: PumpCalculator =
        PumpCalculator()

    // =========================================================
    // PROTECTION
    // =========================================================

    val protection: ProtectionCalculator =
        ProtectionCalculator()

    // =========================================================
    // MDB / PANEL DESIGN
    // =========================================================

    val mdb: MdbCalculator =
        MdbCalculator()

    // =========================================================
    // ELECTRICAL NETWORK
    // =========================================================

    val network: ElectricalNetworkCalculator =
        ElectricalNetworkCalculator(loads)

    // =========================================================
    // SLD
    // =========================================================

    val sld: SldGenerator =
        SldGenerator()

    // =========================================================
    // ENGINEERING REPORTS
    // =========================================================

    val reports: EngineeringReportGenerator =
        EngineeringReportGenerator(this)

    // =========================================================
    // SINGLE INSTANCE
    // =========================================================

    companion object {

        /**
         * Single shared engineering core instance.
         *
         * The application must use:
         *
         * ProfessionalEngineeringCore.instance
         *
         * instead of creating calculators directly.
         */
        val instance: ProfessionalEngineeringCore by lazy {
            ProfessionalEngineeringCore()
        }
    }
}

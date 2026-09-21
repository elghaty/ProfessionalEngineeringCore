package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.*
import com.electricalengineeringpro.app.core.model.BreakerInput
import com.electricalengineeringpro.app.core.model.BreakerResult
import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.CableResult
import com.electricalengineeringpro.app.core.model.DesignSummary
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.MotorInput
import com.electricalengineeringpro.app.core.model.MotorResult
import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.PanelDesignInput
import com.electricalengineeringpro.app.core.model.PanelDesignResult
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.PumpInput
import com.electricalengineeringpro.app.core.model.PumpResult
import com.electricalengineeringpro.app.core.model.ShortCircuitInput
import com.electricalengineeringpro.app.core.model.ShortCircuitResult
import com.electricalengineeringpro.app.core.model.TransformerInput
import com.electricalengineeringpro.app.core.model.TransformerResult
import com.electricalengineeringpro.app.core.model.VoltageDropResult
import com.electricalengineeringpro.app.core.sld.SldGenerator
import com.electricalengineeringpro.app.core.sld.SingleLineDiagram

/**
 * Single public facade for the engineering calculation layer.
 *
 * This class contains:
 * - No engineering formulas
 * - No calculation logic
 * - No UI code
 *
 * All engineering calculations are delegated to the
 * existing calculator classes in the calculation package.
 *
 * Android should communicate with the engineering layer
 * through this facade.
 */
class ProfessionalEngineeringCore private constructor() {

    // ============================================================
    // EXISTING CALCULATORS
    // ============================================================

    private val powerCalculator =
        PowerCalculator()

    private val loadCalculator =
        LoadCalculator()

    private val loadScheduleCalculator =
        LoadScheduleCalculator(
            loadCalculator
        )

    private val cableCalculator =
        CableCalculator()

    private val breakerCalculator =
        BreakerCalculator()

    private val breakerSelectionCalculator =
        BreakerSelectionCalculator()

    private val voltageDropCalculator =
        VoltageDropCalculator()

    private val shortCircuitCalculator =
        ShortCircuitCalculator()

    private val transformerCalculator =
        TransformerCalculator()

    private val transformerSizingCalculator =
        TransformerSizingCalculator()

    private val generatorCalculator =
        GeneratorCalculator()

    private val motorCalculator =
        MotorCalculator()

    private val pumpCalculator =
        PumpCalculator()

    private val protectionCalculator =
        ProtectionCalculator()

    private val mdbCalculator =
        MdbCalculator()

    private val designSummaryCalculator =
        DesignSummaryCalculator()

    private val electricalNetworkCalculator =
        ElectricalNetworkCalculator(
            loadCalculator
        )

    private val completeDesignCalculator =
        CompleteDesignCalculator(
            loadCalculator = loadCalculator,
            transformerSizingCalculator =
                transformerSizingCalculator,
            breakerSelectionCalculator =
                breakerSelectionCalculator
        )

    private val panelDesignCalculator =
        PanelDesignCalculator(
            cableCalculator = cableCalculator,
            breakerCalculator =
                breakerSelectionCalculator,
            transformerSizingCalculator =
                transformerSizingCalculator
        )

    private val sldGenerator =
        SldGenerator()


    // ============================================================
    // POWER CALCULATIONS
    // ============================================================

    fun calculatePower(
        powerKw: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): PowerResult {

        return powerCalculator.fromKw(
            powerKw = powerKw,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
    }

    fun calculatePowerFromKva(
        kva: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): PowerResult {

        return powerCalculator.fromKva(
            kva = kva,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
    }


    // ============================================================
    // LOAD CALCULATIONS
    // ============================================================

    fun calculateLoad(
        load: ElectricalLoad
    ): LoadResult {

        return loadCalculator.calculate(
            load
        )
    }


    // ============================================================
    // LOAD SCHEDULE
    // ============================================================

    fun calculateLoadSchedule(
        loads: List<ElectricalLoad>
    ): LoadScheduleResult {

        return loadScheduleCalculator.calculate(
            loads
        )
    }


    // ============================================================
    // CABLE
    // ============================================================

    fun calculateCable(
        input: CableInput
    ): CableResult {

        return cableCalculator.calculate(
            input
        )
    }


    // ============================================================
    // BREAKER
    // ============================================================

    fun calculateBreaker(
        input: BreakerInput
    ): BreakerResult {

        return breakerCalculator.calculate(
            input
        )
    }


    // ============================================================
    // BREAKER SELECTION
    // ============================================================

    fun calculateBreakerSelection(
        input: BreakerSelectionInput
    ): BreakerSelectionResult {

        return breakerSelectionCalculator.calculate(
            input
        )
    }


    // ============================================================
    // VOLTAGE DROP
    // ============================================================

    fun calculateVoltageDrop(
        currentA: Double,
        lengthM: Double,
        resistanceOhmPerKm: Double,
        reactanceOhmPerKm: Double,
        voltage: Double,
        powerFactor: Double,
        phase: Phase,
        maximumPercent: Double
    ): VoltageDropResult {

        return voltageDropCalculator.calculate(
            currentA = currentA,
            lengthM = lengthM,
            resistanceOhmPerKm =
                resistanceOhmPerKm,
            reactanceOhmPerKm =
                reactanceOhmPerKm,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase,
            maximumPercent =
                maximumPercent
        )
    }


    // ============================================================
    // SHORT CIRCUIT
    // ============================================================

    fun calculateShortCircuit(
        input: ShortCircuitInput
    ): ShortCircuitResult {

        return shortCircuitCalculator.calculate(
            input
        )
    }


    // ============================================================
    // TRANSFORMER
    // ============================================================

    fun calculateTransformer(
        input: TransformerInput
    ): TransformerResult {

        return transformerCalculator.calculate(
            input
        )
    }


    // ============================================================
    // TRANSFORMER SIZING
    // ============================================================

    fun calculateTransformerSizing(
        input: TransformerSizingInput
    ): TransformerSizingResult {

        return transformerSizingCalculator.calculate(
            input
        )
    }


    // ============================================================
    // GENERATOR
    // ============================================================

    fun calculateGenerator(
        input: GeneratorInput
    ): GeneratorResult {

        return generatorCalculator.calculate(
            input
        )
    }


    // ============================================================
    // MOTOR
    // ============================================================

    fun calculateMotor(
        input: MotorInput
    ): MotorResult {

        return motorCalculator.calculate(
            input
        )
    }


    // ============================================================
    // PUMP
    // ============================================================

    fun calculatePump(
        input: PumpInput
    ): PumpResult {

        return pumpCalculator.calculate(
            input
        )
    }


    // ============================================================
    // PROTECTION
    // ============================================================

    fun calculateProtection(
        input: ProtectionInput
    ): ProtectionResult {

        return protectionCalculator.calculate(
            input
        )
    }


    // ============================================================
    // MDB
    // ============================================================

    fun calculateMDB(
        input: MdbInput
    ): MdbResult {

        return mdbCalculator.calculate(
            input
        )
    }


    // ============================================================
    // ELECTRICAL NETWORK
    // ============================================================

    fun calculateNetwork(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): ElectricalNetworkResult {

        return electricalNetworkCalculator.calculate(
            loads = loads,
            voltageV = voltageV,
            powerFactor = powerFactor,
            phase = phase
        )
    }


    // ============================================================
    // COMPLETE DESIGN
    // ============================================================

    fun calculateCompleteDesign(
        input: CompleteDesignInput
    ): CompleteDesignResult {

        return completeDesignCalculator.calculate(
            input
        )
    }


    // ============================================================
    // DESIGN SUMMARY
    // ============================================================

    fun calculateDesignSummary(
        loads: List<ElectricalLoad>,
        voltage: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): DesignSummary {

        return designSummaryCalculator.calculate(
            loads = loads,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
    }


    // ============================================================
    // PANEL DESIGN
    // ============================================================

    fun calculatePanelDesign(
        input: PanelDesignInput
    ): PanelDesignResult {

        return panelDesignCalculator.calculate(
            input
        )
    }


    // ============================================================
    // SLD GENERATION
    // ============================================================

    fun generateSld(
        source: NetworkElement,
        panels: List<NetworkElement>,
        feeders: List<NetworkElement>
    ): SingleLineDiagram {

        return sldGenerator.generate(
            source = source,
            panels = panels,
            feeders = feeders
        )
    }


    // ============================================================
    // SINGLETON
    // ============================================================

    companion object {

        val instance:
            ProfessionalEngineeringCore by lazy {
                ProfessionalEngineeringCore()
            }
    }
}

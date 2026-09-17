package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculation.BasicElectricalCalculator
import com.electrical.calculationspro.core.calculation.BreakerCalculator
import com.electrical.calculationspro.core.calculation.BreakerSelectionCalculator
import com.electrical.calculationspro.core.calculation.CableCalculator
import com.electrical.calculationspro.core.calculation.DesignSummaryCalculator
import com.electrical.calculationspro.core.calculation.DiversityCalculator
import com.electrical.calculationspro.core.calculation.ElectricalNetworkCalculator
import com.electrical.calculationspro.core.calculation.GeneratorCalculator
import com.electrical.calculationspro.core.calculation.LoadCalculator
import com.electrical.calculationspro.core.calculation.LoadScheduleCalculator
import com.electrical.calculationspro.core.calculation.MdbCalculator
import com.electrical.calculationspro.core.calculation.MotorCalculator
import com.electrical.calculationspro.core.calculation.PanelCalculator
import com.electrical.calculationspro.core.calculation.PumpCalculator
import com.electrical.calculationspro.core.calculation.PowerCalculator
import com.electrical.calculationspro.core.calculation.ProtectionCalculator
import com.electrical.calculationspro.core.calculation.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.SldShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.TransformerCalculator
import com.electrical.calculationspro.core.calculation.TransformerSizingCalculator
import com.electrical.calculationspro.core.calculation.VoltageDropCalculator
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.PowerResult
import com.electrical.calculationspro.core.model.ProjectSummaryResult
import com.electrical.calculationspro.core.model.SldNetwork
import com.electrical.calculationspro.core.model.SldResult
import com.electrical.calculationspro.core.sld.SldGenerator

/**
 * SINGLE PUBLIC ENGINEERING FACADE.
 *
 * All application engineering calculations
 * must enter through this class.
 *
 * UI and ViewModels must not implement
 * engineering formulas.
 */
class ProfessionalEngineeringCore private constructor() {

    val basicElectricalCalculator =
        BasicElectricalCalculator()

    val powerCalculator =
        PowerCalculator()

    val loadCalculator =
        LoadCalculator(
            powerCalculator =
                powerCalculator
        )

    val loadScheduleCalculator =
        LoadScheduleCalculator(
            loadCalculator =
                loadCalculator
        )

    val diversityCalculator =
        DiversityCalculator()

    val designSummaryCalculator =
        DesignSummaryCalculator(
            loadScheduleCalculator =
                loadScheduleCalculator
        )

    val voltageDropCalculator =
        VoltageDropCalculator()

    val shortCircuitCalculator =
        ShortCircuitCalculator()

    val breakerCalculator =
        BreakerCalculator()

    val breakerSelectionCalculator =
        BreakerSelectionCalculator(
            breakerCalculator =
                breakerCalculator
        )

    val cableCalculator =
        CableCalculator(
            loadCalculator =
                loadCalculator,

            voltageDropCalculator =
                voltageDropCalculator,

            shortCircuitCalculator =
                shortCircuitCalculator,

            breakerCalculator =
                breakerCalculator
        )

    val transformerCalculator =
        TransformerCalculator()

    val transformerSizingCalculator =
        TransformerSizingCalculator()

    val generatorCalculator =
        GeneratorCalculator()

    val motorCalculator =
        MotorCalculator()

    val pumpCalculator =
        PumpCalculator()

    val protectionCalculator =
        ProtectionCalculator()

    val panelCalculator =
        PanelCalculator(
            breaker =
                breakerCalculator
        )

    val mdbCalculator =
        MdbCalculator(
            summary =
                designSummaryCalculator
        )

    val electricalNetworkCalculator =
        ElectricalNetworkCalculator(
            loadScheduleCalculator =
                loadScheduleCalculator,

            transformerSizingCalculator =
                transformerSizingCalculator,

            breakerCalculator =
                breakerCalculator
        )

    val sldShortCircuitCalculator =
        SldShortCircuitCalculator(
            shortCircuitCalculator =
                shortCircuitCalculator
        )

    val sldGenerator =
        SldGenerator(
            loadCalculator =
                loadCalculator,

            shortCircuitCalculator =
                sldShortCircuitCalculator
        )

    fun calculatePower(
        powerKw: Double,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): PowerResult {

        return powerCalculator.fromKw(
            powerKw = powerKw,
            voltageV = voltageV,
            powerFactor = powerFactor,
            phase = phase
        )
    }

    fun calculatePowerFromKva(
        apparentPowerKva: Double,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): PowerResult {

        return powerCalculator.fromKva(
            apparentPowerKva =
                apparentPowerKva,

            voltageV =
                voltageV,

            powerFactor =
                powerFactor,

            phase =
                phase
        )
    }

    fun calculateVoltageFromPower(
        powerKw: Double,
        currentA: Double,
        powerFactor: Double,
        phaseCount: Int
    ): Double {

        return basicElectricalCalculator
            .voltageFromPower(
                powerKw =
                    powerKw,

                currentA =
                    currentA,

                powerFactor =
                    powerFactor,

                phaseCount =
                    phaseCount
            )
    }

    fun calculateResistance(
        voltageV: Double,
        currentA: Double
    ): Double {

        return basicElectricalCalculator
            .resistance(
                voltageV =
                    voltageV,

                currentA =
                    currentA
            )
    }

    fun calculateImpedance(
        voltageV: Double,
        currentA: Double
    ): Double {

        return basicElectricalCalculator
            .impedanceMagnitude(
                voltageV =
                    voltageV,

                currentA =
                    currentA
            )
    }

    fun calculateReactivePower(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double {

        return basicElectricalCalculator
            .reactivePowerKvar(
                activePowerKw =
                    activePowerKw,

                apparentPowerKva =
                    apparentPowerKva
            )
    }

    fun calculatePowerFactor(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double {

        return basicElectricalCalculator
            .powerFactor(
                activePowerKw =
                    activePowerKw,

                apparentPowerKva =
                    apparentPowerKva
            )
    }

    fun calculateLoad(
        load: ElectricalLoad
    ) =
        loadCalculator.calculate(
            load
        )

    fun calculateLoadSchedule(
        loads: List<ElectricalLoad>
    ) =
        loadScheduleCalculator.calculate(
            loads
        )

    fun calculateProjectSummary(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): ProjectSummaryResult {

        val result =
            designSummaryCalculator.calculate(
                loads =
                    loads,

                voltageV =
                    voltageV,

                powerFactor =
                    powerFactor,

                designMargin =
                    designMargin
            )

        return ProjectSummaryResult(
            connectedLoadKw =
                result.connectedLoadKw,

            demandLoadKw =
                result.demandLoadKw,

            designLoadKw =
                result.designLoadKw,

            apparentPowerKva =
                result.apparentPowerKva,

            mainCurrentA =
                result.mainCurrentA,

            recommendedTransformerKva =
                result.recommendedTransformerKva,

            recommendedMainBreakerA =
                result.recommendedMainBreakerA
        )
    }

    fun generateSld(
        network: SldNetwork,
        sourceFaultMva: Double = 1000.0,
        voltageFactor: Double = 1.05
    ): SldResult {

        return sldGenerator.generate(
            network =
                network,

            sourceFaultMva =
                sourceFaultMva,

            voltageFactor =
                voltageFactor
        )
    }

    companion object {

        @JvmStatic
        val instance:
            ProfessionalEngineeringCore by lazy {
                ProfessionalEngineeringCore()
            }
    }
}

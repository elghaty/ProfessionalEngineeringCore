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
import com.electrical.calculationspro.core.calculation.PowerCalculator
import com.electrical.calculationspro.core.calculation.ProtectionCalculator
import com.electrical.calculationspro.core.calculation.PumpCalculator
import com.electrical.calculationspro.core.calculation.ShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.SldGenerator
import com.electrical.calculationspro.core.calculation.SldShortCircuitCalculator
import com.electrical.calculationspro.core.calculation.TransformerCalculator
import com.electrical.calculationspro.core.calculation.TransformerSizingCalculator
import com.electrical.calculationspro.core.calculation.VoltageDropCalculator
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.PowerResult
import com.electrical.calculationspro.core.model.ProjectSummaryResult
import com.electrical.calculationspro.core.model.SldElement
import com.electrical.calculationspro.core.model.SldNetwork
import com.electrical.calculationspro.core.model.SldResult

class ProfessionalEngineeringCore private constructor() {

    val basicElectricalCalculator =
        BasicElectricalCalculator()

    val powerCalculator =
        PowerCalculator()

    val loadCalculator =
        LoadCalculator(powerCalculator)

    val loadScheduleCalculator =
        LoadScheduleCalculator(loadCalculator)

    val diversityCalculator =
        DiversityCalculator()

    val designSummaryCalculator =
        DesignSummaryCalculator(loadScheduleCalculator)

    val voltageDropCalculator =
        VoltageDropCalculator()

    val shortCircuitCalculator =
        ShortCircuitCalculator()

    val breakerCalculator =
        BreakerCalculator()

    val breakerSelectionCalculator =
        BreakerSelectionCalculator(breakerCalculator)

    val cableCalculator =
        CableCalculator(
            loadCalculator = loadCalculator,
            voltageDropCalculator = voltageDropCalculator,
            shortCircuitCalculator = shortCircuitCalculator,
            breakerCalculator = breakerCalculator
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
        PanelCalculator(breakerCalculator)

    val mdbCalculator =
        MdbCalculator(designSummaryCalculator)

    val electricalNetworkCalculator =
        ElectricalNetworkCalculator(
            loadScheduleCalculator = loadScheduleCalculator,
            transformerSizingCalculator = transformerSizingCalculator,
            breakerCalculator = breakerCalculator
        )

    val sldShortCircuitCalculator =
        SldShortCircuitCalculator(shortCircuitCalculator)

    val sldGenerator =
        SldGenerator()

    fun calculatePower(
        powerKw: Double,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): PowerResult =
        powerCalculator.fromKw(
            powerKw = powerKw,
            voltageV = voltageV,
            powerFactor = powerFactor,
            phase = phase
        )

    fun calculatePowerFromKva(
        apparentPowerKva: Double,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): PowerResult =
        powerCalculator.fromKva(
            apparentPowerKva = apparentPowerKva,
            voltageV = voltageV,
            powerFactor = powerFactor,
            phase = phase
        )

    fun calculateVoltageFromPower(
        powerKw: Double,
        currentA: Double,
        powerFactor: Double,
        phaseCount: Int
    ): Double =
        basicElectricalCalculator.voltageFromPower(
            powerKw,
            currentA,
            powerFactor,
            phaseCount
        )

    fun calculateResistance(
        voltageV: Double,
        currentA: Double
    ): Double =
        basicElectricalCalculator.resistance(
            voltageV,
            currentA
        )

    fun calculateImpedance(
        voltageV: Double,
        currentA: Double
    ): Double =
        basicElectricalCalculator.impedanceMagnitude(
            voltageV,
            currentA
        )

    fun calculateReactivePower(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double =
        basicElectricalCalculator.reactivePowerKvar(
            activePowerKw,
            apparentPowerKva
        )

    fun calculatePowerFactor(
        activePowerKw: Double,
        apparentPowerKva: Double
    ): Double =
        basicElectricalCalculator.powerFactor(
            activePowerKw,
            apparentPowerKva
        )

    fun calculateLoad(
        load: ElectricalLoad
    ) =
        loadCalculator.calculate(load)

    fun calculateLoadSchedule(
        loads: List<ElectricalLoad>
    ) =
        loadScheduleCalculator.calculate(loads)

    fun calculateProjectSummary(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): ProjectSummaryResult {

        val r =
            designSummaryCalculator.calculate(
                loads = loads,
                voltageV = voltageV,
                powerFactor = powerFactor,
                designMargin = designMargin
            )

        return ProjectSummaryResult(
            connectedLoadKw = r.connectedLoadKw,
            demandLoadKw = r.demandLoadKw,
            designLoadKw = r.designLoadKw,
            apparentPowerKva = r.apparentPowerKva,
            mainCurrentA = r.mainCurrentA,
            recommendedTransformerKva =
                r.recommendedTransformerKva,
            recommendedMainBreakerA =
                r.recommendedMainBreakerA
        )
    }

    fun generateSld(
        network: SldNetwork,
        sourceFaultMva: Double = 1000.0,
        voltageFactor: Double = 1.05
    ): SldResult {

        val generated =
            sldGenerator.build(
                elements = network.elements,
                connections = network.connections
            )

        val shortCircuit =
            sldShortCircuitCalculator.calculate(
                network = generated,
                sourceShortCircuitMva = sourceFaultMva,
                voltageFactor = voltageFactor
            )

        val elementResults =
            generated.elements.map { element ->

                val loadResult =
                    if (element.powerKw > 0.0) {

                        loadCalculator.calculate(
                            ElectricalLoad(
                                id = element.id,
                                name = element.name,
                                powerKw = element.powerKw,
                                quantity =
                                    element.quantity.coerceAtLeast(1),
                                voltageV = element.voltageV,
                                phase = element.phase,
                                powerFactor =
                                    element.powerFactor,
                                efficiency =
                                    element.efficiency,
                                demandFactor =
                                    element.demandFactor,
                                diversityFactor =
                                    element.diversityFactor
                            )
                        )
                    } else {
                        null
                    }

                com.electrical.calculationspro.core.model.SldElementResult(
                    elementId = element.id,
                    powerKw =
                        loadResult?.designKw
                            ?: 0.0,
                    apparentPowerKva =
                        loadResult?.apparentPowerKva
                            ?: 0.0,
                    currentA =
                        loadResult?.currentA
                            ?: 0.0,
                    shortCircuitKA =
                        shortCircuit.results[element.id]
                            ?.faultCurrentKA
                            ?: 0.0
                )
            }

        val loadElements =
            generated.elements.filter {
                it.powerKw > 0.0
            }

        val totalLoadKw =
            loadElements.sumOf {
                it.powerKw *
                    it.quantity.coerceAtLeast(1)
            }

        val totalDemandKw =
            loadElements.sumOf {
                it.powerKw *
                    it.quantity.coerceAtLeast(1) *
                    it.demandFactor.coerceIn(0.0, 1.0) *
                    it.diversityFactor.coerceIn(0.0, 1.0)
            }

        val source =
            generated.elements.firstOrNull {
                it.sourceType != null
            }

        val sourceVoltage =
            source?.voltageV ?: 400.0

        val sourcePf =
            loadElements
                .map { it.powerFactor }
                .filter { it > 0.0 }
                .average()
                .takeIf { it.isFinite() && it > 0.0 }
                ?: 0.90

        val sourceCurrentA =
            if (totalDemandKw > 0.0) {
                powerCalculator
                    .fromKw(
                        powerKw = totalDemandKw,
                        voltageV = sourceVoltage,
                        powerFactor = sourcePf,
                        phase = Phase.THREE
                    )
                    .currentA
            } else {
                0.0
            }

        return SldResult(
            elements = elementResults,
            totalLoadKw = totalLoadKw,
            totalDemandKw = totalDemandKw,
            sourceCurrentA = sourceCurrentA,
            sourceFaultCurrentKA =
                shortCircuit.maximumFaultCurrentKA
        )
    }

    companion object {

        @JvmStatic
        val instance: ProfessionalEngineeringCore by lazy {
            ProfessionalEngineeringCore()
        }
    }
}

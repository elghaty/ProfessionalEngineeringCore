package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculation.DesignSummaryCalculator
import com.electrical.calculationspro.core.calculation.DiversityCalculator
import com.electrical.calculationspro.core.calculation.LoadCalculator
import com.electrical.calculationspro.core.calculation.LoadScheduleCalculator
import com.electrical.calculationspro.core.calculation.PowerCalculator
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.PowerResult
import com.electrical.calculationspro.core.model.ProjectSummaryResult

/**
 * SINGLE ENGINEERING FACADE.
 *
 * This is the only public engineering entry point
 * that the Android application should use.
 *
 * UI and ViewModels must not contain engineering formulas.
 *
 * Internal engineering power unit:
 * kW.
 */
class ProfessionalEngineeringCore private constructor() {

    private val powerCalculator =
        PowerCalculator()

    private val loadCalculator =
        LoadCalculator(
            powerCalculator = powerCalculator
        )

    private val loadScheduleCalculator =
        LoadScheduleCalculator(
            loadCalculator = loadCalculator
        )

    private val diversityCalculator =
        DiversityCalculator()

    private val designSummaryCalculator =
        DesignSummaryCalculator(
            loadScheduleCalculator =
                loadScheduleCalculator
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

    fun calculateLoad(
        load: ElectricalLoad
    ) =
        loadCalculator.calculate(load)

    fun calculateLoadSchedule(
        loads: List<ElectricalLoad>
    ) =
        loadScheduleCalculator.calculate(loads)

    fun calculateDiversity(
        loads: List<com.electrical.calculationspro.core.calculation.DiversityLoad>,
        additionalDiversityFactor: Double = 1.0
    ) =
        diversityCalculator.calculate(
            loads = loads,
            additionalDiversityFactor =
                additionalDiversityFactor
        )

    fun calculateProjectSummary(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.90,
        designMargin: Double = 1.15
    ): ProjectSummaryResult {

        val result =
            designSummaryCalculator.calculate(
                loads = loads,
                voltageV = voltageV,
                powerFactor = powerFactor,
                designMargin = designMargin
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

    companion object {

        @JvmStatic
        val instance:
            ProfessionalEngineeringCore by lazy {
                ProfessionalEngineeringCore()
            }
    }
}

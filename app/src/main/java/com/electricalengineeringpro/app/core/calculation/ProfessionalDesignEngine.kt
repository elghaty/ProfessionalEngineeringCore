package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.DesignSummary
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase

class ProfessionalDesignEngine(
    private val loadCalculator: LoadCalculator = LoadCalculator(),
    private val loadScheduleCalculator: LoadScheduleCalculator =
        LoadScheduleCalculator(loadCalculator),
    private val designSummaryCalculator: DesignSummaryCalculator =
        DesignSummaryCalculator()
) {

    fun calculateLoads(
        loads: List<ElectricalLoad>
    ): List<LoadResult> =
        loads.map(loadCalculator::calculate)

    fun calculateSchedule(
        loads: List<ElectricalLoad>
    ): LoadScheduleResult =
        loadScheduleCalculator.calculate(loads)

    fun calculateSummary(
        loads: List<ElectricalLoad>,
        voltage: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): DesignSummary =
        designSummaryCalculator.calculate(
            loads = loads,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
}

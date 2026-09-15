package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.*

class ProfessionalDesignEngine {

    private val loadCalculator = LoadCalculator()
    private val designSummaryCalculator = DesignSummaryCalculator()

    fun calculateLoads(
        loads: List<ElectricalLoad>
    ): List<LoadResult> {
        return loads.map(loadCalculator::calculate)
    }

    fun calculateSummary(
        loads: List<ElectricalLoad>,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): DesignSummary {
        return designSummaryCalculator.calculate(
            loads = loads,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
    }
}

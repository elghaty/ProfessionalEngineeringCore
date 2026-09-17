package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.DesignSummary
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase

class ProfessionalDesignEngine(
    private val loadCalculator: LoadCalculator = LoadCalculator(),
    private val designSummaryCalculator: DesignSummaryCalculator =
        DesignSummaryCalculator()
) {

    fun calculateLoads(
        loads: List<ElectricalLoad>
    ): List<LoadResult> {

        return loads.map {
            loadCalculator.calculate(it)
        }
    }

    fun calculateSummary(
        loads: List<ElectricalLoad>,
        voltage: Double,
        powerFactor: Double,
        phase: Phase
    ): DesignSummary {

        require(voltage > 0.0) {
            "Voltage must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        return designSummaryCalculator.calculate(
            loads = loads
        )
    }
}

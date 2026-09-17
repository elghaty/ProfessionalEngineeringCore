package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.LoadResult

class LoadCalculator(
    private val powerCalculator: PowerCalculator = PowerCalculator()
) {

    fun calculate(load: ElectricalLoad): LoadResult {

        require(load.id.isNotBlank()) {
            "Load id must not be blank."
        }

        require(load.name.isNotBlank()) {
            "Load name must not be blank."
        }

        require(load.powerKw >= 0.0) {
            "Load power must be >= 0 kW."
        }

        require(load.quantity > 0) {
            "Load quantity must be greater than zero."
        }

        require(load.voltageV > 0.0) {
            "Voltage must be greater than zero."
        }

        require(load.powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(load.efficiency in 0.01..1.0) {
            "Efficiency must be between 0.01 and 1.0."
        }

        require(load.demandFactor in 0.0..1.0) {
            "Demand factor must be between 0 and 1."
        }

        require(load.diversityFactor in 0.01..1.0) {
            "Diversity factor must be between 0.01 and 1.0."
        }

        val connectedKw =
            load.powerKw * load.quantity

        val demandKw =
            connectedKw * load.demandFactor

        /*
         * Diversity is a project/schedule-level factor.
         * It must not be divided into every individual load.
         */
        val designKw =
            demandKw

        /*
         * powerKw is treated as useful/rated load power.
         * Efficiency converts it to electrical input power.
         */
        val electricalInputKw =
            designKw / load.efficiency

        val power =
            powerCalculator.fromKw(
                powerKw = electricalInputKw,
                voltageV = load.voltageV,
                powerFactor = load.powerFactor,
                phase = load.phase
            )

        return LoadResult(
            connectedKw = connectedKw,
            demandKw = demandKw,
            designKw = designKw,
            currentA = power.currentA,
            apparentPowerKva = power.apparentPowerKva
        )
    }

    fun calculateAll(
        loads: List<ElectricalLoad>
    ): List<LoadResult> {

        require(loads.isNotEmpty()) {
            "Load list must not be empty."
        }

        return loads.map(::calculate)
    }
}

package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

data class LoadResult(
    val connectedKw: Double,
    val demandKw: Double,
    val designKw: Double,
    val apparentPowerKva: Double,
    val reactivePowerKvar: Double,
    val currentA: Double,
    val startingCurrentA: Double,
    val loadPowerFactor: Double,
    val utilizationPercent: Double
)

class LoadCalculator {

    fun calculate(load: ElectricalLoad): LoadResult {

        require(load.quantity > 0) {
            "Load quantity must be greater than zero."
        }

        require(load.powerKw >= 0.0) {
            "Load power cannot be negative."
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

        require(load.diversityFactor in 1.0..10.0) {
            "Diversity factor must be between 1.0 and 10.0."
        }

        require(load.voltage > 0.0) {
            "Voltage must be greater than zero."
        }

        require(load.startingCurrentMultiplier >= 0.0) {
            "Starting current multiplier cannot be negative."
        }

        val connectedKw =
            load.powerKw * load.quantity

        val demandKw =
            connectedKw * load.demandFactor

        /*
         * Diversity factor is defined as:
         *
         * Diversity Factor =
         * Sum of individual maximum demands /
         * Maximum coincident demand
         *
         * Therefore coincident/design demand is obtained
         * by dividing the sum of individual demands
         * by the diversity factor.
         */
        val designKw =
            demandKw / load.diversityFactor

        val apparentPowerKva =
            designKw / load.powerFactor

        val reactivePowerKvar =
            apparentPowerKva *
                sqrt(
                    (
                        1.0 -
                            load.powerFactor *
                            load.powerFactor
                    ).coerceAtLeast(0.0)
                )

        val currentA =
            when (load.phase) {

                Phase.THREE ->
                    designKw * 1000.0 /
                        (
                            sqrt(3.0) *
                                load.voltage *
                                load.powerFactor
                        )

                Phase.SINGLE ->
                    designKw * 1000.0 /
                        (
                            load.voltage *
                                load.powerFactor
                        )
            }

        val startingCurrentA =
            currentA *
                load.startingCurrentMultiplier

        val utilizationPercent =
            if (connectedKw > 0.0) {
                demandKw /
                    connectedKw *
                    100.0
            } else {
                0.0
            }

        return LoadResult(
            connectedKw = connectedKw,
            demandKw = demandKw,
            designKw = designKw,
            apparentPowerKva = apparentPowerKva,
            reactivePowerKvar = reactivePowerKvar,
            currentA = currentA,
            startingCurrentA = startingCurrentA,
            loadPowerFactor = load.powerFactor,
            utilizationPercent = utilizationPercent
        )
    }
}

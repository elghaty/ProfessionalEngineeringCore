package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.DesignSummary
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

class DesignSummaryCalculator {

    private val loadCalculator =
        LoadCalculator()

    private val breakerSelectionCalculator =
        BreakerSelectionCalculator()

    private val transformerSizingCalculator =
        TransformerSizingCalculator()

    fun calculate(
        loads: List<ElectricalLoad>,
        voltage: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ): DesignSummary {

        require(voltage > 0.0) {
            "Voltage must be greater than zero."
        }

        require(powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        if (loads.isEmpty()) {
            return DesignSummary(
                connectedLoadKw = 0.0,
                demandLoadKw = 0.0,
                designLoadKw = 0.0,
                totalCurrentA = 0.0,
                recommendedMainBreakerA = 0.0,
                recommendedTransformerKva = 0.0
            )
        }

        val results =
            loads.map {
                loadCalculator.calculate(it)
            }

        val connected =
            results.sumOf {
                it.connectedKw
            }

        val demand =
            results.sumOf {
                it.demandKw
            }

        val design =
            results.sumOf {
                it.designKw
            }

        val current =
            when (phase) {

                Phase.THREE ->
                    design * 1000.0 /
                        (
                            sqrt(3.0) *
                                voltage *
                                powerFactor
                        )

                Phase.SINGLE ->
                    design * 1000.0 /
                        (
                            voltage *
                                powerFactor
                        )
            }

        val breaker =
            breakerSelectionCalculator.calculate(
                BreakerSelectionInput(
                    loadCurrentA = current,
                    shortCircuitKA = 0.0,
                    designMarginFactor = 1.0
                )
            )

        val transformer =
            transformerSizingCalculator.calculate(
                TransformerSizingInput(
                    designLoadKW = design,
                    powerFactor = powerFactor,
                    spareCapacityFactor = 1.15
                )
            )

        return DesignSummary(
            connectedLoadKw = connected,
            demandLoadKw = demand,
            designLoadKw = design,
            totalCurrentA = current,
            recommendedMainBreakerA =
                breaker.recommendedRatingA,
            recommendedTransformerKva =
                transformer.recommendedRatingKVA
        )
    }
}

package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.GeneratorInput
import com.electrical.calculationspro.core.model.GeneratorResult
import kotlin.math.sqrt

data class GeneratorDetailedInput(
    val ratingKva: Double,
    val voltageV: Double,
    val powerFactor: Double = 0.80,
    val xdPercent: Double = 15.0,
    val xOverR: Double = 10.0,
    val loadPercent: Double = 100.0
)

data class GeneratorDetailedResult(
    val fullLoadCurrentA: Double,
    val inputPowerKw: Double,
    val apparentPowerKva: Double,
    val initialShortCircuitCurrentKA: Double,
    val steadyStateShortCircuitCurrentKA: Double,
    val faultMva: Double,
    val peakFaultCurrentKA: Double,
    val notes: List<String>
)

class GeneratorCalculator {

    fun calculate(
        input: GeneratorInput
    ): GeneratorResult {

        validate(input)

        val current =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) * input.voltageV)

        val initialFault =
            current /
                (input.xdPercent / 100.0)

        val faultMva =
            sqrt(3.0) *
                input.voltageV *
                initialFault /
                1_000_000.0

        return GeneratorResult(
            fullLoadCurrentA = current,
            initialShortCircuitCurrentKA =
                initialFault / 1000.0,
            faultMva = faultMva
        )
    }

    fun calculateDetailed(
        input: GeneratorDetailedInput
    ): GeneratorDetailedResult {

        validateDetailed(input)

        val current =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) * input.voltageV)

        val inputPower =
            input.ratingKva *
                input.loadPercent /
                100.0 *
                input.powerFactor

        val initialFault =
            current /
                (input.xdPercent / 100.0)

        val steadyStateFault =
            initialFault *
                0.25

        val kappa =
            if (input.xOverR > 0.0) {
                1.02 +
                    0.98 *
                    kotlin.math.exp(
                        -3.0 /
                            input.xOverR
                    )
            } else {
                1.02
            }

        val peak =
            initialFault *
                sqrt(2.0) *
                kappa

        val faultMva =
            sqrt(3.0) *
                input.voltageV *
                initialFault /
                1_000_000.0

        return GeneratorDetailedResult(
            fullLoadCurrentA = current,
            inputPowerKw = inputPower,
            apparentPowerKva =
                input.ratingKva *
                    input.loadPercent /
                    100.0,
            initialShortCircuitCurrentKA =
                initialFault / 1000.0,
            steadyStateShortCircuitCurrentKA =
                steadyStateFault / 1000.0,
            faultMva = faultMva,
            peakFaultCurrentKA =
                peak / 1000.0,
            notes = listOf(
                "Xd'' = %.2f %%"
                    .format(input.xdPercent),
                "X/R = %.2f"
                    .format(input.xOverR),
                "Peak current uses calculated kappa = %.3f"
                    .format(kappa)
            )
        )
    }

    private fun validate(
        input: GeneratorInput
    ) {
        require(input.ratingKva > 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.xdPercent > 0.0)
        require(input.xOverR >= 0.0)
    }

    private fun validateDetailed(
        input: GeneratorDetailedInput
    ) {
        require(input.ratingKva > 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.xdPercent > 0.0)
        require(input.xOverR >= 0.0)
        require(input.loadPercent >= 0.0)
    }
}

package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.MotorInput
import com.electrical.calculationspro.core.model.MotorResult
import kotlin.math.sqrt

enum class MotorStartingMethod {
    DOL,
    STAR_DELTA,
    SOFT_STARTER,
    VFD
}

data class MotorDetailedInput(
    val powerKw: Double,
    val voltageV: Double,
    val powerFactor: Double = 0.85,
    val efficiency: Double = 0.90,
    val startingMultiplier: Double = 6.0,
    val startingMethod: MotorStartingMethod =
        MotorStartingMethod.DOL
)

data class MotorDetailedResult(
    val outputPowerKw: Double,
    val inputPowerKw: Double,
    val fullLoadCurrentA: Double,
    val startingCurrentA: Double,
    val apparentPowerKva: Double,
    val startingKva: Double,
    val notes: List<String>
)

class MotorCalculator {

    fun calculate(
        input: MotorInput
    ): MotorResult {

        validate(input)

        val inputPower =
            input.powerKw /
                input.efficiency

        val kva =
            inputPower /
                input.powerFactor

        val current =
            kva * 1000.0 /
                (sqrt(3.0) * input.voltageV)

        val startingCurrent =
            current *
                input.startingMultiplier

        return MotorResult(
            inputPowerKw = inputPower,
            fullLoadCurrentA = current,
            startingCurrentA =
                startingCurrent,
            apparentPowerKva = kva
        )
    }

    fun calculateDetailed(
        input: MotorDetailedInput
    ): MotorDetailedResult {

        validateDetailed(input)

        val inputPower =
            input.powerKw /
                input.efficiency

        val kva =
            inputPower /
                input.powerFactor

        val current =
            kva * 1000.0 /
                (sqrt(3.0) * input.voltageV)

        val multiplier =
            when (input.startingMethod) {
                MotorStartingMethod.DOL ->
                    input.startingMultiplier

                MotorStartingMethod.STAR_DELTA ->
                    input.startingMultiplier / 3.0

                MotorStartingMethod.SOFT_STARTER ->
                    input.startingMultiplier * 0.45

                MotorStartingMethod.VFD ->
                    input.startingMultiplier * 0.10
            }

        val startingCurrent =
            current * multiplier

        return MotorDetailedResult(
            outputPowerKw =
                input.powerKw,

            inputPowerKw =
                inputPower,

            fullLoadCurrentA =
                current,

            startingCurrentA =
                startingCurrent,

            apparentPowerKva =
                kva,

            startingKva =
                kva * multiplier,

            notes = listOf(
                "Starting method = ${input.startingMethod}",
                "Starting current multiplier = %.2f"
                    .format(multiplier)
            )
        )
    }

    private fun validate(
        input: MotorInput
    ) {
        require(input.powerKw >= 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.efficiency in 0.01..1.0)
        require(input.startingMultiplier >= 0.0)
    }

    private fun validateDetailed(
        input: MotorDetailedInput
    ) {
        require(input.powerKw >= 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.efficiency in 0.01..1.0)
        require(input.startingMultiplier >= 0.0)
    }
}

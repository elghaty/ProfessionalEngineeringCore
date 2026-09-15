package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.PumpInput
import com.electrical.calculationspro.core.model.PumpResult
import kotlin.math.sqrt

data class PumpDetailedInput(
    val flowM3s: Double,
    val headM: Double,
    val densityKgM3: Double = 1000.0,
    val pumpEfficiency: Double = 0.80,
    val motorEfficiency: Double = 0.90,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.85,
    val startingMultiplier: Double = 6.0
)

data class PumpDetailedResult(
    val hydraulicPowerKw: Double,
    val pumpShaftPowerKw: Double,
    val motorInputPowerKw: Double,
    val apparentPowerKva: Double,
    val fullLoadCurrentA: Double,
    val startingCurrentA: Double,
    val notes: List<String>
)

class PumpCalculator {

    private val gravity =
        9.80665

    fun calculate(
        input: PumpInput
    ): PumpResult {

        require(input.hydraulicPowerKw >= 0.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.motorEfficiency in 0.01..1.0)
        require(input.pumpEfficiency in 0.01..1.0)

        val pumpShaftPower =
            input.hydraulicPowerKw /
                input.pumpEfficiency

        val motorInputPower =
            pumpShaftPower /
                input.motorEfficiency

        val kva =
            motorInputPower /
                input.powerFactor

        val current =
            kva * 1000.0 /
                (sqrt(3.0) * input.voltageV)

        return PumpResult(
            motorInputPowerKw =
                motorInputPower,
            currentA = current,
            apparentPowerKva = kva
        )
    }

    fun calculateDetailed(
        input: PumpDetailedInput
    ): PumpDetailedResult {

        validate(input)

        val hydraulicPower =
            input.densityKgM3 *
                gravity *
                input.flowM3s *
                input.headM /
                1000.0

        val shaftPower =
            hydraulicPower /
                input.pumpEfficiency

        val motorPower =
            shaftPower /
                input.motorEfficiency

        val kva =
            motorPower /
                input.powerFactor

        val current =
            kva * 1000.0 /
                (
                    sqrt(3.0) *
                        input.voltageV
                    )

        return PumpDetailedResult(
            hydraulicPowerKw =
                hydraulicPower,

            pumpShaftPowerKw =
                shaftPower,

            motorInputPowerKw =
                motorPower,

            apparentPowerKva =
                kva,

            fullLoadCurrentA =
                current,

            startingCurrentA =
                current *
                    input.startingMultiplier,

            notes = listOf(
                "Flow = %.4f m³/s"
                    .format(input.flowM3s),
                "Head = %.2f m"
                    .format(input.headM),
                "Hydraulic power = %.2f kW"
                    .format(hydraulicPower),
                "Motor input power = %.2f kW"
                    .format(motorPower)
            )
        )
    }

    private fun validate(
        input: PumpDetailedInput
    ) {
        require(input.flowM3s >= 0.0)
        require(input.headM >= 0.0)
        require(input.densityKgM3 > 0.0)
        require(input.pumpEfficiency in 0.01..1.0)
        require(input.motorEfficiency in 0.01..1.0)
        require(input.voltageV > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.startingMultiplier >= 0.0)
    }
}

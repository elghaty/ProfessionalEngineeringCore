package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

data class GeneratorInput(
    val ratingKva: Double,
    val voltageV: Double,
    val powerFactor: Double = 0.8,
    val phase: Phase = Phase.THREE,
    val efficiency: Double = 0.9
)

data class GeneratorResult(
    val ratingKva: Double,
    val activePowerKw: Double,
    val fullLoadCurrentA: Double,
    val estimatedFuelLoadPercent: Double,
    val recommendedBreakerA: Double
)

class GeneratorCalculator {

    fun calculate(input: GeneratorInput): GeneratorResult {

        require(input.ratingKva > 0.0) {
            "Generator rating must be greater than zero."
        }

        require(input.voltageV > 0.0) {
            "Voltage must be greater than zero."
        }

        require(input.powerFactor in 0.1..1.0) {
            "Power factor must be between 0.1 and 1.0."
        }

        require(input.efficiency in 0.1..1.0) {
            "Generator efficiency must be between 0.1 and 1.0."
        }

        val activePowerKw =
            input.ratingKva *
                input.powerFactor *
                input.efficiency

        val current = when (input.phase) {

            Phase.SINGLE ->
                input.ratingKva * 1000.0 /
                    input.voltageV

            Phase.THREE ->
                input.ratingKva * 1000.0 /
                    (sqrt(3.0) * input.voltageV)
        }

        val breaker =
            standardBreaker(current * 1.25)

        return GeneratorResult(
            ratingKva = input.ratingKva,
            activePowerKw = activePowerKw,
            fullLoadCurrentA = current,
            estimatedFuelLoadPercent = 75.0,
            recommendedBreakerA = breaker
        )
    }

    private fun standardBreaker(current: Double): Double {

        val ratings = listOf(
            16.0,
            20.0,
            25.0,
            32.0,
            40.0,
            50.0,
            63.0,
            80.0,
            100.0,
            125.0,
            160.0,
            200.0,
            250.0,
            315.0,
            400.0,
            500.0,
            630.0,
            800.0,
            1000.0,
            1250.0,
            1600.0,
            2000.0,
            2500.0,
            3200.0
        )

        return ratings.firstOrNull {
            it >= current
        } ?: ratings.last()
    }
}

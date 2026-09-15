package com.electricalengineeringpro.app.core.calculation

import kotlin.math.ceil

data class MdbInput(
    val connectedLoadKw: Double,
    val demandFactor: Double = 0.8,
    val powerFactor: Double = 0.9,
    val voltageV: Double = 400.0,
    val spareCapacity: Double = 0.2
)

data class MdbResult(
    val demandLoadKw: Double,
    val apparentPowerKva: Double,
    val designCurrentA: Double,
    val recommendedIncomerA: Double,
    val recommendedBusbarA: Double
)

class MdbCalculator {

    fun calculate(input: MdbInput): MdbResult {

        require(input.connectedLoadKw >= 0)
        require(input.demandFactor in 0.0..1.0)
        require(input.powerFactor in 0.1..1.0)

        val demandKw =
            input.connectedLoadKw * input.demandFactor

        val kva =
            if (input.powerFactor > 0)
                demandKw / input.powerFactor
            else 0.0

        val designKva =
            kva * (1.0 + input.spareCapacity)

        val current =
            designKva * 1000.0 /
                (kotlin.math.sqrt(3.0) * input.voltageV)

        val incomer =
            standardRating(current)

        val busbar =
            standardRating(current * 1.25)

        return MdbResult(
            demandLoadKw = demandKw,
            apparentPowerKva = designKva,
            designCurrentA = current,
            recommendedIncomerA = incomer,
            recommendedBusbarA = busbar
        )
    }

    private fun standardRating(value: Double): Double {
        val ratings = listOf(
            63.0, 80.0, 100.0, 125.0, 160.0, 200.0,
            250.0, 315.0, 400.0, 500.0, 630.0, 800.0,
            1000.0, 1250.0, 1600.0, 2000.0, 2500.0,
            3200.0, 4000.0
        )

        return ratings.firstOrNull { it >= value }
            ?: ceil(value / 500.0) * 500.0
    }
}

package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.BreakerResult
import com.electrical.calculationspro.core.model.BreakerType

class BreakerCalculator {

    private val standardRatings =
        listOf(
            6.0, 10.0, 16.0, 20.0, 25.0,
            32.0, 40.0, 50.0, 63.0,
            80.0, 100.0, 125.0, 160.0,
            200.0, 250.0, 315.0, 400.0,
            500.0, 630.0, 800.0, 1000.0,
            1250.0, 1600.0, 2000.0,
            2500.0, 3200.0, 4000.0,
            5000.0, 6300.0
        )

    fun calculate(
        designCurrentA: Double,
        cableCapacityA: Double,
        shortCircuitKA: Double,
        preferredType: BreakerType = BreakerType.MCCB
    ): BreakerResult {

        require(designCurrentA >= 0.0)
        require(cableCapacityA >= 0.0)
        require(shortCircuitKA >= 0.0)

        val rating =
            standardRatings.firstOrNull {
                it >= designCurrentA
            } ?: standardRatings.last()

        val cableOk =
            rating <= cableCapacityA

        val icu =
            selectBreakingCapacity(
                shortCircuitKA
            )

        val ics =
            icu * 0.75

        val utilization =
            if (rating == 0.0) {
                0.0
            } else {
                designCurrentA /
                    rating *
                    100.0
            }

        val warnings =
            mutableListOf<String>()

        if (!cableOk) {
            warnings +=
                "Breaker rating exceeds cable corrected ampacity."
        }

        if (icu < shortCircuitKA) {
            warnings +=
                "Breaker Icu is below prospective short-circuit current."
        }

        return BreakerResult(
            type = preferredType,
            ratedCurrentA = rating,
            icuKA = icu,
            icsKA = ics,
            utilizationPercent = utilization,
            acceptable =
                cableOk &&
                    icu >= shortCircuitKA,
            warnings = warnings
        )
    }

    private fun selectBreakingCapacity(
        faultKA: Double
    ): Double {

        val values =
            listOf(
                6.0,
                10.0,
                15.0,
                18.0,
                25.0,
                36.0,
                50.0,
                65.0,
                85.0,
                100.0
            )

        return values.firstOrNull {
            it >= faultKA
        } ?: values.last()
    }
}

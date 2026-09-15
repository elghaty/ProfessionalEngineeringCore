package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.BreakerResult
import com.electrical.calculationspro.core.model.BreakerType

class BreakerCalculator {

    private val standardRatingsA =
        listOf(
            6.0,
            10.0,
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
            3200.0,
            4000.0,
            5000.0,
            6300.0
        )

    private val standardBreakingCapacitiesKA =
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

    fun calculate(
        designCurrentA: Double,
        cableCapacityA: Double,
        shortCircuitKA: Double,
        preferredType: BreakerType =
            BreakerType.MCCB
    ): BreakerResult {

        require(designCurrentA >= 0.0)
        require(cableCapacityA >= 0.0)
        require(shortCircuitKA >= 0.0)

        val rating =
            selectRating(
                designCurrentA
            )

        val cableOk =
            cableCapacityA <= 0.0 ||
                rating <= cableCapacityA

        val icu =
            selectBreakingCapacity(
                shortCircuitKA
            )

        val ics =
            calculateIcs(
                icuKA = icu
            )

        val utilization =
            if (rating > 0.0) {
                designCurrentA /
                    rating *
                    100.0
            } else {
                0.0
            }

        val warnings =
            mutableListOf<String>()

        if (!cableOk) {
            warnings +=
                "Breaker rating exceeds cable corrected ampacity."
        }

        if (
            shortCircuitKA > 0.0 &&
            icu < shortCircuitKA
        ) {
            warnings +=
                "Breaker Icu is below prospective short-circuit current."
        }

        if (utilization > 100.0) {
            warnings +=
                "Breaker rated current is below design current."
        }

        return BreakerResult(
            type = preferredType,
            ratedCurrentA = rating,
            icuKA = icu,
            icsKA = ics,
            utilizationPercent = utilization,
            acceptable =
                rating >= designCurrentA &&
                    cableOk &&
                    (
                        shortCircuitKA <= 0.0 ||
                            icu >= shortCircuitKA
                        ),
            warnings = warnings
        )
    }

    fun selectRating(
        designCurrentA: Double
    ): Double {

        require(designCurrentA >= 0.0)

        return standardRatingsA.firstOrNull {
            it >= designCurrentA
        } ?: standardRatingsA.last()
    }

    fun selectBreakingCapacity(
        faultCurrentKA: Double
    ): Double {

        require(faultCurrentKA >= 0.0)

        if (faultCurrentKA == 0.0) {
            return standardBreakingCapacitiesKA.first()
        }

        return standardBreakingCapacitiesKA.firstOrNull {
            it >= faultCurrentKA
        } ?: standardBreakingCapacitiesKA.last()
    }

    private fun calculateIcs(
        icuKA: Double
    ): Double {

        if (icuKA <= 0.0) {
            return 0.0
        }

        return icuKA * 0.75
    }
}

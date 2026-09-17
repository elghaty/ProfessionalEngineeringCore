package com.electricalengineeringpro.app.core.calculation

data class ProtectionInput(
    val designCurrentA: Double,
    val cableAmpacityA: Double,
    val shortCircuitKA: Double,
    val voltageV: Double,
    val margin: Double = 1.25
)

data class ProtectionResult(
    val recommendedBreakerA: Double,
    val breakingCapacityKA: Double,
    val cableProtected: Boolean,
    val shortCircuitProtected: Boolean,
    val status: String
)

class ProtectionCalculator {

    fun calculate(
        input: ProtectionInput
    ): ProtectionResult {

        require(input.designCurrentA > 0.0)
        require(input.cableAmpacityA > 0.0)
        require(input.shortCircuitKA >= 0.0)
        require(input.voltageV > 0.0)
        require(input.margin >= 1.0)

        val requiredBreaker =
            input.designCurrentA * input.margin

        val breaker =
            standardBreaker(requiredBreaker)

        val cableProtected =
            breaker <= input.cableAmpacityA

        val breakingCapacity =
            standardBreakingCapacity(input.shortCircuitKA)

        val shortCircuitProtected =
            breakingCapacity >= input.shortCircuitKA

        val status =
            when {
                !cableProtected ->
                    "Breaker rating exceeds cable ampacity."

                !shortCircuitProtected ->
                    "Breaker breaking capacity is insufficient."

                else ->
                    "Protection arrangement acceptable for preliminary design."
            }

        return ProtectionResult(
            recommendedBreakerA = breaker,
            breakingCapacityKA = breakingCapacity,
            cableProtected = cableProtected,
            shortCircuitProtected = shortCircuitProtected,
            status = status
        )
    }

    private fun standardBreaker(
        current: Double
    ): Double {

        val ratings =
            listOf(
                6.0, 10.0, 16.0, 20.0, 25.0,
                32.0, 40.0, 50.0, 63.0, 80.0,
                100.0, 125.0, 160.0, 200.0,
                250.0, 315.0, 400.0, 500.0,
                630.0, 800.0, 1000.0, 1250.0,
                1600.0, 2000.0, 2500.0, 3200.0,
                4000.0
            )

        return ratings.firstOrNull {
            it >= current
        } ?: current
    }

    private fun standardBreakingCapacity(
        shortCircuitKA: Double
    ): Double {

        val capacities =
            listOf(
                3.0, 4.5, 6.0, 10.0, 15.0,
                18.0, 25.0, 30.0, 36.0, 40.0,
                50.0, 65.0, 80.0, 100.0
            )

        return capacities.firstOrNull {
            it >= shortCircuitKA
        } ?: shortCircuitKA
    }
}

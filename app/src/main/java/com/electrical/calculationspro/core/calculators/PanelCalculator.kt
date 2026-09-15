package com.electrical.calculationspro.core.calculators

import kotlin.math.sqrt

data class PanelFeederInput(
    val name: String,
    val loadKw: Double,
    val powerFactor: Double = 0.90,
    val voltageV: Double = 400.0,
    val demandFactor: Double = 1.0,
    val cableCapacityA: Double = 0.0
)

data class PanelInput(
    val name: String,
    val voltageV: Double = 400.0,
    val feeders: List<PanelFeederInput> = emptyList(),
    val spareCapacityFactor: Double = 1.15
)

data class PanelFeederResult(
    val name: String,
    val loadKw: Double,
    val demandKw: Double,
    val apparentPowerKva: Double,
    val currentA: Double,
    val recommendedBreakerA: Double,
    val cableCapacityA: Double,
    val cableAdequate: Boolean
)

data class PanelResult(
    val name: String,
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val apparentPowerKva: Double,
    val designCurrentA: Double,
    val recommendedMainBreakerA: Double,
    val requiredBusbarCurrentA: Double,
    val feeders: List<PanelFeederResult>,
    val notes: List<String>
)

class PanelCalculator {

    private val standardBreakers =
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

    fun calculate(
        input: PanelInput
    ): PanelResult {

        validate(input)

        val feederResults =
            input.feeders.map { feeder ->

                val demandKw =
                    feeder.loadKw *
                        feeder.demandFactor

                val kva =
                    demandKw /
                        feeder.powerFactor

                val current =
                    kva *
                        1000.0 /
                        (
                            sqrt(3.0) *
                                feeder.voltageV
                            )

                val breaker =
                    nextBreaker(current)

                val cableAdequate =
                    feeder.cableCapacityA <= 0.0 ||
                        feeder.cableCapacityA >=
                        breaker

                PanelFeederResult(
                    name = feeder.name,
                    loadKw = feeder.loadKw,
                    demandKw = demandKw,
                    apparentPowerKva = kva,
                    currentA = current,
                    recommendedBreakerA = breaker,
                    cableCapacityA =
                        feeder.cableCapacityA,
                    cableAdequate =
                        cableAdequate
                )
            }

        val connected =
            input.feeders.sumOf {
                it.loadKw
            }

        val demand =
            feederResults.sumOf {
                it.demandKw
            }

        val kva =
            if (input.voltageV > 0.0) {
                feederResults.sumOf {
                    it.apparentPowerKva
                }
            } else {
                0.0
            }

        val designKva =
            kva *
                input.spareCapacityFactor

        val designCurrent =
            designKva *
                1000.0 /
                (
                    sqrt(3.0) *
                        input.voltageV
                    )

        val mainBreaker =
            nextBreaker(designCurrent)

        val requiredBusbar =
            designCurrent

        val warnings =
            feederResults
                .filterNot {
                    it.cableAdequate
                }
                .map {
                    "Feeder ${it.name}: cable capacity is below the selected breaker."
                }

        val notes =
            buildList {

                add(
                    "Panel = ${input.name}"
                )

                add(
                    "Connected load = %.2f kW"
                        .format(connected)
                )

                add(
                    "Demand load = %.2f kW"
                        .format(demand)
                )

                add(
                    "Calculated apparent power = %.2f kVA"
                        .format(kva)
                )

                add(
                    "Design apparent power = %.2f kVA"
                        .format(designKva)
                )

                add(
                    "Design current = %.2f A"
                        .format(designCurrent)
                )

                add(
                    "Recommended main breaker = %.0f A"
                        .format(mainBreaker)
                )

                add(
                    "Required busbar current = %.0f A"
                        .format(requiredBusbar)
                )

                addAll(warnings)
            }

        return PanelResult(
            name = input.name,
            connectedLoadKw = connected,
            demandLoadKw = demand,
            apparentPowerKva = kva,
            designCurrentA = designCurrent,
            recommendedMainBreakerA =
                mainBreaker,
            requiredBusbarCurrentA =
                requiredBusbar,
            feeders = feederResults,
            notes = notes
        )
    }

    private fun nextBreaker(
        currentA: Double
    ): Double {

        return standardBreakers.firstOrNull {
            it >= currentA
        } ?: standardBreakers.last()
    }

    private fun validate(
        input: PanelInput
    ) {

        require(input.name.isNotBlank()) {
            "Panel name is required."
        }

        require(input.voltageV > 0.0) {
            "Panel voltage must be greater than zero."
        }

        require(
            input.spareCapacityFactor >= 1.0
        ) {
            "Spare capacity factor must be at least 1.0."
        }

        input.feeders.forEach {

            require(it.name.isNotBlank()) {
                "Every feeder must have a name."
            }

            require(it.loadKw >= 0.0) {
                "Feeder load cannot be negative."
            }

            require(
                it.powerFactor > 0.0 &&
                    it.powerFactor <= 1.0
            ) {
                "Power factor must be between 0 and 1."
            }

            require(it.voltageV > 0.0) {
                "Feeder voltage must be greater than zero."
            }

            require(
                it.demandFactor >= 0.0 &&
                    it.demandFactor <= 1.0
            ) {
                "Demand factor must be between 0 and 1."
            }

            require(
                it.cableCapacityA >= 0.0
            ) {
                "Cable capacity cannot be negative."
            }
        }
    }
}

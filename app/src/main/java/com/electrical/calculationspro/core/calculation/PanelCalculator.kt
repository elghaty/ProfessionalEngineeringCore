package com.electrical.calculationspro.core.calculation

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

class PanelCalculator(
    private val breaker: BreakerCalculator = BreakerCalculator()
) {

    fun calculate(input: PanelInput): PanelResult {

        require(input.name.isNotBlank())
        require(input.voltageV > 0.0)
        require(input.spareCapacityFactor >= 1.0)

        input.feeders.forEach {
            require(it.name.isNotBlank())
            require(it.loadKw >= 0.0)
            require(it.powerFactor in 0.01..1.0)
            require(it.voltageV > 0.0)
            require(it.demandFactor in 0.0..1.0)
            require(it.cableCapacityA >= 0.0)
        }

        val feeders =
            input.feeders.map { feeder ->

                val demandKw =
                    feeder.loadKw *
                        feeder.demandFactor

                val kva =
                    demandKw /
                        feeder.powerFactor

                val current =
                    kva * 1000.0 /
                        (sqrt(3.0) * feeder.voltageV)

                val selectedBreaker =
                    breaker.selectRating(current)

                PanelFeederResult(
                    name = feeder.name,
                    loadKw = feeder.loadKw,
                    demandKw = demandKw,
                    apparentPowerKva = kva,
                    currentA = current,
                    recommendedBreakerA = selectedBreaker,
                    cableCapacityA = feeder.cableCapacityA,
                    cableAdequate =
                        feeder.cableCapacityA <= 0.0 ||
                            feeder.cableCapacityA >= selectedBreaker
                )
            }

        val connected =
            input.feeders.sumOf { it.loadKw }

        val demand =
            feeders.sumOf { it.demandKw }

        val kva =
            feeders.sumOf { it.apparentPowerKva }

        val designKva =
            kva * input.spareCapacityFactor

        val designCurrent =
            designKva * 1000.0 /
                (sqrt(3.0) * input.voltageV)

        val mainBreaker =
            breaker.selectRating(designCurrent)

        val warnings =
            feeders
                .filterNot { it.cableAdequate }
                .map {
                    "Feeder ${it.name}: cable capacity is below the selected breaker."
                }

        return PanelResult(
            name = input.name,
            connectedLoadKw = connected,
            demandLoadKw = demand,
            apparentPowerKva = kva,
            designCurrentA = designCurrent,
            recommendedMainBreakerA = mainBreaker,
            requiredBusbarCurrentA = designCurrent,
            feeders = feeders,
            notes =
                listOf(
                    "Panel = ${input.name}",
                    "Connected load = %.2f kW".format(connected),
                    "Demand load = %.2f kW".format(demand),
                    "Apparent power = %.2f kVA".format(kva),
                    "Design apparent power = %.2f kVA"
                        .format(designKva),
                    "Design current = %.2f A"
                        .format(designCurrent),
                    "Main breaker = %.0f A"
                        .format(mainBreaker)
                ) + warnings
        )
    }
}

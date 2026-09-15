package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.SldElementType
import com.electrical.calculationspro.core.model.SldNetwork

data class SldShortCircuitResult(
    val results: Map<String, ShortCircuitResult>,
    val maximumFaultCurrentKA: Double,
    val minimumFaultCurrentKA: Double,
    val maximumFaultLocationId: String?,
    val minimumFaultLocationId: String?
)

class SldShortCircuitCalculator(
    private val shortCircuitCalculator:
        ShortCircuitCalculator =
        ShortCircuitCalculator()
) {

    fun calculate(
        network: SldNetwork,
        sourceShortCircuitMva: Double = 1000.0,
        voltageFactor: Double = 1.05
    ): SldShortCircuitResult {

        require(network.elements.isNotEmpty())
        require(sourceShortCircuitMva > 0.0)
        require(voltageFactor > 0.0)

        val results =
            linkedMapOf<String, ShortCircuitResult>()

        network.elements.forEach { element ->

            if (element.voltageV <= 0.0) {
                return@forEach
            }

            val connection =
                network.connections.firstOrNull {
                    it.toId == element.id
                }

            val cableLength =
                if (connection != null) {
                    connection.lengthM
                } else {
                    element.cableLengthM
                }

            val cableResistance =
                if (connection != null) {
                    connection.cableResistanceOhmPerKm
                } else {
                    element.cableResistanceOhmPerKm
                }

            val cableReactance =
                if (connection != null) {
                    connection.cableReactanceOhmPerKm
                } else {
                    element.cableReactanceOhmPerKm
                }

            val parallelRuns =
                if (connection != null) {
                    connection.parallelRuns
                } else {
                    element.parallelRuns
                }

            val sourceMva =
                when {
                    element.sourceShortCircuitMva > 0.0 ->
                        element.sourceShortCircuitMva

                    else ->
                        sourceShortCircuitMva
                }

            val result =
                shortCircuitCalculator.calculate(
                    ShortCircuitInput(
                        voltageV =
                            element.voltageV,

                        sourceShortCircuitMva =
                            sourceMva,

                        sourceXOverR =
                            element.sourceXOverR,

                        transformerKva =
                            element.transformerKva,

                        transformerPercentZ =
                            element.transformerPercentZ,

                        cableLengthM =
                            cableLength,

                        cableResistanceOhmPerKm =
                            cableResistance,

                        cableReactanceOhmPerKm =
                            cableReactance,

                        parallelRuns =
                            parallelRuns,

                        voltageFactor =
                            voltageFactor
                    )
                )

            results[element.id] =
                result
        }

        val maximum =
            results.maxByOrNull {
                it.value.faultCurrentKA
            }

        val minimum =
            results
                .filter {
                    it.value.faultCurrentKA > 0.0
                }
                .minByOrNull {
                    it.value.faultCurrentKA
                }

        return SldShortCircuitResult(
            results = results,

            maximumFaultCurrentKA =
                maximum?.value?.faultCurrentKA
                    ?: 0.0,

            minimumFaultCurrentKA =
                minimum?.value?.faultCurrentKA
                    ?: 0.0,

            maximumFaultLocationId =
                maximum?.key,

            minimumFaultLocationId =
                minimum?.key
        )
    }
}

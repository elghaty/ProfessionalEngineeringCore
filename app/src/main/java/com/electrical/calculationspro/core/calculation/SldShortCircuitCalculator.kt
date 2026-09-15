package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.SldNetwork

data class SldShortCircuitResult(
    val nodeResults: Map<String, ShortCircuitResult>
)

class SldShortCircuitCalculator(
    private val shortCircuit: ShortCircuitCalculator =
        ShortCircuitCalculator()
) {

    fun calculate(
        network: SldNetwork,
        voltageFactor: Double = 1.05
    ): SldShortCircuitResult {

        require(network.elements.isNotEmpty()) {
            "SLD network is empty."
        }

        val results = linkedMapOf<String, ShortCircuitResult>()

        network.elements
            .filter { element ->
                element.voltageV > 0.0
            }
            .forEach { element ->

                val result =
                    shortCircuit.calculate(
                        ShortCircuitInput(
                            voltageV = element.voltageV,
                            sourceShortCircuitMva =
                                element.sourceShortCircuitMva,
                            transformerKva =
                                element.transformerKva,
                            transformerPercentZ =
                                element.transformerPercentZ,
                            cableLengthM =
                                element.cableLengthM,
                            cableResistanceOhmPerKm =
                                element.cableResistanceOhmPerKm,
                            cableReactanceOhmPerKm =
                                element.cableReactanceOhmPerKm,
                            parallelRuns =
                                element.parallelRuns.coerceAtLeast(1),
                            voltageFactor =
                                voltageFactor
                        )
                    )

                results[element.id] = result
            }

        return SldShortCircuitResult(
            nodeResults = results
        )
    }
}

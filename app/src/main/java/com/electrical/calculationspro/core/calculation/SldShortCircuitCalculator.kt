package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.SldNetwork

data class SldShortCircuitResult(
    val results: Map<String, ShortCircuitResult>
)

class SldShortCircuitCalculator(
    private val shortCircuitCalculator: ShortCircuitCalculator =
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

            val result =
                shortCircuitCalculator.calculate(
                    ShortCircuitInput(
                        voltageV = element.voltageV,
                        sourceShortCircuitMva =
                            sourceShortCircuitMva,
                        transformerKva =
                            element.transformerKva,
                        transformerPercentZ =
                            element.transformerPercentZ,
                        voltageFactor =
                            voltageFactor
                    )
                )

            results[element.id] = result
        }

        return SldShortCircuitResult(
            results = results
        )
    }
}

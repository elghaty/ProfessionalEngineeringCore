package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.SldConnection
import com.electrical.calculationspro.core.model.SldElement
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

        require(network.elements.isNotEmpty()) {
            "SLD network must contain elements."
        }

        require(sourceShortCircuitMva > 0.0) {
            "Source short-circuit level must be greater than zero."
        }

        require(voltageFactor > 0.0)

        val results =
            linkedMapOf<String, ShortCircuitResult>()

        network.elements.forEach { element ->

            val input =
                buildInputForElement(
                    network = network,
                    element = element,
                    sourceShortCircuitMva =
                        sourceShortCircuitMva,
                    voltageFactor =
                        voltageFactor
                )

            if (input != null) {

                results[element.id] =
                    shortCircuitCalculator.calculate(
                        input
                    )
            }
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
                maximum?.value?.faultCurrentKA ?: 0.0,
            minimumFaultCurrentKA =
                minimum?.value?.faultCurrentKA ?: 0.0,
            maximumFaultLocationId =
                maximum?.key,
            minimumFaultLocationId =
                minimum?.key
        )
    }

    private fun buildInputForElement(
        network: SldNetwork,
        element: SldElement,
        sourceShortCircuitMva: Double,
        voltageFactor: Double
    ): ShortCircuitInput? {

        if (element.voltageV <= 0.0) {
            return null
        }

        val path =
            upstreamConnections(
                network = network,
                elementId = element.id
            )

        val orderedPath =
            path.asReversed()

        val sourceElement =
            orderedPath
                .mapNotNull { connection ->
                    network.elements.firstOrNull {
                        it.id == connection.fromId
                    }
                }
                .firstOrNull {
                    it.sourceType != null
                }

        val effectiveSourceMva =
            when {
                element.sourceShortCircuitMva > 0.0 ->
                    element.sourceShortCircuitMva

                sourceElement?.sourceShortCircuitMva
                    ?: 0.0 > 0.0 ->
                    sourceElement.sourceShortCircuitMva

                else ->
                    sourceShortCircuitMva
            }

        val sourceXOverR =
            when {
                element.sourceXOverR > 0.0 &&
                    element.sourceShortCircuitMva > 0.0 ->
                    element.sourceXOverR

                sourceElement != null &&
                    sourceElement.sourceXOverR > 0.0 ->
                    sourceElement.sourceXOverR

                else ->
                    10.0
            }

        /*
         * Every upstream connection contributes its own
         * physical impedance.
         *
         * No artificial 1000 m equivalent feeder is used.
         */
        var cableR = 0.0
        var cableX = 0.0

        orderedPath.forEach { connection ->

            val runs =
                connection.parallelRuns
                    .coerceAtLeast(1)

            cableR +=
                connection.cableResistanceOhmPerKm *
                    connection.lengthM /
                    1000.0 /
                    runs

            cableX +=
                connection.cableReactanceOhmPerKm *
                    connection.lengthM /
                    1000.0 /
                    runs
        }

        /*
         * The target element transformer data are included
         * when the element itself represents a transformer.
         */
        return ShortCircuitInput(
            voltageV =
                element.voltageV,

            sourceShortCircuitMva =
                effectiveSourceMva,

            sourceXOverR =
                sourceXOverR,

            transformerKva =
                element.transformerKva,

            transformerPercentZ =
                element.transformerPercentZ,

            transformerXOverR =
                if (element.transformerXOverR > 0.0) {
                    element.transformerXOverR
                } else {
                    10.0
                },

            cableLengthM =
                if (cableR > 0.0 || cableX > 0.0) {
                    1000.0
                } else {
                    0.0
                },

            cableResistanceOhmPerKm =
                cableR,

            cableReactanceOhmPerKm =
                cableX,

            parallelRuns = 1,

            phase =
                element.phase,

            voltageFactor =
                voltageFactor
        )
    }

    private fun upstreamConnections(
        network: SldNetwork,
        elementId: String
    ): List<SldConnection> {

        val result =
            mutableListOf<SldConnection>()

        var currentId =
            elementId

        val visited =
            mutableSetOf<String>()

        while (visited.add(currentId)) {

            val connection =
                network.connections
                    .firstOrNull {
                        it.toId == currentId
                    }
                    ?: break

            result += connection

            currentId =
                connection.fromId
        }

        return result
    }
}

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

        require(network.elements.isNotEmpty())
        require(sourceShortCircuitMva > 0.0)
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

        val sourceElement =
            path
                .asSequence()
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

                sourceElement != null &&
                    sourceElement.sourceShortCircuitMva > 0.0 ->
                    sourceElement.sourceShortCircuitMva

                else ->
                    sourceShortCircuitMva
            }

        val sourceXOverR =
            when {
                element.sourceXOverR > 0.0 &&
                    element.sourceShortCircuitMva > 0.0 ->
                    element.sourceXOverR

                sourceElement != null ->
                    sourceElement.sourceXOverR

                else ->
                    10.0
            }

        val totalCableR =
            path.sumOf { connection ->

                val runs =
                    connection.parallelRuns
                        .coerceAtLeast(1)

                connection.cableResistanceOhmPerKm *
                    connection.lengthM /
                    1000.0 /
                    runs
            }

        val totalCableX =
            path.sumOf { connection ->

                val runs =
                    connection.parallelRuns
                        .coerceAtLeast(1)

                connection.cableReactanceOhmPerKm *
                    connection.lengthM /
                    1000.0 /
                    runs
            }

        /*
         * The complete upstream cable impedance is
         * represented as one equivalent feeder.
         *
         * The local feeder is NOT added again.
         */
        val equivalentLengthM =
            if (
                totalCableR > 0.0 ||
                totalCableX > 0.0
            ) {
                1000.0
            } else {
                0.0
            }

        val equivalentRPerKm =
            if (equivalentLengthM > 0.0) {
                totalCableR
            } else {
                0.0
            }

        val equivalentXPerKm =
            if (equivalentLengthM > 0.0) {
                totalCableX
            } else {
                0.0
            }

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
                10.0,

            cableLengthM =
                equivalentLengthM,

            cableResistanceOhmPerKm =
                equivalentRPerKm,

            cableReactanceOhmPerKm =
                equivalentXPerKm,

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
                network.connections.firstOrNull {
                    it.toId == currentId
                } ?: break

            result += connection

            currentId =
                connection.fromId
        }

        return result
    }
}

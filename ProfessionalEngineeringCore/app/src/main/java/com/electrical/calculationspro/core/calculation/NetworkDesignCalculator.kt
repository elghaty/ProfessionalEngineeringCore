package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.SldElementType
import com.electrical.calculationspro.core.model.SldNetwork
import com.electrical.calculationspro.core.model.SldResult
import kotlin.math.sqrt

/**
 * Performs engineering calculations for an electrical SLD network.
 *
 * Architectural rule:
 * - This class contains network calculation logic.
 * - ProfessionalEngineeringCore must only delegate to this class.
 * - No UI code belongs here.
 * - No drawing logic belongs here.
 */
class NetworkDesignCalculator(
    private val sldGenerator: SldGenerator,
    private val loadCalculator: LoadCalculator,
    private val sldShortCircuitCalculator: SldShortCircuitCalculator
) {

    /**
     * Calculates the complete engineering result of an SLD network.
     *
     * The calculation flow is:
     *
     * SLD network
     *      ↓
     * topology validation / build
     *      ↓
     * load calculations
     *      ↓
     * source current
     *      ↓
     * short-circuit calculations
     *      ↓
     * SldResult
     */
    fun calculate(
        network: SldNetwork,
        sourceFaultMva: Double = 1000.0,
        voltageFactor: Double = 1.05
    ): SldResult {

        require(sourceFaultMva >= 0.0) {
            "Source short-circuit MVA cannot be negative."
        }

        require(voltageFactor > 0.0) {
            "Voltage factor must be greater than zero."
        }

        /*
         * First allow the SLD generator to build/validate
         * the network representation.
         */
        val builtNetwork = sldGenerator.build(network)

        /*
         * Calculate short-circuit results for the complete
         * SLD network.
         */
        val shortCircuitResult =
            sldShortCircuitCalculator.calculate(
                network = builtNetwork,
                sourceShortCircuitMva = sourceFaultMva,
                voltageFactor = voltageFactor
            )

        /*
         * Calculate the electrical loads represented by
         * powered SLD elements.
         */
        val elementResults =
            builtNetwork.elements.map { element ->

                val quantity =
                    element.quantity.coerceAtLeast(1)

                val connectedPowerKw =
                    if (element.powerKw > 0.0) {
                        element.powerKw * quantity
                    } else {
                        0.0
                    }

                val demandFactor =
                    element.demandFactor
                        .coerceAtLeast(0.0)

                val diversityFactor =
                    element.diversityFactor
                        .coerceAtLeast(0.0)

                val efficiency =
                    element.efficiency
                        .coerceIn(0.01, 1.0)

                val powerFactor =
                    element.powerFactor
                        .coerceIn(0.01, 1.0)

                val isLoadElement =
                    element.type == SldElementType.LOAD ||
                        element.type == SldElementType.MOTOR ||
                        element.type == SldElementType.PUMP

                if (!isLoadElement || connectedPowerKw <= 0.0) {
                    return@map null
                }

                val load =
                    ElectricalLoad(
                        name = element.name,
                        powerKw = connectedPowerKw,
                        voltageV = element.voltageV,
                        powerFactor = powerFactor,
                        efficiency = efficiency,
                        demandFactor = demandFactor,
                        diversityFactor = diversityFactor,
                        phase = element.phase
                    )

                val result =
                    loadCalculator.calculate(load)

                NetworkElementCalculation(
                    elementId = element.id,
                    connectedKw = connectedPowerKw,
                    demandKw = result.demandPowerKw,
                    apparentPowerKva = result.apparentPowerKva,
                    currentA = result.currentA
                )
            }

        val validElementResults =
            elementResults.filterNotNull()

        val totalLoadKw =
            validElementResults.sumOf {
                it.connectedKw
            }

        val totalDemandKw =
            validElementResults.sumOf {
                it.demandKw
            }

        /*
         * Find the source element.
         */
        val source =
            builtNetwork.elements.firstOrNull {
                it.parentId == null ||
                    it.type == SldElementType.UTILITY ||
                    it.type == SldElementType.TRANSFORMER ||
                    it.type == SldElementType.GENERATOR
            }

        val sourceVoltage =
            source?.voltageV
                ?.takeIf { it > 0.0 }
                ?: builtNetwork.elements
                    .map { it.voltageV }
                    .firstOrNull { it > 0.0 }
                ?: 0.0

        /*
         * Determine a representative power factor
         * for the complete connected load.
         */
        val totalKva =
            validElementResults.sumOf {
                it.apparentPowerKva
            }

        val overallPowerFactor =
            if (totalKva > 0.0) {
                (totalLoadKw / totalKva)
                    .coerceIn(0.01, 1.0)
            } else {
                1.0
            }

        /*
         * Calculate the source current from the total
         * demand load.
         *
         * The SLD model currently represents the general
         * distribution network, therefore the source current
         * is calculated using the standard three-phase
         * relationship when a source voltage exists.
         */
        val sourceCurrentA =
            if (sourceVoltage > 0.0 && totalDemandKw > 0.0) {
                totalDemandKw * 1000.0 /
                    (
                        sqrt(3.0) *
                            sourceVoltage *
                            overallPowerFactor
                        )
            } else {
                0.0
            }

        /*
         * Map element-level short-circuit results back
         * to the public SLD result model.
         */
        val finalElements =
            builtNetwork.elements.map { element ->

                val loadResult =
                    validElementResults.firstOrNull {
                        it.elementId == element.id
                    }

                val shortCircuit =
                    shortCircuitResult.results
                        .firstOrNull {
                            it.elementId == element.id
                        }

                com.electrical.calculationspro.core.model.SldElementResult(
                    elementId = element.id,
                    powerKw =
                        loadResult?.demandKw
                            ?: 0.0,
                    apparentPowerKva =
                        loadResult?.apparentPowerKva
                            ?: 0.0,
                    currentA =
                        loadResult?.currentA
                            ?: 0.0,
                    shortCircuitKA =
                        shortCircuit?.shortCircuitKA
                            ?: 0.0
                )
            }

        val sourceFaultCurrentKA =
            shortCircuitResult.sourceFaultCurrentKA

        return SldResult(
            elements = finalElements,
            totalLoadKw = totalLoadKw,
            totalDemandKw = totalDemandKw,
            sourceCurrentA = sourceCurrentA,
            sourceFaultCurrentKA = sourceFaultCurrentKA
        )
    }

    private data class NetworkElementCalculation(
        val elementId: String,
        val connectedKw: Double,
        val demandKw: Double,
        val apparentPowerKva: Double,
        val currentA: Double
    )
}

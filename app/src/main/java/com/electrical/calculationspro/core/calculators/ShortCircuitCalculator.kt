package com.electrical.calculationspro.core.calculators

import kotlin.math.sqrt

data class ShortCircuitInput(
    val voltageV: Double = 400.0,
    val systemFrequencyHz: Double = 50.0,

    /**
     * Upstream source short-circuit level.
     *
     * If supplied, it is expressed in kA.
     */
    val sourceShortCircuitKA: Double = 0.0,

    /**
     * Alternative upstream source representation.
     *
     * Short-circuit power in MVA.
     */
    val sourceShortCircuitMVA: Double = 0.0,

    /**
     * Transformer rating.
     */
    val transformerKVA: Double = 0.0,

    /**
     * Transformer impedance in percent.
     */
    val transformerPercentZ: Double = 0.0,

    /**
     * Cable length in metres.
     */
    val cableLengthM: Double = 0.0,

    /**
     * Cable resistance in ohm/km.
     */
    val cableResistanceOhmPerKm: Double = 0.0,

    /**
     * Cable reactance in ohm/km.
     */
    val cableReactanceOhmPerKm: Double = 0.0,

    /**
     * Number of parallel cable runs.
     */
    val parallelRuns: Int = 1,

    /**
     * Fault type.
     *
     * Supported:
     * - THREE_PHASE
     * - LINE_TO_LINE
     * - LINE_TO_NEUTRAL
     */
    val faultType: FaultType = FaultType.THREE_PHASE
)

enum class FaultType {
    THREE_PHASE,
    LINE_TO_LINE,
    LINE_TO_NEUTRAL
}

data class ShortCircuitResult(
    val sourceShortCircuitKA: Double,
    val transformerShortCircuitKA: Double,
    val faultCurrentKA: Double,
    val sourceImpedanceOhm: Double,
    val transformerImpedanceOhm: Double,
    val cableResistanceOhm: Double,
    val cableReactanceOhm: Double,
    val totalResistanceOhm: Double,
    val totalReactanceOhm: Double,
    val totalImpedanceOhm: Double,
    val faultType: FaultType,
    val notes: List<String>
)

class ShortCircuitCalculator {

    private const val EPSILON = 1.0e-9

    fun calculate(
        input: ShortCircuitInput
    ): ShortCircuitResult {

        validate(input)

        val sourceFaultKA =
            when {
                input.sourceShortCircuitKA > EPSILON ->
                    input.sourceShortCircuitKA

                input.sourceShortCircuitMVA > EPSILON ->
                    sourceMvaToFaultCurrentKA(
                        input.sourceShortCircuitMVA,
                        input.voltageV
                    )

                else ->
                    0.0
            }

        val sourceImpedance =
            if (sourceFaultKA > EPSILON) {

                faultVoltage(
                    input.voltageV,
                    input.faultType
                ) /
                    (sourceFaultKA * 1000.0)

            } else {
                0.0
            }

        val transformerImpedance =
            if (
                input.transformerKVA > EPSILON &&
                input.transformerPercentZ > EPSILON
            ) {

                transformerImpedance(
                    input.voltageV,
                    input.transformerKVA,
                    input.transformerPercentZ
                )

            } else {
                0.0
            }

        val transformerFaultKA =
            if (
                input.transformerKVA > EPSILON &&
                input.transformerPercentZ > EPSILON
            ) {

                transformerFaultCurrentKA(
                    input.transformerKVA,
                    input.voltageV,
                    input.transformerPercentZ
                )

            } else {
                0.0
            }

        val runs =
            input.parallelRuns
                .coerceAtLeast(1)

        val lengthKm =
            input.cableLengthM / 1000.0

        val cableResistance =
            input.cableResistanceOhmPerKm *
                lengthKm /
                runs

        val cableReactance =
            input.cableReactanceOhmPerKm *
                lengthKm /
                runs

        val totalResistance =
            sourceImpedance +
                transformerImpedance +
                cableResistance

        val totalReactance =
            cableReactance

        val totalImpedance =
            sqrt(
                totalResistance *
                    totalResistance +
                    totalReactance *
                    totalReactance
            ).coerceAtLeast(EPSILON)

        val faultVoltage =
            faultVoltage(
                input.voltageV,
                input.faultType
            )

        val faultCurrentA =
            faultVoltage /
                totalImpedance

        val faultCurrentKA =
            faultCurrentA /
                1000.0

        return ShortCircuitResult(

            sourceShortCircuitKA =
                sourceFaultKA,

            transformerShortCircuitKA =
                transformerFaultKA,

            faultCurrentKA =
                faultCurrentKA,

            sourceImpedanceOhm =
                sourceImpedance,

            transformerImpedanceOhm =
                transformerImpedance,

            cableResistanceOhm =
                cableResistance,

            cableReactanceOhm =
                cableReactance,

            totalResistanceOhm =
                totalResistance,

            totalReactanceOhm =
                totalReactance,

            totalImpedanceOhm =
                totalImpedance,

            faultType =
                input.faultType,

            notes = listOf(

                "Fault type = ${input.faultType.name}",

                "Source fault level = %.2f kA"
                    .format(sourceFaultKA),

                "Transformer fault contribution = %.2f kA"
                    .format(transformerFaultKA),

                "Source impedance = %.6f Ω"
                    .format(sourceImpedance),

                "Transformer impedance = %.6f Ω"
                    .format(transformerImpedance),

                "Cable resistance = %.6f Ω"
                    .format(cableResistance),

                "Cable reactance = %.6f Ω"
                    .format(cableReactance),

                "Total impedance = %.6f Ω"
                    .format(totalImpedance),

                "Calculated fault current = %.3f kA"
                    .format(faultCurrentKA)
            )
        )
    }

    private fun sourceMvaToFaultCurrentKA(
        mva: Double,
        voltageV: Double
    ): Double {

        return mva *
            1000.0 /
            (
                sqrt(3.0) *
                    voltageV
            )
    }

    private fun transformerFaultCurrentKA(
        transformerKVA: Double,
        voltageV: Double,
        percentZ: Double
    ): Double {

        val ratedCurrentA =
            transformerKVA *
                1000.0 /
                (
                    sqrt(3.0) *
                        voltageV
                )

        return ratedCurrentA /
            (percentZ / 100.0) /
            1000.0
    }

    private fun transformerImpedance(
        voltageV: Double,
        transformerKVA: Double,
        percentZ: Double
    ): Double {

        val ratedCurrent =
            transformerKVA *
                1000.0 /
                (
                    sqrt(3.0) *
                        voltageV
                )

        val ratedImpedance =
            voltageV /
                (
                    sqrt(3.0) *
                        ratedCurrent
                )

        return ratedImpedance *
            percentZ /
            100.0
    }

    private fun faultVoltage(
        voltageV: Double,
        faultType: FaultType
    ): Double {

        return when (faultType) {

            FaultType.THREE_PHASE ->
                voltageV /
                    sqrt(3.0)

            FaultType.LINE_TO_LINE ->
                voltageV

            FaultType.LINE_TO_NEUTRAL ->
                voltageV /
                    sqrt(3.0)
        }
    }

    private fun validate(
        input: ShortCircuitInput
    ) {

        require(input.voltageV > EPSILON) {
            "System voltage must be greater than zero."
        }

        require(input.systemFrequencyHz > EPSILON) {
            "System frequency must be greater than zero."
        }

        require(
            input.sourceShortCircuitKA >= 0.0
        ) {
            "Source short-circuit current cannot be negative."
        }

        require(
            input.sourceShortCircuitMVA >= 0.0
        ) {
            "Source short-circuit MVA cannot be negative."
        }

        require(
            input.transformerKVA >= 0.0
        ) {
            "Transformer rating cannot be negative."
        }

        require(
            input.transformerPercentZ >= 0.0
        ) {
            "Transformer impedance cannot be negative."
        }

        require(
            input.cableLengthM >= 0.0
        ) {
            "Cable length cannot be negative."
        }

        require(
            input.cableResistanceOhmPerKm >= 0.0
        ) {
            "Cable resistance cannot be negative."
        }

        require(
            input.cableReactanceOhmPerKm >= 0.0
        ) {
            "Cable reactance cannot be negative."
        }

        require(
            input.parallelRuns >= 1
        ) {
            "Parallel cable runs must be at least one."
        }

        require(
            input.sourceShortCircuitKA > EPSILON ||
                input.sourceShortCircuitMVA > EPSILON ||
                (
                    input.transformerKVA > EPSILON &&
                        input.transformerPercentZ > EPSILON
                    )
        ) {
            throw IllegalArgumentException(
                "A valid upstream source or transformer short-circuit source is required."
            )
        }
    }
}

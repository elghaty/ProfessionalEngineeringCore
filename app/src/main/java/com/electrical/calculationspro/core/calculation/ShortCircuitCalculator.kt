package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.FaultType
import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.ShortCircuitResult
import kotlin.math.exp
import kotlin.math.sqrt

data class ShortCircuitInput(
    val voltageV: Double,

    val sourceShortCircuitMva: Double = 0.0,
    val sourceXOverR: Double = 10.0,

    val transformerKva: Double = 0.0,
    val transformerPercentZ: Double = 0.0,
    val transformerXOverR: Double = 10.0,

    val cableLengthM: Double = 0.0,
    val cableResistanceOhmPerKm: Double = 0.0,
    val cableReactanceOhmPerKm: Double = 0.0,
    val parallelRuns: Int = 1,

    val phase: Phase = Phase.THREE,
    val faultType: FaultType = FaultType.THREE_PHASE,

    val voltageFactor: Double = 1.0,

    val clearingTimeS: Double = 1.0,

    /*
     * Sequence impedances in ohms.
     *
     * If not supplied, the calculator uses the positive
     * sequence impedance as the fallback for positive/negative
     * sequence and zero sequence where no earthing data exist.
     */
    val sourcePositiveSequenceOhm: Double? = null,
    val sourceNegativeSequenceOhm: Double? = null,
    val sourceZeroSequenceOhm: Double? = null,

    val transformerPositiveSequenceOhm: Double? = null,
    val transformerNegativeSequenceOhm: Double? = null,
    val transformerZeroSequenceOhm: Double? = null,

    val cablePositiveSequenceOhm: Double? = null,
    val cableNegativeSequenceOhm: Double? = null,
    val cableZeroSequenceOhm: Double? = null
)

class ShortCircuitCalculator {

    fun calculate(
        input: ShortCircuitInput
    ): ShortCircuitResult {

        validate(input)

        val sourceZ =
            calculateSourceImpedance(input)

        val transformerZ =
            calculateTransformerImpedance(input)

        val cableR =
            input.cableResistanceOhmPerKm *
                input.cableLengthM /
                1000.0 /
                input.parallelRuns

        val cableX =
            input.cableReactanceOhmPerKm *
                input.cableLengthM /
                1000.0 /
                input.parallelRuns

        val sourceRx =
            splitImpedance(
                impedance = sourceZ,
                xOverR = input.sourceXOverR
            )

        val transformerRx =
            splitImpedance(
                impedance = transformerZ,
                xOverR = input.transformerXOverR
            )

        val positiveR =
            sourceRx.first +
                transformerRx.first +
                cableR

        val positiveX =
            sourceRx.second +
                transformerRx.second +
                cableX

        val positiveZ =
            magnitude(
                positiveR,
                positiveX
            )

        val negativeZ =
            input.sourceNegativeSequenceOhm
                ?: input.sourcePositiveSequenceOhm
                ?: positiveZ

        val zeroZ =
            input.sourceZeroSequenceOhm
                ?: input.transformerZeroSequenceOhm
                ?: input.cableZeroSequenceOhm
                ?: positiveZ

        val positiveSequence =
            input.sourcePositiveSequenceOhm
                ?: input.transformerPositiveSequenceOhm
                ?: input.cablePositiveSequenceOhm
                ?: positiveZ

        val negativeSequence =
            input.sourceNegativeSequenceOhm
                ?: input.transformerNegativeSequenceOhm
                ?: input.cableNegativeSequenceOhm
                ?: negativeZ

        val zeroSequence =
            zeroZ

        val faultCurrentA =
            calculateFaultCurrent(
                voltageV = input.voltageV,
                positiveSequenceOhm =
                    positiveSequence,
                negativeSequenceOhm =
                    negativeSequence,
                zeroSequenceOhm =
                    zeroSequence,
                faultType = input.faultType,
                voltageFactor = input.voltageFactor
            )

        val faultCurrentKA =
            faultCurrentA / 1000.0

        val xOverR =
            if (positiveR > 0.0) {
                positiveX / positiveR
            } else {
                Double.POSITIVE_INFINITY
            }

        val rOverX =
            if (positiveX > 0.0) {
                positiveR / positiveX
            } else {
                Double.POSITIVE_INFINITY
            }

        val kappa =
            calculateKappa(rOverX)

        val peakFaultCurrentKA =
            faultCurrentKA *
                sqrt(2.0) *
                kappa

        val faultMva =
            calculateFaultMva(
                voltageV = input.voltageV,
                currentKA = faultCurrentKA,
                faultType = input.faultType
            )

        val thermalI2tKa2s =
            faultCurrentKA *
                faultCurrentKA *
                input.clearingTimeS

        return ShortCircuitResult(
            sourceImpedanceOhm = sourceZ,
            transformerImpedanceOhm = transformerZ,
            cableResistanceOhm = cableR,
            cableReactanceOhm = cableX,
            totalResistanceOhm = positiveR,
            totalReactanceOhm = positiveX,
            totalImpedanceOhm = positiveZ,
            faultCurrentKA = faultCurrentKA,
            faultMva = faultMva,
            peakFaultCurrentKA = peakFaultCurrentKA,
            thermalI2tKa2s = thermalI2tKa2s,
            kappa = kappa,
            xOverR = xOverR,
            rOverX = rOverX
        )
    }

    fun fromSourceFaultLevel(
        voltageV: Double,
        sourceShortCircuitMva: Double,
        cableLengthM: Double = 0.0,
        cableResistanceOhmPerKm: Double = 0.0,
        cableReactanceOhmPerKm: Double = 0.0,
        parallelRuns: Int = 1,
        voltageFactor: Double = 1.0,
        clearingTimeS: Double = 1.0
    ): ShortCircuitResult {

        return calculate(
            ShortCircuitInput(
                voltageV = voltageV,
                sourceShortCircuitMva =
                    sourceShortCircuitMva,
                cableLengthM = cableLengthM,
                cableResistanceOhmPerKm =
                    cableResistanceOhmPerKm,
                cableReactanceOhmPerKm =
                    cableReactanceOhmPerKm,
                parallelRuns = parallelRuns,
                voltageFactor = voltageFactor,
                clearingTimeS = clearingTimeS
            )
        )
    }

    private fun validate(
        input: ShortCircuitInput
    ) {

        require(input.voltageV > 0.0)

        require(input.sourceShortCircuitMva >= 0.0)
        require(input.sourceXOverR >= 0.0)

        require(input.transformerKva >= 0.0)
        require(input.transformerPercentZ >= 0.0)
        require(input.transformerXOverR >= 0.0)

        require(input.cableLengthM >= 0.0)
        require(input.cableResistanceOhmPerKm >= 0.0)
        require(input.cableReactanceOhmPerKm >= 0.0)

        require(input.parallelRuns > 0)
        require(input.voltageFactor > 0.0)
        require(input.clearingTimeS > 0.0)
    }

    private fun calculateSourceImpedance(
        input: ShortCircuitInput
    ): Double {

        if (input.sourceShortCircuitMva <= 0.0) {
            return 0.0
        }

        return input.voltageV *
            input.voltageV /
            (
                input.sourceShortCircuitMva *
                    1_000_000.0
                )
    }

    private fun calculateTransformerImpedance(
        input: ShortCircuitInput
    ): Double {

        if (
            input.transformerKva <= 0.0 ||
            input.transformerPercentZ <= 0.0
        ) {
            return 0.0
        }

        return input.voltageV *
            input.voltageV /
            (
                input.transformerKva *
                    1000.0
                ) *
            (
                input.transformerPercentZ /
                    100.0
                )
    }

    private fun splitImpedance(
        impedance: Double,
        xOverR: Double
    ): Pair<Double, Double> {

        if (impedance <= 0.0) {
            return 0.0 to 0.0
        }

        if (xOverR <= 0.0) {
            return impedance to 0.0
        }

        val r =
            impedance /
                sqrt(
                    1.0 +
                        xOverR *
                        xOverR
                )

        val x =
            r * xOverR

        return r to x
    }

    private fun magnitude(
        resistance: Double,
        reactance: Double
    ): Double {

        return sqrt(
            resistance * resistance +
                reactance * reactance
        )
    }

    private fun calculateFaultCurrent(
        voltageV: Double,
        positiveSequenceOhm: Double,
        negativeSequenceOhm: Double,
        zeroSequenceOhm: Double,
        faultType: FaultType,
        voltageFactor: Double
    ): Double {

        val phaseVoltage =
            voltageV / sqrt(3.0)

        val denominator =
            when (faultType) {

                FaultType.THREE_PHASE ->
                    sqrt(3.0) *
                        positiveSequenceOhm

                FaultType.LINE_TO_LINE ->
                    negativeSequenceOhm +
                        positiveSequenceOhm

                FaultType.LINE_TO_NEUTRAL ->
                    positiveSequenceOhm +
                        negativeSequenceOhm +
                        zeroSequenceOhm
            }

        if (denominator <= 0.0) {
            return 0.0
        }

        return when (faultType) {

            FaultType.THREE_PHASE ->
                voltageFactor *
                    voltageV /
                    denominator

            FaultType.LINE_TO_LINE ->
                voltageFactor *
                    voltageV /
                    denominator

            FaultType.LINE_TO_NEUTRAL ->
                voltageFactor *
                    3.0 *
                    phaseVoltage /
                    denominator
        }
    }

    private fun calculateKappa(
        rOverX: Double
    ): Double {

        if (!rOverX.isFinite()) {
            return 1.02
        }

        return (
            1.02 +
                0.98 *
                exp(
                    -3.0 *
                        rOverX
                )
            ).coerceIn(1.02, 2.0)
    }

    private fun calculateFaultMva(
        voltageV: Double,
        currentKA: Double,
        faultType: FaultType
    ): Double {

        return when (faultType) {

            FaultType.THREE_PHASE ->
                sqrt(3.0) *
                    voltageV *
                    currentKA /
                    1000.0

            FaultType.LINE_TO_LINE ->
                voltageV *
                    currentKA /
                    1000.0

            FaultType.LINE_TO_NEUTRAL ->
                voltageV *
                    currentKA /
                    1000.0
        }
    }
}

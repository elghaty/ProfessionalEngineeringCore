package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.Phase
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
    val voltageFactor: Double = 1.0
)

enum class FaultType {
    THREE_PHASE,
    LINE_TO_LINE,
    LINE_TO_NEUTRAL
}

data class ShortCircuitResult(
    val sourceImpedanceOhm: Double,
    val transformerImpedanceOhm: Double,
    val cableResistanceOhm: Double,
    val cableReactanceOhm: Double,
    val totalResistanceOhm: Double,
    val totalReactanceOhm: Double,
    val totalImpedanceOhm: Double,
    val faultCurrentKA: Double,
    val faultMva: Double,
    val peakFaultCurrentKA: Double,
    val thermalI2tKa2s: Double,
    val kappa: Double,
    val xOverR: Double,
    val rOverX: Double
)

class ShortCircuitCalculator {

    fun calculate(input: ShortCircuitInput): ShortCircuitResult {
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

        val sourceZ =
            if (input.sourceShortCircuitMva > 0.0) {
                input.voltageV * input.voltageV /
                    (input.sourceShortCircuitMva * 1_000_000.0)
            } else {
                0.0
            }

        val transformerZ =
            if (
                input.transformerKva > 0.0 &&
                input.transformerPercentZ > 0.0
            ) {
                input.voltageV * input.voltageV /
                    (input.transformerKva * 1_000.0) *
                    (input.transformerPercentZ / 100.0)
            } else {
                0.0
            }

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

        val sourceR =
            if (sourceZ > 0.0 && input.sourceXOverR > 0.0) {
                sourceZ /
                    sqrt(
                        1.0 +
                            input.sourceXOverR *
                            input.sourceXOverR
                    )
            } else {
                if (sourceZ > 0.0) sourceZ else 0.0
            }

        val sourceX =
            if (sourceZ > 0.0) {
                if (input.sourceXOverR > 0.0) {
                    sourceR * input.sourceXOverR
                } else {
                    0.0
                }
            } else {
                0.0
            }

        val transformerR =
            if (transformerZ > 0.0 && input.transformerXOverR > 0.0) {
                transformerZ /
                    sqrt(
                        1.0 +
                            input.transformerXOverR *
                            input.transformerXOverR
                    )
            } else {
                if (transformerZ > 0.0) transformerZ else 0.0
            }

        val transformerX =
            if (transformerZ > 0.0) {
                if (input.transformerXOverR > 0.0) {
                    transformerR * input.transformerXOverR
                } else {
                    0.0
                }
            } else {
                0.0
            }

        val totalR =
            sourceR +
                transformerR +
                cableR

        val totalX =
            sourceX +
                transformerX +
                cableX

        val totalZ =
            sqrt(
                totalR * totalR +
                    totalX * totalX
            )

        val phaseVoltage =
            when (input.faultType) {
                FaultType.THREE_PHASE ->
                    input.voltageV / sqrt(3.0)

                FaultType.LINE_TO_LINE ->
                    input.voltageV

                FaultType.LINE_TO_NEUTRAL ->
                    input.voltageV / sqrt(3.0)
            }

        val baseCurrentA =
            if (totalZ > 0.0) {
                phaseVoltage / totalZ
            } else {
                0.0
            }

        val faultCurrentKA =
            when (input.faultType) {
                FaultType.THREE_PHASE ->
                    baseCurrentA *
                        input.voltageFactor /
                        1000.0

                FaultType.LINE_TO_LINE ->
                    baseCurrentA *
                        sqrt(3.0) *
                        input.voltageFactor /
                        1000.0

                FaultType.LINE_TO_NEUTRAL ->
                    baseCurrentA *
                        input.voltageFactor /
                        1000.0
            }

        val xOverR =
            if (totalR > 0.0) {
                totalX / totalR
            } else {
                Double.POSITIVE_INFINITY
            }

        val rOverX =
            if (totalX > 0.0) {
                totalR / totalX
            } else {
                Double.POSITIVE_INFINITY
            }

        val kappa =
            if (rOverX.isFinite()) {
                1.02 +
                    0.98 *
                    exp(-3.0 * rOverX)
            } else {
                1.02
            }

        val peakFaultCurrentKA =
            faultCurrentKA *
                sqrt(2.0) *
                kappa

        val thermalI2tKa2s =
            faultCurrentKA *
                faultCurrentKA

        val faultMva =
            when (input.faultType) {
                FaultType.THREE_PHASE ->
                    sqrt(3.0) *
                        input.voltageV *
                        faultCurrentKA /
                        1000.0

                FaultType.LINE_TO_LINE ->
                    input.voltageV *
                        faultCurrentKA /
                        1000.0

                FaultType.LINE_TO_NEUTRAL ->
                    input.voltageV *
                        faultCurrentKA /
                        1000.0
            }

        return ShortCircuitResult(
            sourceImpedanceOhm = sourceZ,
            transformerImpedanceOhm = transformerZ,
            cableResistanceOhm = cableR,
            cableReactanceOhm = cableX,
            totalResistanceOhm = totalR,
            totalReactanceOhm = totalX,
            totalImpedanceOhm = totalZ,
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
        voltageFactor: Double = 1.0
    ): ShortCircuitResult {
        return calculate(
            ShortCircuitInput(
                voltageV = voltageV,
                sourceShortCircuitMva = sourceShortCircuitMva,
                cableLengthM = cableLengthM,
                cableResistanceOhmPerKm = cableResistanceOhmPerKm,
                cableReactanceOhmPerKm = cableReactanceOhmPerKm,
                parallelRuns = parallelRuns,
                voltageFactor = voltageFactor
            )
        )
    }
}

package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.ShortCircuitResult
import kotlin.math.sqrt

class ShortCircuitCalculator {

    fun fromSourceFaultLevel(
        voltageV: Double,
        sourceFaultMva: Double,
        sourceXR: Double = 10.0,
        downstreamResistanceOhm: Double = 0.0,
        downstreamReactanceOhm: Double = 0.0
    ): ShortCircuitResult {

        require(voltageV > 0.0)
        require(sourceFaultMva > 0.0)
        require(sourceXR >= 0.0)
        require(downstreamResistanceOhm >= 0.0)
        require(downstreamReactanceOhm >= 0.0)

        val sourceZ =
            voltageV * voltageV /
                (sourceFaultMva * 1_000_000.0)

        val sourceX =
            if (sourceXR == 0.0) {
                sourceZ
            } else {
                sourceZ *
                    sourceXR /
                    sqrt(1.0 + sourceXR * sourceXR)
            }

        val sourceR =
            if (sourceXR == 0.0) {
                0.0
            } else {
                sourceX / sourceXR
            }

        val totalR =
            sourceR + downstreamResistanceOhm

        val totalX =
            sourceX + downstreamReactanceOhm

        val totalZ =
            sqrt(
                totalR * totalR +
                    totalX * totalX
            ).coerceAtLeast(1.0e-12)

        val ikA =
            voltageV /
                (sqrt(3.0) * totalZ)

        val ikKA =
            ikA / 1000.0

        val xr =
            if (totalR == 0.0) {
                1000.0
            } else {
                totalX / totalR
            }

        val kappa =
            1.02 +
                0.98 *
                kotlin.math.exp(
                    -3.0 / xr.coerceAtLeast(0.01)
                )

        val peakKA =
            sqrt(2.0) *
                kappa *
                ikKA

        val thermalKA =
            ikKA

        val faultMva =
            sqrt(3.0) *
                voltageV *
                ikA /
                1_000_000.0

        val i2t =
            ikA * ikA * 0.1

        return ShortCircuitResult(
            initialSymmetricalCurrentKA = ikKA,
            peakCurrentKA = peakKA,
            thermalCurrentKA = thermalKA,
            faultMva = faultMva,
            impedanceOhm = totalZ,
            kappa = kappa,
            i2t = i2t
        )
    }

    fun fromTransformer(
        voltageV: Double,
        transformerKva: Double,
        impedancePercent: Double,
        downstreamResistanceOhm: Double = 0.0,
        downstreamReactanceOhm: Double = 0.0
    ): ShortCircuitResult {

        require(voltageV > 0.0)
        require(transformerKva > 0.0)
        require(impedancePercent > 0.0)

        val transformerZ =
            voltageV * voltageV /
                (transformerKva * 1000.0) *
                impedancePercent / 100.0

        return fromSourceFaultLevel(
            voltageV = voltageV,
            sourceFaultMva =
                voltageV * voltageV /
                    transformerZ /
                    1_000_000.0,
            downstreamResistanceOhm =
                downstreamResistanceOhm,
            downstreamReactanceOhm =
                downstreamReactanceOhm
        )
    }
}

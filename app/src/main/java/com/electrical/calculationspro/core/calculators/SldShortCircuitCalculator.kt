package com.electrical.calculationspro.core.calculators

import com.electrical.calculationspro.data.SldNetwork
import com.electrical.calculationspro.data.SldShortCircuitEngine
import com.electrical.calculationspro.data.SldShortCircuitStudy

/**
 * SLD short-circuit calculation module.
 *
 * This class is the engineering-core boundary for SLD
 * short-circuit calculations.
 *
 * UI must not call SldShortCircuitEngine directly.
 */
class SldShortCircuitCalculator {

    fun calculate(
        network: SldNetwork,
        voltageFactor: Double = 1.05
    ): SldShortCircuitStudy {

        require(network.nodes.isNotEmpty()) {
            "SLD network is empty."
        }

        return SldShortCircuitEngine.calculate(
            network = network,
            voltageFactor = voltageFactor
        )
    }
}

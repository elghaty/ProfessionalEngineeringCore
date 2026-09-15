package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.calculators.DiversityLoad
import com.electrical.calculationspro.core.calculators.DiversityResult
import com.electrical.calculationspro.core.calculators.FaultType
import com.electrical.calculationspro.core.calculators.ShortCircuitInput
import com.electrical.calculationspro.core.calculators.ShortCircuitResult
import com.electrical.calculationspro.data.SldConnection
import com.electrical.calculationspro.data.SldNetwork
import com.electrical.calculationspro.data.SldNode
import com.electrical.calculationspro.data.SldShortCircuitStudy

/**
 * Single public engineering gateway.
 *
 * UI communicates with the engineering system through this facade.
 */
class ProfessionalEngineeringFacade(
    private val core: ProfessionalEngineeringCore =
        ProfessionalEngineeringCore()
) {

    fun calculateDiversity(
        loads: List<DiversityLoad>,
        diversityFactor: Double = 1.0
    ): DiversityResult {

        return core.diversity.calculate(
            loads = loads,
            additionalDiversityFactor = diversityFactor
        )
    }

    fun calculateShortCircuit(
        voltageV: Double,
        sourceShortCircuitKA: Double = 0.0,
        sourceShortCircuitMVA: Double = 0.0,
        transformerKVA: Double = 0.0,
        transformerPercentZ: Double = 0.0,
        cableLengthM: Double = 0.0,
        cableResistanceOhmPerKm: Double = 0.0,
        cableReactanceOhmPerKm: Double = 0.0,
        parallelRuns: Int = 1,
        faultType: FaultType =
            FaultType.THREE_PHASE
    ): ShortCircuitResult {

        return core.shortCircuit.calculate(
            ShortCircuitInput(
                voltageV = voltageV,
                sourceShortCircuitKA = sourceShortCircuitKA,
                sourceShortCircuitMVA = sourceShortCircuitMVA,
                transformerKVA = transformerKVA,
                transformerPercentZ = transformerPercentZ,
                cableLengthM = cableLengthM,
                cableResistanceOhmPerKm =
                    cableResistanceOhmPerKm,
                cableReactanceOhmPerKm =
                    cableReactanceOhmPerKm,
                parallelRuns = parallelRuns,
                faultType = faultType
            )
        )
    }

    fun calculateSldShortCircuit(
        network: SldNetwork,
        voltageFactor: Double = 1.05
    ): SldShortCircuitStudy {

        return core.sldShortCircuit.calculate(
            network = network,
            voltageFactor = voltageFactor
        )
    }

    fun createSld(
        nodes: List<SldNode>,
        connections: List<SldConnection>
    ): SldNetwork {

        return core.sld.build(
            nodes = nodes,
            connections = connections
        )
    }

    fun getCore(): ProfessionalEngineeringCore {
        return core
    }
}

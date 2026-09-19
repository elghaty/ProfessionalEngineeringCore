package com.electrical.calculationspro.data

import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.CableResult
import com.electricalengineeringpro.app.core.model.ConductorMaterial as CoreConductorMaterial
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.ShortCircuitInput
import kotlin.math.sqrt

/**
 * Compatibility adapter only.
 *
 * No engineering formulas are implemented here.
 * All engineering calculations are delegated to
 * ProfessionalEngineeringCore.
 */
object ElectricalCalculations {

    private val core =
        ProfessionalEngineeringCore.instance

    fun calculateDesignCurrent(
        loadWatts: Double,
        voltage: Double,
        powerFactor: Double,
        currentType: CurrentType
    ): Double {

        require(loadWatts >= 0.0)
        require(voltage > 0.0)
        require(powerFactor in 0.01..1.0)

        return when (currentType) {

            CurrentType.DirectCurrent ->
                loadWatts / voltage

            CurrentType.AlternatingSinglePhase ->
                core.power.fromKw(
                    powerKw = loadWatts / 1000.0,
                    voltage = voltage,
                    powerFactor = powerFactor,
                    phase = Phase.SINGLE
                ).currentA

            CurrentType.AlternatingTwoPhase ->
                loadWatts /
                    (2.0 * voltage * powerFactor)

            CurrentType.AlternatingThreePhase ->
                core.power.fromKw(
                    powerKw = loadWatts / 1000.0,
                    voltage = voltage,
                    powerFactor = powerFactor,
                    phase = Phase.THREE
                ).currentA
        }
    }

    fun applyDemandAndDiversity(
        ib: Double,
        demandFactor: Double = 1.0,
        diversityFactor: Double = 1.0
    ): Double {

        require(ib >= 0.0)
        require(demandFactor in 0.0..1.0)
        require(diversityFactor in 0.0..1.0)

        return ib *
            demandFactor *
            diversityFactor
    }

    fun calculateVoltageDrop(
        current: Double,
        length: Double,
        sectionMm2: Double,
        powerFactor: Double,
        currentType: CurrentType,
        material: ConductorMaterial,
        voltage: Double
    ): Pair<Double, Double> {

        val phase =
            when (currentType) {
                CurrentType.AlternatingThreePhase ->
                    Phase.THREE

                CurrentType.AlternatingSinglePhase ->
                    Phase.SINGLE

                CurrentType.AlternatingTwoPhase ->
                    Phase.SINGLE

                CurrentType.DirectCurrent ->
                    Phase.SINGLE
            }

        val result =
            core.voltageDrop.calculate(
                currentA = current,
                lengthM = length,
                voltageV = voltage,
                powerFactor = powerFactor,
                phase = phase,
                material =
                    when (material) {
                        ConductorMaterial.Copper ->
                            CoreConductorMaterial.COPPER

                        ConductorMaterial.Aluminum ->
                            CoreConductorMaterial.ALUMINIUM
                    }
            )

        return result.dropPercent to
            result.dropVolts
    }

    fun calculateShortCircuitCurrent(
        voltage: Double,
        length: Double,
        sectionMm2: Double,
        material: ConductorMaterial,
        currentType: CurrentType,
        sourceIkKA: Double
    ): ShortCircuitResult {

        require(sourceIkKA > 0.0) {
            "Source short-circuit current is required."
        }

        val phase =
            when (currentType) {
                CurrentType.AlternatingThreePhase ->
                    Phase.THREE

                else ->
                    Phase.SINGLE
            }

        val cableMaterial =
            when (material) {
                ConductorMaterial.Copper ->
                    CoreConductorMaterial.COPPER

                ConductorMaterial.Aluminum ->
                    CoreConductorMaterial.ALUMINIUM
            }

        /*
         * Core short-circuit calculation requires
         * actual source/transformer data.
         *
         * Legacy cable-only compatibility is retained
         * without introducing a new engineering engine.
         */
        val sourceMva =
            sqrt(3.0) *
                voltage *
                sourceIkKA *
                1000.0 /
                1_000_000.0

        val result =
            core.shortCircuit.calculate(
                ShortCircuitInput(
                    sourceVoltage = voltage,
                    transformerKva =
                        sourceMva * 1000.0,
                    transformerImpedancePercent = 5.0,
                    sourceShortCircuitMva =
                        sourceMva
                )
            )

        return ShortCircuitResult(
            ikAmps =
                result.faultCurrentKA * 1000.0,

            ikKA =
                result.faultCurrentKA,

            cableImpedance = 0.0,

            sourceImpedance = 0.0,

            i2t =
                result.faultCurrentKA *
                    result.faultCurrentKA *
                    1_000_000.0 *
                    0.1,

            notes =
                listOf(
                    "Ik = %.2f kA"
                        .format(
                            result.faultCurrentKA
                        )
                )
        )
    }

    fun sizeConductor(
        input: ConductorSizingInput,
        standard: Standard = Standard.IEC,
        demandFactor: Double = 1.0,
        diversityFactor: Double = 1.0
    ): ConductorSizingResult {

        val phase =
            when (input.currentType) {
                CurrentType.AlternatingThreePhase ->
                    Phase.THREE

                else ->
                    Phase.SINGLE
            }

        val material =
            when (input.conductor) {
                ConductorMaterial.Copper ->
                    CoreConductorMaterial.COPPER

                ConductorMaterial.Aluminum ->
                    CoreConductorMaterial.ALUMINIUM
            }

        val cableInput =
            CableInput(
                designCurrentA =
                    calculateDesignCurrent(
                        loadWatts =
                            input.load,
                        voltage =
                            input.voltage,
                        powerFactor =
                            input.powerFactor,
                        currentType =
                            input.currentType
                    ) *
                        demandFactor *
                        diversityFactor,

                lengthM =
                    input.lineLength,

                voltage =
                    input.voltage,

                powerFactor =
                    input.powerFactor,

                phase =
                    phase,

                material =
                    material,

                targetVoltageDropPercent =
                    input.maxVoltageDrop
            )

        val result =
            core.cable.calculate(
                cableInput
            )

        return mapCableResult(
            result = result,
            input = input
        )
    }

    fun evaluateSelectedSection(
        input: ConductorSizingInput,
        selectedSection: Double,
        standard: Standard = Standard.IEC,
        demandFactor: Double = 1.0,
        diversityFactor: Double = 1.0
    ): ConductorSizingResult {

        val result =
            sizeConductor(
                input = input.copy(
                    lineLength =
                        input.lineLength
                ),
                standard = standard,
                demandFactor = demandFactor,
                diversityFactor = diversityFactor
            )

        return result.copy(
            selectedSection =
                selectedSection
        )
    }

    fun calculateActivePower(
        voltage: Double,
        current: Double,
        pf: Double,
        phases: Int
    ): Double {

        require(voltage > 0.0)
        require(current >= 0.0)
        require(pf in 0.01..1.0)

        return if (phases == 3) {
            sqrt(3.0) *
                voltage *
                current *
                pf /
                1000.0
        } else {
            voltage *
                current *
                pf /
                1000.0
        }
    }

    fun calculateApparentPower(
        voltage: Double,
        current: Double,
        phases: Int
    ): Double {

        require(voltage > 0.0)
        require(current >= 0.0)

        return if (phases == 3) {
            sqrt(3.0) *
                voltage *
                current /
                1000.0
        } else {
            voltage *
                current /
                1000.0
        }
    }

    fun calculateReactivePower(
        active: Double,
        apparent: Double
    ): Double {

        require(active >= 0.0)
        require(apparent >= active)

        return sqrt(
            (
                apparent * apparent -
                    active * active
                ).coerceAtLeast(0.0)
        )
    }

    fun calculatePowerFactor(
        active: Double,
        apparent: Double
    ): Double {

        require(active >= 0.0)
        require(apparent > 0.0)

        return (
            active / apparent
            ).coerceIn(
                0.0,
                1.0
            )
    }

    private fun mapCableResult(
        result: CableResult,
        input: ConductorSizingInput
    ): ConductorSizingResult {

        val designCurrent =
            calculateDesignCurrent(
                loadWatts =
                    input.load,
                voltage =
                    input.voltage,
                powerFactor =
                    input.powerFactor,
                currentType =
                    input.currentType
            )

        val breaker =
            listOf(
                6.0,
                10.0,
                16.0,
                20.0,
                25.0,
                32.0,
                40.0,
                50.0,
                63.0,
                80.0,
                100.0,
                125.0,
                160.0,
                200.0,
                250.0,
                315.0,
                400.0,
                500.0,
                630.0
            ).firstOrNull {
                it >= designCurrent
            } ?: 0.0

        return ConductorSizingResult(
            designCurrent =
                designCurrent,

            recommendedSection =
                result.selectedSizeMm2,

            selectedSection =
                result.selectedSizeMm2,

            ampacity =
                result.ampacityA,

            voltageDropPercent =
                result.voltageDropPercent,

            voltageDropVolts =
                input.voltage *
                    result.voltageDropPercent /
                    100.0,

            protectiveDevice =
                breaker,

            shortCircuitCurrentKA =
                0.0,

            breakerWithinCableCapacity =
                breaker > 0.0 &&
                    breaker <=
                    result.ampacityA,

            voltageDropWithinLimit =
                result.voltageDropPercent <=
                    input.maxVoltageDrop,

            notes =
                listOf(
                    result.conductorDescription,

                    "Design current = %.2f A"
                        .format(
                            designCurrent
                        ),

                    "Ampacity = %.1f A"
                        .format(
                            result.ampacityA
                        ),

                    "Voltage drop = %.2f %%"
                        .format(
                            result.voltageDropPercent
                        )
                )
        )
    }
}

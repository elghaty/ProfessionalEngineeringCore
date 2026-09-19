package com.electrical.calculationspro.data

import android.content.Context
import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.ConductorMaterial as CoreConductorMaterial
import com.electricalengineeringpro.app.core.model.CableInsulation
import com.electricalengineeringpro.app.core.model.Phase

object ElectricalCalculations {

    private val core =
        ProfessionalEngineeringCore.instance

    private fun phase(
        currentType: CurrentType
    ): Phase =
        when (currentType) {
            CurrentType.AlternatingThreePhase ->
                Phase.THREE

            CurrentType.AlternatingSinglePhase,
            CurrentType.AlternatingTwoPhase,
            CurrentType.DirectCurrent ->
                Phase.SINGLE
        }

    private fun material(
        value: ConductorMaterial
    ): CoreConductorMaterial =
        when (value) {
            ConductorMaterial.Copper ->
                CoreConductorMaterial.COPPER

            ConductorMaterial.Aluminum ->
                CoreConductorMaterial.ALUMINIUM
        }

    private fun insulation(
        value: InsulationType
    ): CableInsulation =
        when (value) {
            InsulationType.PVC ->
                CableInsulation.PVC

            InsulationType.XLPE ->
                CableInsulation.XLPE

            InsulationType.EPR ->
                CableInsulation.EPR

            InsulationType.Rubber ->
                CableInsulation.PVC
        }

    fun calculateDesignCurrent(
        loadWatts: Double,
        voltage: Double,
        powerFactor: Double,
        currentType: CurrentType
    ): Double {

        require(loadWatts >= 0.0)
        require(voltage > 0.0)
        require(powerFactor in 0.01..1.0)

        return core.power
            .fromKw(
                powerKw = loadWatts / 1000.0,
                voltage = voltage,
                powerFactor = powerFactor,
                phase = phase(currentType)
            )
            .currentA
    }

    fun applyDemandAndDiversity(
        ib: Double,
        demandFactor: Double = 1.0,
        diversityFactor: Double = 1.0
    ): Double {

        require(ib >= 0.0)
        require(demandFactor in 0.0..1.0)
        require(diversityFactor > 0.0)

        return core.loads
            .applyFactors(
                connectedPowerKw = ib,
                demandFactor = demandFactor,
                diversityFactor = diversityFactor
            )
            .demandKw
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

        require(current >= 0.0)
        require(length >= 0.0)
        require(sectionMm2 > 0.0)
        require(voltage > 0.0)
        require(powerFactor in 0.01..1.0)

        val result =
            core.voltageDrop.calculate(
                currentA = current,
                lengthM = length,
                resistanceOhmPerKm =
                    if (material == ConductorMaterial.Copper)
                        12.1 / sectionMm2
                    else
                        18.1 / sectionMm2,
                reactanceOhmPerKm = 0.08,
                voltage = voltage,
                powerFactor = powerFactor,
                phase = phase(currentType),
                maximumPercent = Double.MAX_VALUE
            )

        return result.dropPercent to result.dropVolts
    }

    fun sizeConductor(
        input: ConductorSizingInput,
        standard: Standard = Standard.IEC,
        demandFactor: Double = 1.0,
        diversityFactor: Double = 1.0
    ): ConductorSizingResult {

        require(standard == Standard.IEC ||
                standard == Standard.EGYPTIAN ||
                standard == Standard.CEI ||
                standard == Standard.NEC ||
                standard == Standard.CEC)

        val designCurrent =
            calculateDesignCurrent(
                loadWatts = input.load * 1000.0,
                voltage = input.voltage,
                powerFactor = input.powerFactor,
                currentType = input.currentType
            )

        val adjustedCurrent =
            core.loads
                .applyFactors(
                    connectedPowerKw = input.load,
                    demandFactor = demandFactor,
                    diversityFactor = diversityFactor
                )
                .demandKw

        val finalCurrent =
            if (designCurrent > 0.0)
                designCurrent *
                    (adjustedCurrent /
                        input.load.coerceAtLeast(0.000001))
            else
                0.0

        require(finalCurrent > 0.0) {
            "Design current must be greater than zero."
        }

        val result =
            core.cable.calculate(
                CableInput(
                    designCurrentA = finalCurrent,
                    lengthM = input.lineLength,
                    voltage = input.voltage,
                    powerFactor = input.powerFactor,
                    phase = phase(input.currentType),
                    material = material(input.conductor),
                    insulation = insulation(input.insulation),
                    installationMethod =
                        when {
                            input.installationMethod.code
                                .contains("BUR", true) ->
                                com.electricalengineeringpro.app.core.model.InstallationMethod.BURIED

                            input.installationMethod.code
                                .contains("FREE", true) ->
                                com.electricalengineeringpro.app.core.model.InstallationMethod.FREE_AIR

                            input.installationMethod.code
                                .contains("DUCT", true) ->
                                com.electricalengineeringpro.app.core.model.InstallationMethod.DUCT

                            input.installationMethod.code
                                .contains("LAD", true) ->
                                com.electricalengineeringpro.app.core.model.InstallationMethod.LADDER

                            else ->
                                com.electricalengineeringpro.app.core.model.InstallationMethod.TRAY
                        },
                    ambientFactor = 1.0,
                    groupingFactor = 1.0,
                    targetVoltageDropPercent =
                        input.maxVoltageDrop
                )
            )

        val breaker =
            core.breakerSelection.calculate(
                com.electricalengineeringpro.app.core.calculation.BreakerSelectionInput(
                    loadCurrentA = finalCurrent
                )
            )

        return ConductorSizingResult(
            designCurrent = finalCurrent,
            recommendedSection = result.selectedSizeMm2,
            selectedSection = result.selectedSizeMm2,
            ampacity = result.ampacityA,
            voltageDropPercent =
                result.voltageDropPercent,
            voltageDropVolts =
                result.voltageDropPercent *
                    input.voltage / 100.0,
            protectiveDevice =
                breaker.recommendedRatingA,
            shortCircuitCurrentKA = 0.0,
            breakerWithinCableCapacity =
                breaker.recommendedRatingA <=
                    result.ampacityA,
            voltageDropWithinLimit =
                result.voltageDropPercent <=
                    input.maxVoltageDrop,
            notes = listOf(
                "ProfessionalEngineeringCore",
                "Power unit: kW",
                "Cable size: ${result.selectedSizeMm2} mm²",
                "Ampacity: ${result.ampacityA} A",
                "Voltage drop: ${result.voltageDropPercent} %"
            )
        )
    }

    fun evaluateSelectedSection(
        input: ConductorSizingInput,
        selectedSection: Double,
        standard: Standard = Standard.IEC,
        demandFactor: Double = 1.0,
        diversityFactor: Double = 1.0
    ): ConductorSizingResult {

        val calculated =
            sizeConductor(
                input = input,
                standard = standard,
                demandFactor = demandFactor,
                diversityFactor = diversityFactor
            )

        return calculated.copy(
            selectedSection = selectedSection
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
        require(pf in 0.0..1.0)
        require(phases > 0)

        return when (phases) {
            3 ->
                core.power.fromKva(
                    kva =
                        kotlin.math.sqrt(3.0) *
                            voltage *
                            current /
                            1000.0,
                    voltage = voltage,
                    powerFactor = pf,
                    phase = Phase.THREE
                ).activePowerKw

            else ->
                core.power.fromKva(
                    kva =
                        voltage *
                            current /
                            1000.0,
                    voltage = voltage,
                    powerFactor = pf,
                    phase = Phase.SINGLE
                ).activePowerKw
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
            kotlin.math.sqrt(3.0) *
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

        return kotlin.math.sqrt(
            apparent * apparent -
                active * active
        )
    }

    fun calculatePowerFactor(
        active: Double,
        apparent: Double
    ): Double {

        require(active >= 0.0)
        require(apparent > 0.0)

        return (active / apparent)
            .coerceIn(0.0, 1.0)
    }
}

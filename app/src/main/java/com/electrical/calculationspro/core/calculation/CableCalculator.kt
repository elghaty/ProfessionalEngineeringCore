package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.CableResult
import com.electrical.calculationspro.core.model.ConductorMaterial
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.InstallationMethod

class CableCalculator(
    private val loadCalculator:
        LoadCalculator = LoadCalculator(),

    private val voltageDropCalculator:
        VoltageDropCalculator =
        VoltageDropCalculator(),

    private val shortCircuitCalculator:
        ShortCircuitCalculator =
        ShortCircuitCalculator(),

    private val breakerCalculator:
        BreakerCalculator =
        BreakerCalculator()
) {

    private val sectionsMm2 =
        listOf(
            1.5,
            2.5,
            4.0,
            6.0,
            10.0,
            16.0,
            25.0,
            35.0,
            50.0,
            70.0,
            95.0,
            120.0,
            150.0,
            185.0,
            240.0,
            300.0,
            400.0,
            500.0,
            630.0
        )

    fun calculate(
        load: ElectricalLoad,
        maximumVoltageDropPercent: Double = 4.0,
        ambientFactor: Double = 1.0,
        groupingFactor: Double = 1.0,
        sourceShortCircuitMva: Double = 1000.0
    ): CableResult {

        require(maximumVoltageDropPercent > 0.0)
        require(ambientFactor > 0.0)
        require(groupingFactor > 0.0)
        require(sourceShortCircuitMva >= 0.0)
        require(load.lengthM >= 0.0)

        val loadResult =
            loadCalculator.calculate(load)

        val designCurrent =
            loadResult.currentA

        var lastSection =
            sectionsMm2.last()

        for (section in sectionsMm2) {

            lastSection = section

            val baseAmpacity =
                baseAmpacity(
                    section = section,
                    material = load.material,
                    method = load.installationMethod
                )

            val correctedAmpacity =
                baseAmpacity *
                    ambientFactor *
                    groupingFactor

            if (correctedAmpacity < designCurrent) {
                continue
            }

            val resistance =
                resistance(
                    section = section,
                    material = load.material
                )

            val reactance =
                reactance(
                    section = section,
                    method = load.installationMethod
                )

            val voltageDrop =
                voltageDropCalculator.calculate(
                    currentA = designCurrent,
                    lengthM = load.lengthM,
                    resistanceOhmPerKm =
                        resistance,
                    reactanceOhmPerKm =
                        reactance,
                    voltageV = load.voltageV,
                    powerFactor = load.powerFactor,
                    phase = load.phase,
                    maximumPercent =
                        maximumVoltageDropPercent
                )

            if (!voltageDrop.withinLimit) {
                continue
            }

            val fault =
                shortCircuitCalculator.fromSourceFaultLevel(
                    voltageV = load.voltageV,
                    sourceShortCircuitMva =
                        sourceShortCircuitMva,
                    cableLengthM =
                        load.lengthM,
                    cableResistanceOhmPerKm =
                        resistance,
                    cableReactanceOhmPerKm =
                        reactance,
                    parallelRuns = 1
                )

            val protection =
                breakerCalculator.calculate(
                    designCurrentA =
                        designCurrent,
                    cableCapacityA =
                        correctedAmpacity,
                    shortCircuitKA =
                        fault.faultCurrentKA
                )

            val warnings =
                mutableListOf<String>()

            warnings += protection.warnings

            if (
                correctedAmpacity <
                designCurrent
            ) {
                warnings +=
                    "Cable ampacity is below design current."
            }

            if (!voltageDrop.withinLimit) {
                warnings +=
                    "Voltage drop exceeds the specified limit."
            }

            if (
                sourceShortCircuitMva > 0.0 &&
                fault.faultCurrentKA <= 0.0
            ) {
                warnings +=
                    "Short-circuit current could not be established."
            }

            return CableResult(
                sectionMm2 = section,
                designCurrentA = designCurrent,
                baseAmpacityA = baseAmpacity,
                correctedAmpacityA =
                    correctedAmpacity,
                voltageDropPercent =
                    voltageDrop.dropPercent,
                voltageDropVolts =
                    voltageDrop.dropVolts,
                breakerRatingA =
                    protection.ratedCurrentA,
                shortCircuitCurrentKA =
                    fault.faultCurrentKA,
                acceptable =
                    protection.acceptable &&
                        voltageDrop.withinLimit &&
                        correctedAmpacity >=
                        designCurrent,
                warnings = warnings
            )
        }

        val finalBaseAmpacity =
            baseAmpacity(
                section = lastSection,
                material = load.material,
                method = load.installationMethod
            )

        return CableResult(
            sectionMm2 = lastSection,
            designCurrentA = designCurrent,
            baseAmpacityA = finalBaseAmpacity,
            correctedAmpacityA =
                finalBaseAmpacity *
                    ambientFactor *
                    groupingFactor,
            voltageDropPercent = 0.0,
            voltageDropVolts = 0.0,
            breakerRatingA = 0.0,
            shortCircuitCurrentKA = 0.0,
            acceptable = false,
            warnings =
                listOf(
                    "No standard cable section satisfies all design requirements."
                )
        )
    }

    private fun resistance(
        section: Double,
        material: ConductorMaterial
    ): Double {

        val resistivity =
            when (material) {

                ConductorMaterial.COPPER ->
                    18.0

                ConductorMaterial.ALUMINUM ->
                    29.0
            }

        return resistivity / section
    }

    private fun reactance(
        section: Double,
        method: InstallationMethod
    ): Double {

        /*
         * Typical engineering design values.
         * The final project design should use the selected
         * cable manufacturer's data where available.
         */
        return when (method) {

            InstallationMethod.FREE_AIR ->
                0.075

            InstallationMethod.CABLE_TRAY ->
                0.080

            InstallationMethod.LADDER ->
                0.080

            InstallationMethod.DIRECT_BURIED ->
                0.085

            InstallationMethod.CONDUIT ->
                0.090

            InstallationMethod.TRUNKING ->
                0.090
        }
    }

    private fun baseAmpacity(
        section: Double,
        material: ConductorMaterial,
        method: InstallationMethod
    ): Double {

        val copperAmpacity =
            mapOf(
                1.5 to 18.0,
                2.5 to 24.0,
                4.0 to 32.0,
                6.0 to 41.0,
                10.0 to 57.0,
                16.0 to 76.0,
                25.0 to 101.0,
                35.0 to 125.0,
                50.0 to 150.0,
                70.0 to 192.0,
                95.0 to 232.0,
                120.0 to 269.0,
                150.0 to 309.0,
                185.0 to 353.0,
                240.0 to 415.0,
                300.0 to 473.0,
                400.0 to 556.0,
                500.0 to 641.0,
                630.0 to 746.0
            )

        val materialFactor =
            when (material) {

                ConductorMaterial.COPPER ->
                    1.0

                ConductorMaterial.ALUMINUM ->
                    0.82
            }

        val installationFactor =
            when (method) {

                InstallationMethod.FREE_AIR ->
                    1.15

                InstallationMethod.CABLE_TRAY ->
                    1.05

                InstallationMethod.LADDER ->
                    1.10

                InstallationMethod.DIRECT_BURIED ->
                    0.92

                InstallationMethod.CONDUIT ->
                    1.00

                InstallationMethod.TRUNKING ->
                    1.00
            }

        return (
            copperAmpacity[section] ?: 0.0
            ) *
            materialFactor *
            installationFactor
    }
}

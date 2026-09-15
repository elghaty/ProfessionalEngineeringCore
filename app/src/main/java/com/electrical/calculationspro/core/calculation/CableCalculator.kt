package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.CableResult
import com.electrical.calculationspro.core.model.ConductorMaterial
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.Phase
import kotlin.math.sqrt

class CableCalculator(
    private val power: PowerCalculator =
        PowerCalculator(),
    private val voltageDrop:
        VoltageDropCalculator =
        VoltageDropCalculator(),
    private val shortCircuit:
        ShortCircuitCalculator =
        ShortCircuitCalculator(),
    private val breaker:
        BreakerCalculator =
        BreakerCalculator()
) {

    private val sections =
        listOf(
            1.5, 2.5, 4.0, 6.0,
            10.0, 16.0, 25.0, 35.0,
            50.0, 70.0, 95.0, 120.0,
            150.0, 185.0, 240.0,
            300.0, 400.0, 500.0,
            630.0
        )

    fun calculate(
        load: ElectricalLoad,
        maximumVoltageDropPercent: Double = 4.0,
        ambientFactor: Double = 1.0,
        groupingFactor: Double = 1.0
    ): CableResult {

        require(ambientFactor > 0.0)
        require(groupingFactor > 0.0)

        val loadResult =
            com.electrical.calculationspro
                .core.calculation.LoadCalculator()
                .calculate(load)

        val designCurrent =
            loadResult.currentA

        for (section in sections) {

            val baseAmpacity =
                baseAmpacity(
                    section,
                    load.material,
                    load.installationMethod
                )

            val corrected =
                baseAmpacity *
                    ambientFactor *
                    groupingFactor

            if (corrected < designCurrent) {
                continue
            }

            val r =
                resistance(
                    section,
                    load.material
                )

            val x = 0.08

            val vd =
                voltageDrop.calculate(
                    currentA = designCurrent,
                    lengthM = load.lengthM,
                    resistanceOhmPerKm = r,
                    reactanceOhmPerKm = x,
                    voltageV = load.voltageV,
                    powerFactor = load.powerFactor,
                    phase = load.phase,
                    maximumPercent =
                        maximumVoltageDropPercent
                )

            if (!vd.withinLimit) {
                continue
            }

            val fault =
                shortCircuit.fromSourceFaultLevel(
                    voltageV = load.voltageV,
                    sourceFaultMva = 1000.0
                )

            val protection =
                breaker.calculate(
                    designCurrentA =
                        designCurrent,
                    cableCapacityA =
                        corrected,
                    shortCircuitKA =
                        fault.initialSymmetricalCurrentKA
                )

            return CableResult(
                sectionMm2 = section,
                designCurrentA = designCurrent,
                baseAmpacityA = baseAmpacity,
                correctedAmpacityA = corrected,
                voltageDropPercent =
                    vd.dropPercent,
                voltageDropVolts =
                    vd.dropVolts,
                breakerRatingA =
                    protection.ratedCurrentA,
                shortCircuitCurrentKA =
                    fault.initialSymmetricalCurrentKA,
                acceptable =
                    protection.acceptable,
                warnings =
                    protection.warnings
            )
        }

        val last = sections.last()

        val base =
            baseAmpacity(
                last,
                load.material,
                load.installationMethod
            )

        val corrected =
            base *
                ambientFactor *
                groupingFactor

        val vd =
            voltageDrop.calculate(
                currentA = designCurrent,
                lengthM = load.lengthM,
                resistanceOhmPerKm =
                    resistance(
                        last,
                        load.material
                    ),
                reactanceOhmPerKm = 0.08,
                voltageV = load.voltageV,
                powerFactor = load.powerFactor,
                phase = load.phase,
                maximumPercent =
                    maximumVoltageDropPercent
            )

        return CableResult(
            sectionMm2 = last,
            designCurrentA = designCurrent,
            baseAmpacityA = base,
            correctedAmpacityA = corrected,
            voltageDropPercent =
                vd.dropPercent,
            voltageDropVolts =
                vd.dropVolts,
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

    private fun baseAmpacity(
        section: Double,
        material: ConductorMaterial,
        method:
            com.electrical.calculationspro
                .core.model.InstallationMethod
    ): Double {

        val copper =
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

        val aluminiumFactor =
            0.82

        val installationFactor =
            when (method) {
                com.electrical.calculationspro
                    .core.model.InstallationMethod.FREE_AIR ->
                    1.15

                com.electrical.calculationspro
                    .core.model.InstallationMethod.CABLE_TRAY ->
                    1.05

                com.electrical.calculationspro
                    .core.model.InstallationMethod.LADDER ->
                    1.10

                com.electrical.calculationspro
                    .core.model.InstallationMethod.DIRECT_BURIED ->
                    0.92

                else ->
                    1.0
            }

        return (
            copper[section] ?: 0.0
            ) *
            if (
                material ==
                ConductorMaterial.ALUMINUM
            ) {
                aluminiumFactor
            } else {
                1.0
            } *
            installationFactor
    }
}

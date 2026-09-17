package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.CableResult
import com.electricalengineeringpro.app.core.model.ConductorMaterial
import com.electricalengineeringpro.app.core.model.Phase
import kotlin.math.sqrt

class CableCalculator {

    private data class CableSize(
        val sizeMm2: Double,
        val baseAmpacityA: Double
    )

    private val copperSizes = listOf(
        CableSize(1.5, 18.0),
        CableSize(2.5, 24.0),
        CableSize(4.0, 32.0),
        CableSize(6.0, 41.0),
        CableSize(10.0, 57.0),
        CableSize(16.0, 76.0),
        CableSize(25.0, 101.0),
        CableSize(35.0, 125.0),
        CableSize(50.0, 150.0),
        CableSize(70.0, 192.0),
        CableSize(95.0, 232.0),
        CableSize(120.0, 269.0),
        CableSize(150.0, 309.0),
        CableSize(185.0, 353.0),
        CableSize(240.0, 415.0),
        CableSize(300.0, 473.0),
        CableSize(400.0, 556.0)
    )

    private val aluminiumSizes = listOf(
        CableSize(16.0, 59.0),
        CableSize(25.0, 78.0),
        CableSize(35.0, 96.0),
        CableSize(50.0, 116.0),
        CableSize(70.0, 147.0),
        CableSize(95.0, 178.0),
        CableSize(120.0, 205.0),
        CableSize(150.0, 233.0),
        CableSize(185.0, 265.0),
        CableSize(240.0, 310.0),
        CableSize(300.0, 354.0),
        CableSize(400.0, 416.0)
    )

    fun calculate(input: CableInput): CableResult {

        require(input.designCurrentA > 0.0) {
            "Design current must be greater than zero."
        }

        require(input.lengthM >= 0.0) {
            "Cable length must not be negative."
        }

        require(input.voltage > 0.0) {
            "Voltage must be greater than zero."
        }

        require(input.powerFactor in 0.01..1.0) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(input.ambientFactor > 0.0) {
            "Ambient correction factor must be greater than zero."
        }

        require(input.groupingFactor > 0.0) {
            "Grouping correction factor must be greater than zero."
        }

        require(input.targetVoltageDropPercent >= 0.0) {
            "Voltage-drop limit must not be negative."
        }

        val table =
            if (input.material == ConductorMaterial.COPPER) {
                copperSizes
            } else {
                aluminiumSizes
            }

        val correctionFactor =
            input.ambientFactor * input.groupingFactor

        require(correctionFactor > 0.0) {
            "Total correction factor must be greater than zero."
        }

        val requiredBaseAmpacity =
            input.designCurrentA / correctionFactor

        var selected =
            table.firstOrNull {
                it.baseAmpacityA >= requiredBaseAmpacity
            } ?: table.last()

        var voltageDropPercent =
            calculateVoltageDropPercent(
                sizeMm2 = selected.sizeMm2,
                lengthM = input.lengthM,
                currentA = input.designCurrentA,
                voltageV = input.voltage,
                powerFactor = input.powerFactor,
                phase = input.phase,
                material = input.material
            )

        while (
            voltageDropPercent >
            input.targetVoltageDropPercent &&
            selected != table.last()
        ) {

            val index = table.indexOf(selected)

            selected =
                table.getOrNull(index + 1)
                    ?: break

            voltageDropPercent =
                calculateVoltageDropPercent(
                    sizeMm2 = selected.sizeMm2,
                    lengthM = input.lengthM,
                    currentA = input.designCurrentA,
                    voltageV = input.voltage,
                    powerFactor = input.powerFactor,
                    phase = input.phase,
                    material = input.material
                )
        }

        val correctedAmpacity =
            selected.baseAmpacityA * correctionFactor

        val utilizationPercent =
            input.designCurrentA /
                correctedAmpacity *
                100.0

        return CableResult(
            selectedSizeMm2 = selected.sizeMm2,
            ampacityA = correctedAmpacity,
            voltageDropPercent = voltageDropPercent,
            utilizationPercent = utilizationPercent,
            conductorDescription =
                buildDescription(
                    sizeMm2 = selected.sizeMm2,
                    phase = input.phase,
                    material = input.material,
                    insulation = input.insulation.name
                )
        )
    }

    private fun calculateVoltageDropPercent(
        sizeMm2: Double,
        lengthM: Double,
        currentA: Double,
        voltageV: Double,
        powerFactor: Double,
        phase: Phase,
        material: ConductorMaterial
    ): Double {

        val resistivity =
            when (material) {
                ConductorMaterial.COPPER -> 0.0175
                ConductorMaterial.ALUMINIUM -> 0.0282
            }

        val resistanceOhm =
            resistivity *
                lengthM /
                sizeMm2

        val sinPhi =
            sqrt(
                1.0 -
                    powerFactor * powerFactor
            )

        val reactanceOhmPerKm =
            0.08

        val reactanceOhm =
            reactanceOhmPerKm *
                lengthM /
                1000.0

        val impedanceDropV =
            when (phase) {

                Phase.THREE ->
                    sqrt(3.0) *
                        currentA *
                        (
                            resistanceOhm *
                                powerFactor +
                                reactanceOhm *
                                sinPhi
                            )

                Phase.SINGLE ->
                    2.0 *
                        currentA *
                        (
                            resistanceOhm *
                                powerFactor +
                                reactanceOhm *
                                sinPhi
                            )
            }

        return impedanceDropV /
            voltageV *
            100.0
    }

    private fun buildDescription(
        sizeMm2: Double,
        phase: Phase,
        material: ConductorMaterial,
        insulation: String
    ): String {

        val conductors =
            when (phase) {
                Phase.THREE -> "4C"
                Phase.SINGLE -> "2C"
            }

        val materialName =
            material.name
                .lowercase()
                .replaceFirstChar {
                    it.uppercase()
                }

        return "$conductors × ${format(sizeMm2)} mm² " +
            "$materialName $insulation"
    }

    private fun format(value: Double): String =
        if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
}

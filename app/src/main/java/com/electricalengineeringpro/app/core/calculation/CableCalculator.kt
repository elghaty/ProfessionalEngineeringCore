package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.*
import kotlin.math.sqrt

class CableCalculator {

    private val copperSizes = listOf(
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
        400.0 to 556.0
    )

    private val aluminiumSizes = listOf(
        16.0 to 59.0,
        25.0 to 78.0,
        35.0 to 96.0,
        50.0 to 116.0,
        70.0 to 147.0,
        95.0 to 178.0,
        120.0 to 205.0,
        150.0 to 233.0,
        185.0 to 265.0,
        240.0 to 310.0,
        300.0 to 354.0,
        400.0 to 416.0
    )

    fun calculate(input: CableInput): CableResult {
        require(input.designCurrentA > 0)
        require(input.lengthM >= 0)
        require(input.voltage > 0)
        require(input.powerFactor in 0.01..1.0)
        require(input.ambientFactor > 0)
        require(input.groupingFactor > 0)

        val table = if (input.material == ConductorMaterial.COPPER) {
            copperSizes
        } else {
            aluminiumSizes
        }

        val correction = input.ambientFactor * input.groupingFactor
        val requiredAmpacity = input.designCurrentA / correction

        var selected = table.firstOrNull { it.second >= requiredAmpacity }

        if (selected == null) {
            selected = table.last()
        }

        var size = selected.first
        var ampacity = selected.second * correction

        var drop = voltageDrop(
            size,
            input.lengthM,
            input.designCurrentA,
            input.voltage,
            input.powerFactor,
            input.phase,
            input.material
        )

        while (drop > input.targetVoltageDropPercent && size < table.last().first) {
            val index = table.indexOfFirst { it.first == size }
            val next = table.getOrNull(index + 1) ?: break
            size = next.first
            ampacity = next.second * correction

            drop = voltageDrop(
                size,
                input.lengthM,
                input.designCurrentA,
                input.voltage,
                input.powerFactor,
                input.phase,
                input.material
            )
        }

        return CableResult(
            selectedSizeMm2 = size,
            ampacityA = ampacity,
            voltageDropPercent = drop,
            utilizationPercent = input.designCurrentA / ampacity * 100.0,
            conductorDescription =
                "${if (input.phase == Phase.THREE) "4C" else "2C"} × ${format(size)} mm² " +
                "${input.material.name.lowercase().replaceFirstChar { it.uppercase() }} " +
                input.insulation.name
        )
    }

    private fun voltageDrop(
        size: Double,
        length: Double,
        current: Double,
        voltage: Double,
        pf: Double,
        phase: Phase,
        material: ConductorMaterial
    ): Double {
        val resistivity = if (material == ConductorMaterial.COPPER) 0.0175 else 0.0282
        val resistance = resistivity * length / size
        val sinPhi = sqrt(1.0 - pf * pf)

        val drop = if (phase == Phase.THREE) {
            sqrt(3.0) * current * resistance * pf
        } else {
            2.0 * current * resistance * pf
        }

        return drop / voltage * 100.0
    }

    private fun format(value: Double): String =
        if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()
}

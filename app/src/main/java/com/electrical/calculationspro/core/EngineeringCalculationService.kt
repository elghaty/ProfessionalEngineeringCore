package com.electrical.calculationspro.core

import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.PowerResult

/**
 * Optional application service.
 *
 * It does not contain engineering formulas.
 * It only routes requests to ProfessionalEngineeringCore.
 *
 * ProfessionalEngineeringCore remains the single engineering facade.
 */
class EngineeringCalculationService(
    private val core:
        ProfessionalEngineeringCore =
        ProfessionalEngineeringCore.instance
) {

    fun calculatePower(
        powerKw: Double,
        voltageV: Double,
        powerFactor: Double,
        phase: Phase
    ): EngineeringResult<PowerResult> {

        return execute {
            core.power.fromKw(
                powerKw = powerKw,
                voltageV = voltageV,
                powerFactor = powerFactor,
                phase = phase
            )
        }
    }

    fun calculateLoad(
        load: ElectricalLoad
    ): EngineeringResult<
        com.electrical.calculationspro
            .core.model.LoadResult
        > {

        return execute {
            core.loads.calculate(load)
        }
    }

    private inline fun <T> execute(
        block: () -> T
    ): EngineeringResult<T> {

        return try {
            EngineeringResult.Success(
                block()
            )
        } catch (
            e: IllegalArgumentException
        ) {
            EngineeringResult.Error(
                e.message
                    ?: "Invalid engineering input."
            )
        } catch (
            e: Exception
        ) {
            EngineeringResult.Error(
                e.message
                    ?: "Engineering calculation failed."
            )
        }
    }
}

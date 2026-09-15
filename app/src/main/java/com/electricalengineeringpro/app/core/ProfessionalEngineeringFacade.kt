package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.GeneratorInput
import com.electricalengineeringpro.app.core.calculation.GeneratorResult
import com.electricalengineeringpro.app.core.calculation.MdbInput
import com.electricalengineeringpro.app.core.calculation.MdbResult
import com.electricalengineeringpro.app.core.calculation.ProtectionInput
import com.electricalengineeringpro.app.core.calculation.ProtectionResult
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.result.EngineeringResult

class ProfessionalEngineeringFacade(
    private val core: ProfessionalEngineeringCore =
        ProfessionalEngineeringCore.instance
) {

    fun calculateGenerator(
        input: GeneratorInput
    ): EngineeringResult<GeneratorResult> =
        execute {
            core.generators.calculate(input)
        }

    fun calculateProtection(
        input: ProtectionInput
    ): EngineeringResult<ProtectionResult> =
        execute {
            core.protection.calculate(input)
        }

    fun calculateMdb(
        input: MdbInput
    ): EngineeringResult<MdbResult> =
        execute {
            core.mdb.calculate(input)
        }

    fun calculateLoadSchedule(
        loads: List<ElectricalLoad>
    ) = execute {
        core.loadSchedule.calculate(loads)
    }

    fun calculateNetwork(
        loads: List<ElectricalLoad>,
        voltageV: Double = 400.0,
        powerFactor: Double = 0.9
    ) = execute {
        core.network.calculate(
            loads = loads,
            voltageV = voltageV,
            powerFactor = powerFactor
        )
    }

    private inline fun <T> execute(
        block: () -> T
    ): EngineeringResult<T> {

        return try {
            EngineeringResult.Success(block())
        } catch (e: IllegalArgumentException) {
            EngineeringResult.Error(
                e.message ?: "Invalid engineering input."
            )
        } catch (e: Exception) {
            EngineeringResult.Error(
                e.message ?: "Engineering calculation failed."
            )
        }
    }
}

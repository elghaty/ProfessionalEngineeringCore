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
        runCalculation {
            core.generators.calculate(input)
        }

    fun calculateProtection(
        input: ProtectionInput
    ): EngineeringResult<ProtectionResult> =
        runCalculation {
            core.protection.calculate(input)
        }

    fun calculateMdb(
        input: MdbInput
    ): EngineeringResult<MdbResult> =
        runCalculation {
            core.mdb.calculate(input)
        }

    fun calculateLoadSchedule(
        loads: List<ElectricalLoad>
    ) = runCalculation {
        core.loadSchedule.calculate(loads)
    }

    private inline fun <T> runCalculation(
        calculation: () -> T
    ): EngineeringResult<T> {

        return try {
            EngineeringResult.Success(calculation())
        } catch (e: IllegalArgumentException) {
            EngineeringResult.Error(
                message = e.message ?: "Invalid engineering input."
            )
        } catch (e: Exception) {
            EngineeringResult.Error(
                message = e.message ?: "Engineering calculation failed."
            )
        }
    }
}

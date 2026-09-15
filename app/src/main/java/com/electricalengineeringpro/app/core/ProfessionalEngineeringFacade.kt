package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.model.*
import com.electricalengineeringpro.app.core.result.EngineeringResult

class ProfessionalEngineeringFacade(
    private val core: ProfessionalEngineeringCore = ProfessionalEngineeringCore()
) {

    fun calculateLoad(
        input: ElectricalLoad
    ): EngineeringResult<Any> {
        return try {
            EngineeringResult.Success(core.loads.calculate(input))
        } catch (e: Exception) {
            EngineeringResult.Error(e.message ?: "Load calculation error.")
        }
    }

    fun calculateCable(
        input: CableInput
    ): EngineeringResult<CableResult> {
        return try {
            EngineeringResult.Success(core.cables.calculate(input))
        } catch (e: Exception) {
            EngineeringResult.Error(e.message ?: "Cable calculation error.")
        }
    }

    fun calculateBreaker(
        input: BreakerInput
    ): EngineeringResult<BreakerResult> {
        return try {
            EngineeringResult.Success(core.breakers.calculate(input))
        } catch (e: Exception) {
            EngineeringResult.Error(e.message ?: "Breaker calculation error.")
        }
    }

    fun calculateTransformer(
        input: TransformerInput
    ): EngineeringResult<TransformerResult> {
        return try {
            EngineeringResult.Success(core.transformers.calculate(input))
        } catch (e: Exception) {
            EngineeringResult.Error(e.message ?: "Transformer calculation error.")
        }
    }

    fun calculateMotor(
        input: MotorInput
    ): EngineeringResult<MotorResult> {
        return try {
            EngineeringResult.Success(core.motors.calculate(input))
        } catch (e: Exception) {
            EngineeringResult.Error(e.message ?: "Motor calculation error.")
        }
    }

    fun calculatePump(
        input: PumpInput
    ): EngineeringResult<PumpResult> {
        return try {
            EngineeringResult.Success(core.pumps.calculate(input))
        } catch (e: Exception) {
            EngineeringResult.Error(e.message ?: "Pump calculation error.")
        }
    }

    fun calculateShortCircuit(
        input: ShortCircuitInput
    ): EngineeringResult<ShortCircuitResult> {
        return try {
            EngineeringResult.Success(core.shortCircuit.calculate(input))
        } catch (e: Exception) {
            EngineeringResult.Error(e.message ?: "Short-circuit calculation error.")
        }
    }
}

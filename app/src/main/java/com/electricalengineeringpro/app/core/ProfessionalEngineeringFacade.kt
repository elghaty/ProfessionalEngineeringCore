package com.electricalengineeringpro.app.core

import com.electricalengineeringpro.app.core.calculation.BreakerInput
import com.electricalengineeringpro.app.core.calculation.BreakerResult
import com.electricalengineeringpro.app.core.calculation.BreakerSelectionInput
import com.electricalengineeringpro.app.core.calculation.BreakerSelectionResult
import com.electricalengineeringpro.app.core.calculation.CableInput
import com.electricalengineeringpro.app.core.calculation.CableResult
import com.electricalengineeringpro.app.core.calculation.CompleteDesignInput
import com.electricalengineeringpro.app.core.calculation.CompleteDesignResult
import com.electricalengineeringpro.app.core.calculation.GeneratorInput
import com.electricalengineeringpro.app.core.calculation.GeneratorResult
import com.electricalengineeringpro.app.core.calculation.MdbInput
import com.electricalengineeringpro.app.core.calculation.MdbResult
import com.electricalengineeringpro.app.core.calculation.MotorInput
import com.electricalengineeringpro.app.core.calculation.MotorResult
import com.electricalengineeringpro.app.core.calculation.ProtectionInput
import com.electricalengineeringpro.app.core.calculation.ProtectionResult
import com.electricalengineeringpro.app.core.calculation.PumpInput
import com.electricalengineeringpro.app.core.calculation.PumpResult
import com.electricalengineeringpro.app.core.calculation.ShortCircuitInput
import com.electricalengineeringpro.app.core.calculation.ShortCircuitResult
import com.electricalengineeringpro.app.core.calculation.TransformerInput
import com.electricalengineeringpro.app.core.calculation.TransformerResult
import com.electricalengineeringpro.app.core.calculation.TransformerSizingInput
import com.electricalengineeringpro.app.core.calculation.TransformerSizingResult
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.result.EngineeringResult

class ProfessionalEngineeringFacade(
    private val core: ProfessionalEngineeringCore =
        ProfessionalEngineeringCore.instance
) {

    fun calculateLoad(
        load: ElectricalLoad
    ) = execute {
        core.loads.calculate(load)
    }

    fun calculateLoadSchedule(
        loads: List<ElectricalLoad>
    ) = execute {
        core.loadSchedule.calculate(loads)
    }

    fun calculateDesignSummary(
        loads: List<ElectricalLoad>,
        voltage: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ) = execute {
        core.designSummary.calculate(
            loads = loads,
            voltage = voltage,
            powerFactor = powerFactor,
            phase = phase
        )
    }

    fun calculateCable(
        input: CableInput
    ): EngineeringResult<CableResult> =
        execute {
            core.cable.calculate(input)
        }

    fun calculateBreaker(
        input: BreakerInput
    ): EngineeringResult<BreakerResult> =
        execute {
            core.breaker.calculate(input)
        }

    fun calculateBreakerSelection(
        input: BreakerSelectionInput
    ): EngineeringResult<BreakerSelectionResult> =
        execute {
            core.breakerSelection.calculate(input)
        }

    fun calculateTransformer(
        input: TransformerInput
    ): EngineeringResult<TransformerResult> =
        execute {
            core.transformer.calculate(input)
        }

    fun calculateTransformerSizing(
        input: TransformerSizingInput
    ): EngineeringResult<TransformerSizingResult> =
        execute {
            core.transformerSizing.calculate(input)
        }

    fun calculateGenerator(
        input: GeneratorInput
    ): EngineeringResult<GeneratorResult> =
        execute {
            core.generators.calculate(input)
        }

    fun calculateMotor(
        input: MotorInput
    ): EngineeringResult<MotorResult> =
        execute {
            core.motors.calculate(input)
        }

    fun calculatePump(
        input: PumpInput
    ): EngineeringResult<PumpResult> =
        execute {
            core.pumps.calculate(input)
        }

    fun calculateShortCircuit(
        input: ShortCircuitInput
    ): EngineeringResult<ShortCircuitResult> =
        execute {
            core.shortCircuit.calculate(input)
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

    fun calculateCompleteDesign(
        input: CompleteDesignInput
    ): EngineeringResult<CompleteDesignResult> =
        execute {
            core.completeDesign.calculate(input)
        }

    fun calculateNetwork(
        loads: List<ElectricalLoad>,
        voltage: Double = 400.0,
        powerFactor: Double = 0.90,
        phase: Phase = Phase.THREE
    ) = execute {
        core.network.calculate(
            loads = loads,
            voltageV = voltage,
            powerFactor = powerFactor,
            phase = phase
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

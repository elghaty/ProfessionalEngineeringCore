package com.elghaty.professionalengineeringcore.core

import com.elghaty.professionalengineeringcore.core.calculation.*
import com.elghaty.professionalengineeringcore.core.model.*
import com.elghaty.professionalengineeringcore.core.network.NetworkCalculator
import com.elghaty.professionalengineeringcore.core.network.NetworkDesignBuilder
import com.elghaty.professionalengineeringcore.core.report.EngineeringReportGenerator
import com.elghaty.professionalengineeringcore.core.sld.SldGenerator

class ProfessionalEngineeringCore {

    val loadCalculator = LoadCalculator()
    val loadScheduleCalculator = LoadScheduleCalculator()
    val cableCalculator = CableCalculator()
    val voltageDropCalculator = VoltageDropCalculator()
    val breakerCalculator = BreakerCalculator()
    val protectionCalculator = ProtectionCalculator()
    val transformerCalculator = TransformerCalculator()
    val generatorCalculator = GeneratorCalculator()
    val motorCalculator = MotorCalculator()
    val pumpCalculator = PumpCalculator()
    val shortCircuitCalculator = ShortCircuitCalculator()

    val networkDesignBuilder = NetworkDesignBuilder()
    val networkCalculator = NetworkCalculator()
    val sldGenerator = SldGenerator()
    val reportGenerator = EngineeringReportGenerator()

    fun calculateLoad(input: LoadInput): LoadResult =
        loadCalculator.calculate(input)

    fun calculateLoadSchedule(input: LoadScheduleInput): LoadScheduleResult =
        loadScheduleCalculator.calculate(input)

    fun calculateCable(input: CableInput): CableResult =
        cableCalculator.calculate(input)

    fun calculateVoltageDrop(input: VoltageDropInput): VoltageDropResult =
        voltageDropCalculator.calculate(input)

    fun calculateBreaker(input: BreakerInput): BreakerResult =
        breakerCalculator.calculate(input)

    fun calculateProtection(input: ProtectionInput): ProtectionResult =
        protectionCalculator.calculate(input)

    fun calculateTransformer(input: TransformerInput): TransformerResult =
        transformerCalculator.calculate(input)

    fun calculateGenerator(input: GeneratorInput): GeneratorResult =
        generatorCalculator.calculate(input)

    fun calculateMotor(input: MotorInput): MotorResult =
        motorCalculator.calculate(input)

    fun calculatePump(input: PumpInput): PumpResult =
        pumpCalculator.calculate(input)

    fun calculateShortCircuit(input: ShortCircuitInput): ShortCircuitResult =
        shortCircuitCalculator.calculate(input)
}

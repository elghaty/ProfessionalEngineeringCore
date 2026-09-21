package com.elghaty.professionalengineeringcore.core.model

data class BreakerInput(
    val designCurrentA: Double,
    val faultCurrentKA: Double = 0.0,
    val breakerType: BreakerType = BreakerType.MCCB,
    val availableRatingsA: List<Double> = emptyList(),
    val availableBreakingCapacitiesKA: List<Double> = emptyList()
)

data class BreakerResult(
    val ratedCurrentA: Double?,
    val breakingCapacityKA: Double?,
    val breakerType: BreakerType,
    val status: CalculationStatus,
    val message: String = ""
)

data class ProtectionInput(
    val designCurrentA: Double,
    val breakerRatedCurrentA: Double,
    val faultCurrentKA: Double,
    val breakerBreakingCapacityKA: Double
)

data class ProtectionResult(
    val overloadProtected: Boolean,
    val shortCircuitProtected: Boolean,
    val breakingCapacityAdequate: Boolean,
    val status: CalculationStatus,
    val message: String
)

data class TransformerInput(
    val ratedKva: Double,
    val primaryVoltage: Double,
    val secondaryVoltage: Double,
    val impedancePercent: Double,
    val phase: Phase = Phase.THREE,
    val sourceShortCircuitMva: Double? = null
)

data class TransformerResult(
    val primaryCurrentA: Double,
    val secondaryCurrentA: Double,
    val shortCircuitCurrentA: Double,
    val shortCircuitMva: Double,
    val status: CalculationStatus,
    val message: String = ""
)

data class GeneratorInput(
    val ratedKva: Double,
    val voltage: Double,
    val powerFactor: Double = 0.8,
    val phase: Phase = Phase.THREE,
    val subtransientReactancePercent: Double = 15.0
)

data class GeneratorResult(
    val ratedKw: Double,
    val ratedCurrentA: Double,
    val initialShortCircuitCurrentA: Double,
    val status: CalculationStatus,
    val message: String = ""
)

data class MotorInput(
    val ratedKw: Double,
    val voltage: Double,
    val powerFactor: Double,
    val efficiency: Double,
    val phase: Phase = Phase.THREE,
    val startingCurrentMultiplier: Double = 6.0
)

data class MotorResult(
    val fullLoadCurrentA: Double,
    val startingCurrentA: Double,
    val status: CalculationStatus,
    val message: String = ""
)

data class PumpInput(
    val flowM3s: Double,
    val headM: Double,
    val fluidDensityKgM3: Double = 1000.0,
    val pumpEfficiency: Double = 0.8,
    val motorEfficiency: Double = 0.9,
    val voltage: Double,
    val powerFactor: Double = 0.85,
    val phase: Phase = Phase.THREE
)

data class PumpResult(
    val hydraulicPowerKw: Double,
    val motorInputPowerKw: Double,
    val currentA: Double,
    val status: CalculationStatus,
    val message: String = ""
)

data class VoltageDropInput(
    val phase: Phase,
    val voltage: Double,
    val currentA: Double,
    val lengthM: Double,
    val powerFactor: Double,
    val resistanceOhmPerKm: Double,
    val reactanceOhmPerKm: Double = 0.08
)

data class VoltageDropResult(
    val voltageDropV: Double,
    val voltageDropPercent: Double,
    val status: CalculationStatus,
    val message: String = ""
)

data class ShortCircuitInput(
    val sourceType: SourceType,
    val voltage: Double,
    val phase: Phase = Phase.THREE,
    val transformerKva: Double? = null,
    val transformerImpedancePercent: Double? = null,
    val utilityFaultMva: Double? = null,
    val generatorKva: Double? = null,
    val generatorSubtransientReactancePercent: Double? = null,
    val upstreamFaultCurrentKA: Double? = null
)

data class ShortCircuitResult(
    val faultCurrentKA: Double,
    val faultMva: Double,
    val sourceImpedanceOhm: Double,
    val status: CalculationStatus,
    val message: String = ""
)

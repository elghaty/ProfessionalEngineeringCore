package com.electrical.calculationspro.core.model

enum class Phase {
    DC,
    SINGLE,
    TWO,
    THREE
}

enum class ConductorMaterial {
    COPPER,
    ALUMINUM
}

enum class Insulation {
    PVC,
    XLPE,
    EPR,
    RUBBER
}

enum class InstallationMethod {
    CONDUIT,
    TRUNKING,
    CABLE_TRAY,
    LADDER,
    DIRECT_BURIED,
    FREE_AIR
}

enum class BreakerType {
    MCB,
    MCCB,
    ACB,
    FUSE
}

enum class SourceType {
    UTILITY,
    TRANSFORMER,
    GENERATOR,
    PANEL
}

enum class SldElementType {
    UTILITY,
    TRANSFORMER,
    GENERATOR,
    BUS,
    MDB,
    SMDB,
    DB,
    MCC,
    BREAKER,
    FEEDER,
    MOTOR,
    PUMP,
    LOAD
}

data class ElectricalLoad(
    val id: String,
    val name: String,
    val powerKw: Double,
    val quantity: Int = 1,
    val voltageV: Double = 400.0,
    val phase: Phase = Phase.THREE,
    val powerFactor: Double = 0.90,
    val efficiency: Double = 1.0,
    val demandFactor: Double = 1.0,
    val diversityFactor: Double = 1.0,
    val lengthM: Double = 0.0,
    val material: ConductorMaterial = ConductorMaterial.COPPER,
    val insulation: Insulation = Insulation.XLPE,
    val installationMethod: InstallationMethod = InstallationMethod.CABLE_TRAY
)

data class PowerResult(
    val activePowerKw: Double,
    val apparentPowerKva: Double,
    val reactivePowerKvar: Double,
    val currentA: Double,
    val powerFactor: Double
)

data class LoadResult(
    val connectedKw: Double,
    val demandKw: Double,
    val designKw: Double,
    val currentA: Double,
    val apparentPowerKva: Double
)

data class VoltageDropResult(
    val dropVolts: Double,
    val dropPercent: Double,
    val withinLimit: Boolean
)

data class ShortCircuitResult(
    val initialSymmetricalCurrentKA: Double,
    val peakCurrentKA: Double,
    val thermalCurrentKA: Double,
    val faultMva: Double,
    val impedanceOhm: Double,
    val kappa: Double,
    val i2t: Double
)

data class CableResult(
    val sectionMm2: Double,
    val designCurrentA: Double,
    val baseAmpacityA: Double,
    val correctedAmpacityA: Double,
    val voltageDropPercent: Double,
    val voltageDropVolts: Double,
    val breakerRatingA: Double,
    val shortCircuitCurrentKA: Double,
    val acceptable: Boolean,
    val warnings: List<String>
)

data class BreakerResult(
    val type: BreakerType,
    val ratedCurrentA: Double,
    val icuKA: Double,
    val icsKA: Double,
    val utilizationPercent: Double,
    val acceptable: Boolean,
    val warnings: List<String>
)

data class TransformerInput(
    val ratingKva: Double,
    val primaryVoltageV: Double,
    val secondaryVoltageV: Double,
    val impedancePercent: Double,
    val frequencyHz: Double = 50.0
)

data class TransformerResult(
    val primaryCurrentA: Double,
    val secondaryCurrentA: Double,
    val shortCircuitCurrentKA: Double,
    val faultMva: Double
)

data class GeneratorInput(
    val ratingKva: Double,
    val voltageV: Double,
    val powerFactor: Double = 0.8,
    val xdPercent: Double = 15.0,
    val xOverR: Double = 10.0
)

data class GeneratorResult(
    val fullLoadCurrentA: Double,
    val initialShortCircuitCurrentKA: Double,
    val faultMva: Double
)

data class MotorInput(
    val powerKw: Double,
    val voltageV: Double,
    val powerFactor: Double = 0.85,
    val efficiency: Double = 0.90,
    val startingMultiplier: Double = 6.0
)

data class MotorResult(
    val fullLoadCurrentA: Double,
    val startingCurrentA: Double,
    val apparentPowerKva: Double
)

data class PumpInput(
    val hydraulicPowerKw: Double,
    val voltageV: Double,
    val powerFactor: Double = 0.85,
    val motorEfficiency: Double = 0.90,
    val pumpEfficiency: Double = 0.80
)

data class PumpResult(
    val motorInputPowerKw: Double,
    val currentA: Double,
    val apparentPowerKva: Double
)

data class SldElement(
    val id: String,
    val name: String,
    val type: SldElementType,
    val x: Float = 0f,
    val y: Float = 0f,
    val powerKw: Double = 0.0,
    val voltageV: Double = 400.0,
    val sourceType: SourceType? = null,
    val parentId: String? = null
)

data class SldConnection(
    val id: String,
    val fromId: String,
    val toId: String,
    val lengthM: Double = 0.0
)

data class SldNetwork(
    val elements: List<SldElement> = emptyList(),
    val connections: List<SldConnection> = emptyList()
)

data class SldElementResult(
    val elementId: String,
    val powerKw: Double,
    val apparentPowerKva: Double,
    val currentA: Double,
    val shortCircuitKA: Double
)

data class SldResult(
    val elements: List<SldElementResult>,
    val totalLoadKw: Double,
    val totalDemandKw: Double,
    val sourceCurrentA: Double,
    val sourceFaultCurrentKA: Double
)

package com.electricalengineeringpro.app.core.model

enum class Phase {
    SINGLE,
    THREE
}

enum class SupplySource {
    UTILITY,
    TRANSFORMER,
    GENERATOR,
    TRANSFORMER_GENERATOR
}

enum class LoadType {
    LIGHTING,
    SOCKET,
    HVAC,
    MOTOR,
    PUMP,
    FIRE_PUMP,
    ELEVATOR,
    MECHANICAL,
    MISCELLANEOUS
}

enum class ConductorMaterial {
    COPPER,
    ALUMINIUM
}

enum class InstallationMethod {
    CONDUIT,
    TRAY,
    LADDER,
    DUCT,
    BURIED,
    FREE_AIR
}

enum class CableInsulation {
    PVC,
    XLPE,
    EPR
}

enum class BreakerType {
    MCB,
    MCCB,
    ACB,
    RCBO,
    RCD
}

data class Project(
    val id: Long = 0,
    val name: String,
    val client: String = "",
    val location: String = "",
    val engineer: String = "Eng. Abdelraouf Elghaty",
    val voltage: Double = 400.0,
    val frequency: Double = 50.0,
    val phase: Phase = Phase.THREE,
    val source: SupplySource = SupplySource.UTILITY
)

data class ElectricalLoad(
    val id: Long = 0,
    val projectId: Long = 0,
    val name: String,
    val type: LoadType,
    val quantity: Int = 1,
    val powerKw: Double,
    val powerFactor: Double = 0.90,
    val efficiency: Double = 1.0,
    val demandFactor: Double = 1.0,
    val diversityFactor: Double = 1.0,
    val voltage: Double = 400.0,
    val phase: Phase = Phase.THREE,
    val startingCurrentMultiplier: Double = 1.0,

    /*
     * Network hierarchy.
     *
     * These fields are optional so existing callers remain compatible.
     */
    val panelName: String = "MDB",
    val feederName: String = "",
    val circuitName: String = ""
)

data class CableInput(
    val designCurrentA: Double,
    val lengthM: Double,
    val voltage: Double,
    val powerFactor: Double,
    val phase: Phase,
    val material: ConductorMaterial = ConductorMaterial.COPPER,
    val insulation: CableInsulation = CableInsulation.XLPE,
    val installationMethod: InstallationMethod = InstallationMethod.TRAY,
    val ambientFactor: Double = 1.0,
    val groupingFactor: Double = 1.0,
    val targetVoltageDropPercent: Double = 3.0
)

data class CableResult(
    val selectedSizeMm2: Double,
    val ampacityA: Double,
    val voltageDropPercent: Double,
    val utilizationPercent: Double,
    val conductorDescription: String
)

data class BreakerInput(
    val designCurrentA: Double,
    val shortCircuitCurrentKA: Double,
    val preferredType: BreakerType = BreakerType.MCCB
)

data class BreakerResult(
    val ratedCurrentA: Double,
    val breakingCapacityKA: Double,
    val type: BreakerType,
    val utilizationPercent: Double
)

data class VoltageDropResult(
    val dropVolts: Double,
    val dropPercent: Double,
    val compliant: Boolean
)

data class TransformerInput(
    val ratingKva: Double,
    val primaryVoltage: Double,
    val secondaryVoltage: Double,
    val impedancePercent: Double
)

data class TransformerResult(
    val primaryCurrentA: Double,
    val secondaryCurrentA: Double,
    val shortCircuitCurrentKA: Double
)

data class MotorInput(
    val powerKw: Double,
    val voltage: Double,
    val powerFactor: Double,
    val efficiency: Double,
    val phase: Phase = Phase.THREE,
    val startingMultiplier: Double = 6.0
)

data class MotorResult(
    val fullLoadCurrentA: Double,
    val startingCurrentA: Double
)

data class PumpInput(
    val flowM3s: Double,
    val headM: Double,
    val pumpEfficiency: Double,
    val motorEfficiency: Double,
    val powerFactor: Double,
    val voltage: Double,
    val phase: Phase = Phase.THREE
)

data class PumpResult(
    val hydraulicPowerKw: Double,
    val motorPowerKw: Double,
    val currentA: Double
)

data class ShortCircuitInput(
    val sourceVoltage: Double,
    val transformerKva: Double,
    val transformerImpedancePercent: Double,
    val sourceShortCircuitMva: Double? = null
)

data class ShortCircuitResult(
    val faultCurrentKA: Double,
    val faultMva: Double
)

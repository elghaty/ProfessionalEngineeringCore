package com.electrical.calculationspro.core

enum class EngineeringPhase {
    DC,
    SINGLE,
    TWO_PHASE,
    THREE
}

enum class EngineeringSourceType {
    UTILITY,
    TRANSFORMER,
    GENERATOR,
    PANEL
}

enum class EngineeringLoadType {
    GENERAL,
    LIGHTING,
    SOCKET,
    MOTOR,
    PUMP,
    HVAC,
    UPS,
    FIRE_PUMP,
    OTHER
}

data class EngineeringLoad(
    val id: String,
    val name: String,
    val loadType: EngineeringLoadType =
        EngineeringLoadType.GENERAL,
    val powerKw: Double,
    val quantity: Int = 1,
    val voltageV: Double = 400.0,
    val powerFactor: Double = 0.90,
    val efficiency: Double = 1.0,
    val demandFactor: Double = 1.0,
    val diversityFactor: Double = 1.0,
    val phase: EngineeringPhase =
        EngineeringPhase.THREE
)

data class EngineeringProject(
    val id: String,
    val name: String,
    val description: String = "",
    val voltageV: Double = 400.0,
    val frequencyHz: Double = 50.0,
    val powerFactor: Double = 0.90,
    val sourceType: EngineeringSourceType =
        EngineeringSourceType.UTILITY,
    val loads: List<EngineeringLoad> = emptyList()
)

data class PowerCalculationResult(
    val activePowerKw: Double,
    val apparentPowerKva: Double,
    val reactivePowerKvar: Double,
    val currentA: Double,
    val powerFactor: Double
)

data class LoadCalculationResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val apparentPowerKva: Double,
    val designCurrentA: Double
)

data class ProjectSummaryResult(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val apparentPowerKva: Double,
    val mainCurrentA: Double,
    val recommendedTransformerKva: Double,
    val recommendedMainBreakerA: Double
)

package com.elghaty.professionalengineeringcore.core.model

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

enum class CableInsulation {
    PVC,
    XLPE,
    EPR
}

enum class InstallationMethod {
    CONDUIT,
    TRAY,
    LADDER,
    DUCT,
    BURIED,
    FREE_AIR
}

enum class BreakerType {
    MCB,
    MCCB,
    ACB,
    RCBO,
    RCD
}

enum class SourceType {
    UTILITY,
    TRANSFORMER,
    GENERATOR,
    PANEL
}

enum class CalculationStatus {
    OK,
    WARNING,
    NOT_COMPLIANT,
    INVALID
}

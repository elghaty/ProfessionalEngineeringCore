package com.elghaty.professionalengineeringcore.core.model

data class CableInput(
    val phase: Phase,
    val voltage: Double,
    val designCurrentA: Double,
    val lengthM: Double,
    val powerFactor: Double = 0.9,
    val material: ConductorMaterial = ConductorMaterial.COPPER,
    val insulation: CableInsulation = CableInsulation.XLPE,
    val installation: InstallationMethod = InstallationMethod.TRAY,
    val ambientFactor: Double = 1.0,
    val groupingFactor: Double = 1.0,
    val targetVoltageDropPercent: Double = 3.0
)

data class CableResult(
    val selectedSizeMm2: Double?,
    val material: ConductorMaterial,
    val correctedAmpacityA: Double?,
    val designCurrentA: Double,
    val voltageDropV: Double?,
    val voltageDropPercent: Double?,
    val ampacityCompliant: Boolean,
    val voltageDropCompliant: Boolean,
    val status: CalculationStatus,
    val message: String = ""
)

package com.elghaty.professionalengineeringcore.core.standards

enum class EngineeringStandard {
    IEC,
    NEC,
    GENERAL_ENGINEERING
}

data class StandardProfile(
    val standard: EngineeringStandard,
    val maxVoltageDropPercent: Double = 3.0,
    val finalCircuitVoltageDropPercent: Double = 3.0,
    val totalVoltageDropPercent: Double = 5.0
)

object StandardProfiles {
    val IEC = StandardProfile(
        standard = EngineeringStandard.IEC
    )

    val NEC = StandardProfile(
        standard = EngineeringStandard.NEC
    )

    val GENERAL = StandardProfile(
        standard = EngineeringStandard.GENERAL_ENGINEERING
    )
}

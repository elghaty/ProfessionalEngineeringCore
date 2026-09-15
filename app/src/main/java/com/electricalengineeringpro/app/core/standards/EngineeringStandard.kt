package com.electricalengineeringpro.app.core.standards

enum class EngineeringStandard {
    IEC,
    BS,
    NEC
}

data class StandardProfile(
    val standard: EngineeringStandard,
    val defaultVoltageDropLighting: Double,
    val defaultVoltageDropPower: Double,
    val defaultPowerFactor: Double
)

object StandardProfiles {

    fun profile(standard: EngineeringStandard): StandardProfile {
        return when (standard) {
            EngineeringStandard.IEC -> StandardProfile(
                standard = standard,
                defaultVoltageDropLighting = 3.0,
                defaultVoltageDropPower = 5.0,
                defaultPowerFactor = 0.90
            )

            EngineeringStandard.BS -> StandardProfile(
                standard = standard,
                defaultVoltageDropLighting = 3.0,
                defaultVoltageDropPower = 5.0,
                defaultPowerFactor = 0.90
            )

            EngineeringStandard.NEC -> StandardProfile(
                standard = standard,
                defaultVoltageDropLighting = 3.0,
                defaultVoltageDropPower = 5.0,
                defaultPowerFactor = 0.90
            )
        }
    }
}

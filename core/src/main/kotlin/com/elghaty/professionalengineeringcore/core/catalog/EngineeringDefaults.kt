package com.elghaty.professionalengineeringcore.core.catalog

import com.elghaty.professionalengineeringcore.core.model.InstallationMethod

object EngineeringDefaults {

    const val DEFAULT_POWER_FACTOR = 0.90
    const val DEFAULT_TRANSFORMER_IMPEDANCE_PERCENT = 6.0
    const val DEFAULT_REACTANCE_OHM_PER_KM = 0.08

    fun installationFactor(method: InstallationMethod): Double =
        when (method) {
            InstallationMethod.CONDUIT -> 0.90
            InstallationMethod.TRAY -> 1.00
            InstallationMethod.LADDER -> 1.05
            InstallationMethod.DUCT -> 0.90
            InstallationMethod.BURIED -> 0.85
            InstallationMethod.FREE_AIR -> 1.10
        }

    fun resistivityOhmMm2PerM(
        copper: Boolean
    ): Double =
        if (copper) 0.0175 else 0.0282
}

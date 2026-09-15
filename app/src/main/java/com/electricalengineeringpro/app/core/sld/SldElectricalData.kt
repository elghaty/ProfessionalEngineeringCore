package com.electricalengineeringpro.app.core.sld

data class SldElectricalData(
    val currentA: Double = 0.0,
    val cableSizeMm2: Double = 0.0,
    val breakerA: Double = 0.0,
    val breakingCapacityKA: Double = 0.0,
    val voltageDropPercent: Double = 0.0,
    val shortCircuitKA: Double = 0.0
) {

    fun asMap(): Map<String, String> =
        buildMap {

            if (currentA > 0.0) {
                put(
                    "Current",
                    "%.1f A".format(currentA)
                )
            }

            if (cableSizeMm2 > 0.0) {
                put(
                    "Cable",
                    "%.1f mm²".format(cableSizeMm2)
                )
            }

            if (breakerA > 0.0) {
                put(
                    "Breaker",
                    "%.0f A".format(breakerA)
                )
            }

            if (breakingCapacityKA > 0.0) {
                put(
                    "Icu",
                    "%.0f kA".format(breakingCapacityKA)
                )
            }

            if (voltageDropPercent > 0.0) {
                put(
                    "V-Drop",
                    "%.2f %%".format(voltageDropPercent)
                )
            }

            if (shortCircuitKA > 0.0) {
                put(
                    "Isc",
                    "%.2f kA".format(shortCircuitKA)
                )
            }
        }
}

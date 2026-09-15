package com.electricalengineeringpro.app.core.model

data class DesignCriteria(
    val standard: String = "IEC",
    val systemVoltage: Double = 400.0,
    val frequencyHz: Double = 50.0,
    val maximumLightingVoltageDropPercent: Double = 3.0,
    val maximumPowerVoltageDropPercent: Double = 5.0,
    val minimumPowerFactor: Double = 0.90,
    val conductorMaterial: ConductorMaterial = ConductorMaterial.COPPER,
    val cableInsulation: CableInsulation = CableInsulation.XLPE
)

data class DesignSummary(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designLoadKw: Double,
    val totalCurrentA: Double,
    val recommendedMainBreakerA: Double,
    val recommendedTransformerKva: Double
)

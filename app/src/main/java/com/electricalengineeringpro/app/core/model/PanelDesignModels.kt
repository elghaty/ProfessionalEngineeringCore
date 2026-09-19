package com.electricalengineeringpro.app.core.model

enum class PanelSourceType {
    TRANSFORMER,
    GENERATOR,
    OTHER_PANEL
}

enum class CableType(
    val material: ConductorMaterial,
    val insulation: CableInsulation
) {
    XLPE_COPPER(
        ConductorMaterial.COPPER,
        CableInsulation.XLPE
    ),

    XLPE_ALUMINIUM(
        ConductorMaterial.ALUMINIUM,
        CableInsulation.XLPE
    ),

    PVC_COPPER(
        ConductorMaterial.COPPER,
        CableInsulation.PVC
    ),

    PVC_ALUMINIUM(
        ConductorMaterial.ALUMINIUM,
        CableInsulation.PVC
    )
}

data class PanelDesignInput(
    val panelName: String = "MDB",

    val loadKw: Double,

    val powerFactor: Double = 0.90,

    val voltageV: Double = 400.0,

    val lengthM: Double,

    val sourceType: PanelSourceType,

    /*
     * Transformer / Generator source data.
     */
    val sourceKva: Double = 0.0,

    /*
     * For transformer:
     * impedance %
     *
     * For generator:
     * Xd'' %
     */
    val sourceImpedancePercent: Double = 6.0,

    /*
     * Used when source is another panel.
     */
    val upstreamShortCircuitKA: Double = 0.0,

    val cableType: CableType =
        CableType.XLPE_COPPER,

    val installationMethod: InstallationMethod =
        InstallationMethod.TRAY,

    val ambientFactor: Double = 1.0,

    val groupingFactor: Double = 1.0,

    val targetVoltageDropPercent: Double = 3.0
)

data class PanelDesignResult(

    val panelName: String,

    val sourceType: PanelSourceType,

    val loadKw: Double,

    val powerFactor: Double,

    val voltageV: Double,

    /*
     * Load / feeder current.
     */
    val designCurrentA: Double,

    /*
     * Required source capacity.
     */
    val sourceRequiredKva: Double,

    /*
     * Recommended source capacity.
     */
    val sourceRecommendedKva: Double,

    /*
     * Source rated current.
     */
    val sourceCurrentA: Double,

    /*
     * Cable.
     */
    val cableSizeMm2: Double,

    val cableAmpacityA: Double,

    val cableDescription: String,

    val voltageDropPercent: Double,

    /*
     * Protection.
     */
    val breakerRatingA: Double,

    val breakerBreakingCapacityKA: Double,

    /*
     * Fault current at panel.
     */
    val shortCircuitKA: Double,

    val faultMva: Double,

    /*
     * Generated SLD.
     */
    val sld: com.electricalengineeringpro.app.core.sld.SingleLineDiagram
)

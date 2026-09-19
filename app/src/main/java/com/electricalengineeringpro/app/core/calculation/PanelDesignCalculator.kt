package com.electricalengineeringpro.app.core.calculation

import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.ConductorMaterial
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.InstallationMethod
import com.electricalengineeringpro.app.core.model.PanelDesignInput
import com.electricalengineeringpro.app.core.model.PanelDesignResult
import com.electricalengineeringpro.app.core.model.PanelSourceType
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.CableInsulation
import com.electricalengineeringpro.app.core.sld.SldConnection
import com.electricalengineeringpro.app.core.sld.SldElectricalData
import com.electricalengineeringpro.app.core.sld.SldNode
import com.electricalengineeringpro.app.core.sld.SldSymbolType
import com.electricalengineeringpro.app.core.sld.SingleLineDiagram
import kotlin.math.sqrt

/**
 * Complete feeder / panel design orchestrator.
 *
 * Flow:
 *
 * LOAD
 *   ↓
 * DESIGN CURRENT
 *   ↓
 * SOURCE
 *   ↓
 * CABLE
 *   ↓
 * VOLTAGE DROP
 *   ↓
 * SHORT CIRCUIT
 *   ↓
 * BREAKER
 *   ↓
 * SOURCE CAPACITY
 *   ↓
 * SLD
 *
 * This class coordinates the existing engineering calculators.
 * It does NOT create another calculation core.
 */
class PanelDesignCalculator(

    private val cableCalculator:
        CableCalculator =
        CableCalculator(),

    private val breakerCalculator:
        BreakerSelectionCalculator =
        BreakerSelectionCalculator(),

    private val transformerSizingCalculator:
        TransformerSizingCalculator =
        TransformerSizingCalculator()
) {

    fun calculate(
        input: PanelDesignInput
    ): PanelDesignResult {

        validateInput(input)

        /*
         * ---------------------------------------------------------
         * 1. DESIGN CURRENT
         * ---------------------------------------------------------
         */

        val designCurrentA =
            input.loadKw * 1000.0 /
                (
                    sqrt(3.0) *
                        input.voltageV *
                        input.powerFactor
                )

        /*
         * ---------------------------------------------------------
         * 2. REQUIRED SOURCE CAPACITY
         * ---------------------------------------------------------
         */

        val sourceRequiredKva =
            input.loadKw /
                input.powerFactor

        /*
         * Recommended transformer/source capacity.
         *
         * The existing TransformerSizingCalculator remains
         * the single sizing engine.
         */

        val sourceRecommendedKva =
            transformerSizingCalculator
                .calculate(
                    TransformerSizingInput(
                        designLoadKW =
                            input.loadKw,

                        powerFactor =
                            input.powerFactor,

                        spareCapacityFactor =
                            1.15
                    )
                )
                .recommendedRatingKVA

        /*
         * ---------------------------------------------------------
         * 3. SOURCE CURRENT
         * ---------------------------------------------------------
         */

        val sourceCurrentA =
            when (input.sourceType) {

                PanelSourceType.TRANSFORMER,
                PanelSourceType.GENERATOR -> {

                    input.sourceKva * 1000.0 /
                        (
                            sqrt(3.0) *
                                input.voltageV
                        )
                }

                PanelSourceType.OTHER_PANEL -> {

                    designCurrentA
                }
            }

        /*
         * ---------------------------------------------------------
         * 4. CABLE
         * ---------------------------------------------------------
         */

        val cableMaterial =
            input.cableType.material

        val cableInsulation =
            input.cableType.insulation

        val cableResult =
            cableCalculator.calculate(
                CableInput(
                    designCurrentA =
                        designCurrentA,

                    lengthM =
                        input.lengthM,

                    voltage =
                        input.voltageV,

                    powerFactor =
                        input.powerFactor,

                    phase =
                        Phase.THREE,

                    material =
                        cableMaterial,

                    insulation =
                        cableInsulation,

                    installationMethod =
                        input.installationMethod,

                    ambientFactor =
                        input.ambientFactor,

                    groupingFactor =
                        input.groupingFactor,

                    targetVoltageDropPercent =
                        input.targetVoltageDropPercent
                )
            )

        /*
         * ---------------------------------------------------------
         * 5. SOURCE SHORT CIRCUIT
         * ---------------------------------------------------------
         */

        val sourceShortCircuitKA =
            when (input.sourceType) {

                PanelSourceType.TRANSFORMER,
                PanelSourceType.GENERATOR -> {

                    val sourceMva =
                        input.sourceKva /
                            (
                                input.sourceImpedancePercent /
                                    100.0
                            ) /
                            1000.0

                    sourceMva *
                        1000.0 /
                        (
                            sqrt(3.0) *
                                input.voltageV
                        )
                }

                PanelSourceType.OTHER_PANEL -> {

                    input.upstreamShortCircuitKA
                }
            }

        /*
         * ---------------------------------------------------------
         * 6. CABLE IMPEDANCE + FAULT CURRENT
         * ---------------------------------------------------------
         */

        val shortCircuitKA =
            calculateFaultAtPanel(
                sourceFaultCurrentKA =
                    sourceShortCircuitKA,

                cableSizeMm2 =
                    cableResult.selectedSizeMm2,

                lengthM =
                    input.lengthM,

                voltageV =
                    input.voltageV,

                material =
                    cableMaterial
            )

        /*
         * ---------------------------------------------------------
         * 7. BREAKER
         * ---------------------------------------------------------
         */

        val breakerResult =
            breakerCalculator.calculate(
                BreakerSelectionInput(
                    loadCurrentA =
                        designCurrentA,

                    shortCircuitKA =
                        shortCircuitKA,

                    designMarginFactor =
                        1.00
                )
            )

        /*
         * ---------------------------------------------------------
         * 8. FAULT MVA
         * ---------------------------------------------------------
         */

        val faultMva =
            shortCircuitKA *
                sqrt(3.0) *
                input.voltageV /
                1000.0

        /*
         * ---------------------------------------------------------
         * 9. SLD
         * ---------------------------------------------------------
         */

        val sld =
            buildSld(
                input =
                    input,

                designCurrentA =
                    designCurrentA,

                sourceRecommendedKva =
                    sourceRecommendedKva,

                cableSizeMm2 =
                    cableResult.selectedSizeMm2,

                breakerRatingA =
                    breakerResult.recommendedRatingA,

                breakerIcuKA =
                    breakerResult.recommendedBreakingCapacityKA,

                shortCircuitKA =
                    shortCircuitKA,

                voltageDropPercent =
                    cableResult.voltageDropPercent
            )

        return PanelDesignResult(

            panelName =
                input.panelName,

            sourceType =
                input.sourceType,

            loadKw =
                input.loadKw,

            powerFactor =
                input.powerFactor,

            voltageV =
                input.voltageV,

            designCurrentA =
                designCurrentA,

            sourceRequiredKva =
                sourceRequiredKva,

            sourceRecommendedKva =
                sourceRecommendedKva,

            sourceCurrentA =
                sourceCurrentA,

            cableSizeMm2 =
                cableResult.selectedSizeMm2,

            cableAmpacityA =
                cableResult.ampacityA,

            cableDescription =
                cableResult.conductorDescription,

            voltageDropPercent =
                cableResult.voltageDropPercent,

            breakerRatingA =
                breakerResult.recommendedRatingA,

            breakerBreakingCapacityKA =
                breakerResult.recommendedBreakingCapacityKA,

            shortCircuitKA =
                shortCircuitKA,

            faultMva =
                faultMva,

            sld =
                sld
        )
    }

    private fun validateInput(
        input: PanelDesignInput
    ) {

        require(
            input.panelName.isNotBlank()
        ) {
            "Panel name must not be blank."
        }

        require(
            input.loadKw > 0.0
        ) {
            "Panel load must be greater than zero."
        }

        require(
            input.powerFactor in 0.01..1.0
        ) {
            "Power factor must be between 0.01 and 1.0."
        }

        require(
            input.voltageV > 0.0
        ) {
            "Voltage must be greater than zero."
        }

        require(
            input.lengthM >= 0.0
        ) {
            "Cable length cannot be negative."
        }

        require(
            input.ambientFactor > 0.0
        ) {
            "Ambient factor must be greater than zero."
        }

        require(
            input.groupingFactor > 0.0
        ) {
            "Grouping factor must be greater than zero."
        }

        require(
            input.targetVoltageDropPercent > 0.0
        ) {
            "Voltage-drop limit must be greater than zero."
        }

        when (input.sourceType) {

            PanelSourceType.TRANSFORMER,
            PanelSourceType.GENERATOR -> {

                require(
                    input.sourceKva > 0.0
                ) {
                    "Source rating must be greater than zero."
                }

                require(
                    input.sourceImpedancePercent > 0.0
                ) {
                    "Source impedance must be greater than zero."
                }
            }

            PanelSourceType.OTHER_PANEL -> {

                require(
                    input.upstreamShortCircuitKA > 0.0
                ) {
                    "Upstream short-circuit current must be greater than zero."
                }
            }
        }
    }

    /**
     * Calculates fault current at the panel after adding
     * feeder impedance to the source impedance.
     *
     * This is a simplified engineering model and should later
     * be upgraded to the selected standard / IEC calculation
     * method with complete R/X data.
     */
    private fun calculateFaultAtPanel(
        sourceFaultCurrentKA: Double,
        cableSizeMm2: Double,
        lengthM: Double,
        voltageV: Double,
        material: ConductorMaterial
    ): Double {

        require(
            sourceFaultCurrentKA > 0.0
        ) {
            "Source short-circuit current must be greater than zero."
        }

        if (lengthM <= 0.0) {
            return sourceFaultCurrentKA
        }

        val resistivity =
            when (material) {

                ConductorMaterial.COPPER ->
                    0.0175

                ConductorMaterial.ALUMINIUM ->
                    0.0282
            }

        /*
         * Cable resistance.
         */
        val resistanceOhm =
            resistivity *
                lengthM /
                cableSizeMm2

        /*
         * Approximate LV cable reactance.
         */
        val reactanceOhm =
            0.08 *
                lengthM /
                1000.0

        val cableImpedanceOhm =
            sqrt(
                resistanceOhm *
                    resistanceOhm +
                    reactanceOhm *
                    reactanceOhm
            )

        /*
         * Convert source fault current to source impedance.
         */
        val sourceImpedanceOhm =
            voltageV /
                (
                    sqrt(3.0) *
                        sourceFaultCurrentKA *
                        1000.0
                )

        val totalImpedance =
            sourceImpedanceOhm +
                cableImpedanceOhm

        if (totalImpedance <= 0.0) {
            return sourceFaultCurrentKA
        }

        return voltageV /
            (
                sqrt(3.0) *
                    totalImpedance
            ) /
            1000.0
    }

    private fun buildSld(
        input: PanelDesignInput,
        designCurrentA: Double,
        sourceRecommendedKva: Double,
        cableSizeMm2: Double,
        breakerRatingA: Double,
        breakerIcuKA: Double,
        shortCircuitKA: Double,
        voltageDropPercent: Double
    ): SingleLineDiagram {

        val sourceId =
            "SOURCE"

        val breakerId =
            "MAIN_BREAKER"

        val cableId =
            "FEEDER_CABLE"

        val panelId =
            "PANEL"

        val loadId =
            "PANEL_LOAD"

        val sourceSymbol =
            when (input.sourceType) {

                PanelSourceType.TRANSFORMER ->
                    SldSymbolType.TRANSFORMER

                PanelSourceType.GENERATOR ->
                    SldSymbolType.GENERATOR

                PanelSourceType.OTHER_PANEL ->
                    SldSymbolType.PANEL
            }

        val sourceName =
            when (input.sourceType) {

                PanelSourceType.TRANSFORMER ->
                    "TRANSFORMER"

                PanelSourceType.GENERATOR ->
                    "GENERATOR"

                PanelSourceType.OTHER_PANEL ->
                    "UPSTREAM PANEL"
            }

        val sourceKva =
            if (
                input.sourceType ==
                PanelSourceType.OTHER_PANEL
            ) {
                sourceRecommendedKva
            } else {
                input.sourceKva
            }

        val sourceData =
            SldElectricalData(
                currentA =
                    if (
                        input.sourceType ==
                        PanelSourceType.OTHER_PANEL
                    ) {
                        designCurrentA
                    } else {
                        sourceKva *
                            1000.0 /
                            (
                                sqrt(3.0) *
                                    input.voltageV
                            )
                    },

                shortCircuitKA =
                    shortCircuitKA
            ).asMap() +
                mapOf(
                    "Rating" =
                        "%.0f kVA".format(
                            sourceKva
                        ),

                    "Voltage" =
                        "%.0f V".format(
                            input.voltageV
                        )
                )

        val breakerData =
            SldElectricalData(
                currentA =
                    designCurrentA,

                breakerA =
                    breakerRatingA,

                breakingCapacityKA =
                    breakerIcuKA,

                shortCircuitKA =
                    shortCircuitKA
            ).asMap()

        val cableData =
            SldElectricalData(
                currentA =
                    designCurrentA,

                cableSizeMm2 =
                    cableSizeMm2,

                voltageDropPercent =
                    voltageDropPercent
            ).asMap() +
                mapOf(
                    "Length" =
                        "%.0f m".format(
                            input.lengthM
                        ),

                    "Cable Type" =
                        input.cableType.name
                )

        val panelData =
            SldElectricalData(
                currentA =
                    designCurrentA,

                shortCircuitKA =
                    shortCircuitKA
            ).asMap() +
                mapOf(
                    "Load" =
                        "%.1f kW".format(
                            input.loadKw
                        ),

                    "PF" =
                        "%.2f".format(
                            input.powerFactor
                        )
                )

        val loadData =
            mapOf(
                "Power" =
                    "%.1f kW".format(
                        input.loadKw
                    ),

                "Current" =
                    "%.1f A".format(
                        designCurrentA
                    )
            )

        val nodes =
            listOf(

                SldNode(
                    id = sourceId,
                    name = sourceName,
                    type = sourceSymbol,
                    x = 0f,
                    y = 0f,
                    electricalData =
                        sourceData
                ),

                SldNode(
                    id = breakerId,
                    name = "MAIN CB",
                    type = SldSymbolType.BREAKER,
                    x = 0f,
                    y = 160f,
                    electricalData =
                        breakerData
                ),

                SldNode(
                    id = cableId,
                    name = "FEEDER",
                    type = SldSymbolType.CABLE,
                    x = 0f,
                    y = 320f,
                    electricalData =
                        cableData
                ),

                SldNode(
                    id = panelId,
                    name = input.panelName,
                    type = SldSymbolType.MAIN_SWITCHBOARD,
                    x = 0f,
                    y = 480f,
                    electricalData =
                        panelData
                ),

                SldNode(
                    id = loadId,
                    name = "PANEL LOAD",
                    type = SldSymbolType.LOAD,
                    x = 0f,
                    y = 640f,
                    electricalData =
                        loadData
                )
            )

        val connections =
            listOf(

                SldConnection(
                    fromId = sourceId,
                    toId = breakerId,
                    label = "SOURCE"
                ),

                SldConnection(
                    fromId = breakerId,
                    toId = cableId,
                    label =
                        "%.0f A".format(
                            breakerRatingA
                        )
                ),

                SldConnection(
                    fromId = cableId,
                    toId = panelId,
                    label =
                        "%.0f mm² / %.0f m".format(
                            cableSizeMm2,
                            input.lengthM
                        )
                ),

                SldConnection(
                    fromId = panelId,
                    toId = loadId,
                    label =
                        "%.1f kW".format(
                            input.loadKw
                        )
                )
            )

        return SingleLineDiagram(
            nodes = nodes,
            connections = connections
        )
    }
}

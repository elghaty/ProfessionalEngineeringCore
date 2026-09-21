package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import com.electricalengineeringpro.app.core.model.CableType
import com.electricalengineeringpro.app.core.model.InstallationMethod
import com.electricalengineeringpro.app.core.model.PanelDesignInput
import com.electricalengineeringpro.app.core.model.PanelDesignResult
import com.electricalengineeringpro.app.core.model.PanelSourceType
import com.electricalengineeringpro.app.core.ui.SldDiagramView

class MainActivity : Activity() {

    private lateinit var content: LinearLayout
    private lateinit var resultContainer: LinearLayout
    private lateinit var sourceDataContainer: LinearLayout

    private lateinit var panelNameInput: EditText
    private lateinit var loadInput: EditText
    private lateinit var pfInput: EditText
    private lateinit var voltageInput: EditText
    private lateinit var lengthInput: EditText

    private lateinit var sourceSpinner: Spinner
    private lateinit var sourceKvaInput: EditText
    private lateinit var sourceImpedanceInput: EditText
    private lateinit var upstreamIscInput: EditText

    private lateinit var cableSpinner: Spinner
    private lateinit var installationSpinner

    private lateinit var ambientInput: EditText
    private lateinit var groupingInput: EditText
    private lateinit var voltageDropInput: EditText

    /*
     * ProfessionalEngineeringCore is the ONLY engineering
     * calculation interface used by the Android UI.
     */
    private val core: ProfessionalEngineeringCore
        get() = ProfessionalEngineeringCore.instance

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        try {
            buildUi()
        } catch (error: Throwable) {
            showStartupError(error)
        }
    }

    private fun showStartupError(
        error: Throwable
    ) {

        val root =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(20),
                    dp(24),
                    dp(20),
                    dp(24)
                )

                setBackgroundColor(
                    Color.rgb(
                        244,
                        247,
                        251
                    )
                )
            }

        root.addView(
            TextView(this).apply {

                text =
                    "PROFESSIONAL ENGINEERING"

                textSize = 20f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    Color.rgb(
                        11,
                        41,
                        66
                    )
                )
            }
        )

        root.addView(
            TextView(this).apply {

                text =
                    "STARTUP DIAGNOSTIC"

                textSize = 17f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    Color.rgb(
                        180,
                        40,
                        40
                    )
                )

                setPadding(
                    0,
                    dp(16),
                    0,
                    dp(10)
                )
            }
        )

        root.addView(
            TextView(this).apply {

                text =
                    buildString {

                        append(
                            "Application startup failed.\n\n"
                        )

                        append(
                            "Exception:\n"
                        )

                        append(
                            error.javaClass.name
                        )

                        append(
                            "\n\nMessage:\n"
                        )

                        append(
                            error.message
                                ?: "No message"
                        )

                        append(
                            "\n\nCause:\n"
                        )

                        append(
                            error.cause?.toString()
                                ?: "None"
                        )
                    }

                textSize = 14f

                setTextColor(
                    Color.DKGRAY
                )
            }
        )

        root.addView(
            Button(this).apply {

                text = "RETRY"

                setOnClickListener {

                    try {
                        buildUi()
                    } catch (retryError: Throwable) {
                        showStartupError(
                            retryError
                        )
                    }
                }
            }
        )

        setContentView(root)
    }

    private fun buildUi() {

        val root =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setBackgroundColor(
                    Color.rgb(
                        244,
                        247,
                        251
                    )
                )
            }

        root.addView(
            createHeader()
        )

        val scrollView =
            ScrollView(this)

        content =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(16),
                    dp(14),
                    dp(16),
                    dp(40)
                )
            }

        scrollView.addView(
            content
        )

        root.addView(
            scrollView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)

        buildPanelDesignScreen()
    }

    private fun createHeader(): View {

        return LinearLayout(this).apply {

            orientation =
                LinearLayout.VERTICAL

            setPadding(
                dp(20),
                dp(14),
                dp(20),
                dp(14)
            )

            setBackgroundColor(
                Color.rgb(
                    11,
                    41,
                    66
                )
            )

            addView(
                TextView(this@MainActivity).apply {

                    text =
                        "PROFESSIONAL ENGINEERING"

                    textSize = 20f

                    typeface =
                        Typeface.DEFAULT_BOLD

                    setTextColor(
                        Color.WHITE
                    )
                }
            )

            addView(
                TextView(this@MainActivity).apply {

                    text =
                        "Electrical Design & Calculation System"

                    textSize = 12f

                    setTextColor(
                        Color.rgb(
                            210,
                            225,
                            235
                        )
                    )

                    setPadding(
                        0,
                        dp(4),
                        0,
                        0
                    )
                }
            )
        }
    }

    private fun buildPanelDesignScreen() {

        content.removeAllViews()

        addSectionTitle(
            "PANEL DESIGN",
            "Enter the electrical design data."
        )

        panelNameInput =
            addEdit(
                "Panel Name",
                "MDB-01"
            )

        loadInput =
            addEdit(
                "Panel Load (kW)",
                "350",
                true
            )

        pfInput =
            addEdit(
                "Power Factor",
                "0.90",
                true
            )

        voltageInput =
            addEdit(
                "System Voltage (V)",
                "400",
                true
            )

        lengthInput =
            addEdit(
                "Feeder Cable Length (m)",
                "50",
                true
            )

        addSectionTitle(
            "SOURCE OF SUPPLY",
            "Select the source feeding this panel."
        )

        sourceSpinner =
            addSpinner(
                "Source Type",
                listOf(
                    "Transformer",
                    "Generator",
                    "Another Panel"
                )
            )

        sourceDataContainer =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL
            }

        content.addView(
            sourceDataContainer
        )

        sourceKvaInput =
            addDynamicEdit(
                "Source Rating (kVA)",
                "630"
            )

        sourceImpedanceInput =
            addDynamicEdit(
                "Transformer Impedance / Generator Xd'' (%)",
                "6.0"
            )

        upstreamIscInput =
            addDynamicEdit(
                "Upstream Panel Short Circuit (kA)",
                "25"
            )

        sourceSpinner.onItemSelectedListener =
            object :
                AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    updateSourceFields(
                        position
                    )
                }
            }

        updateSourceFields(
            sourceSpinner.selectedItemPosition
        )

        addSectionTitle(
            "FEEDER CABLE",
            "Select cable construction and installation method."
        )

        cableSpinner =
            addSpinner(
                "Cable Type",
                listOf(
                    "XLPE Copper",
                    "XLPE Aluminium",
                    "PVC Copper",
                    "PVC Aluminium"
                )
            )

        val installationValues =
            InstallationMethod
                .values()
                .map { method ->
                    readable(
                        method.name
                    )
                }

        installationSpinner =
            addSpinner(
                "Installation Method",
                installationValues
            )

        ambientInput =
            addEdit(
                "Ambient Correction Factor",
                "1.00",
                true
            )

        groupingInput =
            addEdit(
                "Grouping Correction Factor",
                "1.00",
                true
            )

        voltageDropInput =
            addEdit(
                "Maximum Voltage Drop (%)",
                "3.00",
                true
            )

        val calculateButton =
            Button(this).apply {

                text =
                    "CALCULATE COMPLETE DESIGN"

                textSize = 15f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    Color.WHITE
                )

                setBackgroundColor(
                    Color.rgb(
                        21,
                        101,
                        192
                    )
                )

                setOnClickListener {
                    calculateDesign()
                }
            }

        content.addView(
            calculateButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(58)
            ).apply {

                setMargins(
                    0,
                    dp(18),
                    0,
                    dp(14)
                )
            }
        )

        resultContainer =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL
            }

        content.addView(
            resultContainer
        )
    }

    private fun updateSourceFields(
        position: Int
    ) {

        if (!::sourceKvaInput.isInitialized) {
            return
        }

        if (!::sourceImpedanceInput.isInitialized) {
            return
        }

        if (!::upstreamIscInput.isInitialized) {
            return
        }

        val anotherPanel =
            position ==
                SOURCE_ANOTHER_PANEL

        sourceKvaInput.visibility =
            if (anotherPanel) {
                View.GONE
            } else {
                View.VISIBLE
            }

        sourceImpedanceInput.visibility =
            if (anotherPanel) {
                View.GONE
            } else {
                View.VISIBLE
            }

        upstreamIscInput.visibility =
            if (anotherPanel) {
                View.VISIBLE
            } else {
                View.GONE
            }

        sourceKvaInput.hint =
            if (
                position ==
                    SOURCE_TRANSFORMER
            ) {
                "Transformer Rating (kVA)"
            } else {
                "Generator Rating (kVA)"
            }

        sourceImpedanceInput.hint =
            if (
                position ==
                    SOURCE_TRANSFORMER
            ) {
                "Transformer Impedance (%)"
            } else {
                "Generator Xd'' (%)"
            }
    }

    private fun calculateDesign() {

        if (!::resultContainer.isInitialized) {
            return
        }

        resultContainer.removeAllViews()

        try {

            val input =
                createPanelDesignInput()

            /*
             * UI calls ONLY the facade.
             *
             * No calculator is called directly.
             */
            val result =
                core.calculatePanelDesign(
                    input
                )

            showResult(
                result
            )

        } catch (error: Throwable) {

            addResultCard(
                "DESIGN ERROR",
                error.message
                    ?: "Unable to complete the design."
            )
        }
    }

    private fun createPanelDesignInput():
        PanelDesignInput {

        val panelName =
            panelNameInput.text
                .toString()
                .trim()

        require(
            panelName.isNotEmpty()
        ) {
            "Panel Name must not be empty."
        }

        return PanelDesignInput(

            panelName =
                panelName,

            loadKw =
                readDouble(
                    loadInput,
                    "Panel Load"
                ),

            powerFactor =
                readDouble(
                    pfInput,
                    "Power Factor"
                ),

            voltageV =
                readDouble(
                    voltageInput,
                    "System Voltage"
                ),

            lengthM =
                readDouble(
                    lengthInput,
                    "Cable Length"
                ),

            sourceType =
                selectedSourceType(),

            sourceKva =
                readDoubleOrZero(
                    sourceKvaInput
                ),

            sourceImpedancePercent =
                readDoubleOrZero(
                    sourceImpedanceInput
                ),

            upstreamShortCircuitKA =
                readDoubleOrZero(
                    upstreamIscInput
                ),

            cableType =
                selectedCableType(),

            installationMethod =
                selectedInstallationMethod(),

            ambientFactor =
                readDouble(
                    ambientInput,
                    "Ambient Correction Factor"
                ),

            groupingFactor =
                readDouble(
                    groupingInput,
                    "Grouping Correction Factor"
                ),

            targetVoltageDropPercent =
                readDouble(
                    voltageDropInput,
                    "Maximum Voltage Drop"
                )
        )
    }

    private fun selectedSourceType():
        PanelSourceType {

        return when (
            sourceSpinner.selectedItemPosition
        ) {

            SOURCE_TRANSFORMER ->
                PanelSourceType.TRANSFORMER

            SOURCE_GENERATOR ->
                PanelSourceType.GENERATOR

            SOURCE_ANOTHER_PANEL ->
                PanelSourceType.OTHER_PANEL

            else ->
                PanelSourceType.TRANSFORMER
        }
    }

    private fun selectedCableType():
        CableType {

        return when (
            cableSpinner.selectedItemPosition
        ) {

            CABLE_XLPE_COPPER ->
                CableType.XLPE_COPPER

            CABLE_XLPE_ALUMINIUM ->
                CableType.XLPE_ALUMINIUM

            CABLE_PVC_COPPER ->
                CableType.PVC_COPPER

            CABLE_PVC_ALUMINIUM ->
                CableType.PVC_ALUMINIUM

            else ->
                CableType.XLPE_COPPER
        }
    }

    private fun selectedInstallationMethod():
        InstallationMethod {

        val methods =
            InstallationMethod.values()

        require(
            methods.isNotEmpty()
        ) {
            "No installation methods are defined."
        }

        val position =
            installationSpinner
                .selectedItemPosition
                .coerceIn(
                    0,
                    methods.lastIndex
                )

        return methods[position]
    }

    private fun showResult(
        result: PanelDesignResult
    ) {

        addSectionTitle(
            "DESIGN RESULTS",
            "Calculated by ProfessionalEngineeringCore."
        )

        addResultCard(
            "LOAD",
            """
            Panel: ${result.panelName}

            Load: ${result.loadKw} kW

            Power Factor: ${result.powerFactor}

            Design Current: ${result.designCurrentA} A

            Voltage: ${result.voltageV} V
            """.trimIndent()
        )

        addResultCard(
            "SOURCE",
            """
            Source: ${sourceName(result.sourceType)}

            Required Capacity: ${result.sourceRequiredKva} kVA

            Recommended Capacity: ${result.sourceRecommendedKva} kVA

            Source Current: ${result.sourceCurrentA} A
            """.trimIndent()
        )

        addResultCard(
            "FEEDER CABLE",
            """
            Cable: ${result.cableDescription}

            Selected Size: ${result.cableSizeMm2} mm²

            Ampacity: ${result.cableAmpacityA} A

            Voltage Drop: ${result.voltageDropPercent} %
            """.trimIndent()
        )

        addResultCard(
            "PROTECTION",
            """
            Main Breaker: ${result.breakerRatingA} A

            Breaking Capacity:
            ${result.breakerBreakingCapacityKA} kA

            Design Current:
            ${result.designCurrentA} A
            """.trimIndent()
        )

        addResultCard(
            "SHORT CIRCUIT",
            """
            Panel Short Circuit:
            ${result.shortCircuitKA} kA

            Fault Level:
            ${result.faultMva} MVA
            """.trimIndent()
        )

        addSectionTitle(
            "SINGLE LINE DIAGRAM",
            "SLD generated by ProfessionalEngineeringCore."
        )

        try {

            val sldView =
                SldDiagramView(this)

            sldView.setDiagram(
                result.sld
            )

            resultContainer.addView(
                sldView,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(400)
                ).apply {

                    setMargins(
                        0,
                        dp(8),
                        0,
                        dp(20)
                    )
                }
            )

        } catch (error: Throwable) {

            addResultCard(
                "SLD ERROR",
                error.message
                    ?: "Unable to display SLD."
            )
        }
    }

    private fun sourceName(
        sourceType: PanelSourceType
    ): String {

        return when (sourceType) {

            PanelSourceType.TRANSFORMER ->
                "Transformer"

            PanelSourceType.GENERATOR ->
                "Generator"

            PanelSourceType.OTHER_PANEL ->
                "Another Panel"
        }
    }

    private fun addSectionTitle(
        title: String,
        subtitle: String
    ) {

        content.addView(
            TextView(this).apply {

                text = title

                textSize = 18f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    Color.rgb(
                        11,
                        65,
                        105
                    )
                )

                setPadding(
                    dp(4),
                    dp(14),
                    dp(4),
                    dp(3)
                )
            }
        )

        content.addView(
            TextView(this).apply {

                text = subtitle

                textSize = 11f

                setTextColor(
                    Color.rgb(
                        90,
                        105,
                        120
                    )
                )

                setPadding(
                    dp(4),
                    0,
                    dp(4),
                    dp(8)
                )
            }
        )
    }

    private fun addEdit(
        label: String,
        defaultValue: String,
        numeric: Boolean = false
    ): EditText {

        val field =
            EditText(this).apply {

                hint = label

                setText(
                    defaultValue
                )

                textSize = 14f

                setSingleLine(true)

                setPadding(
                    dp(12),
                    dp(4),
                    dp(12),
                    dp(4)
                )

                if (numeric) {

                    inputType =
                        InputType.TYPE_CLASS_NUMBER or
                            InputType.TYPE_NUMBER_FLAG_DECIMAL
                }
            }

        content.addView(
            field,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(55)
            ).apply {

                setMargins(
                    0,
                    dp(3),
                    0,
                    dp(3)
                )
            }
        )

        return field
    }

    private fun addDynamicEdit(
        label: String,
        defaultValue: String
    ): EditText {

        val field =
            EditText(this).apply {

                hint = label

                setText(
                    defaultValue
                )

                textSize = 14f

                setSingleLine(true)

                inputType =
                    InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL

                setPadding(
                    dp(12),
                    dp(4),
                    dp(12),
                    dp(4)
                )
            }

        sourceDataContainer.addView(
            field,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(55)
            ).apply {

                setMargins(
                    0,
                    dp(3),
                    0,
                    dp(3)
                )
            }
        )

        return field
    }

    private fun addSpinner(
        label: String,
        values: List<String>
    ): Spinner {

        require(
            values.isNotEmpty()
        ) {
            "$label has no available values."
        }

        val spinner =
            Spinner(this)

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                values
            )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter =
            adapter

        spinner.contentDescription =
            label

        content.addView(
            spinner,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(55)
            ).apply {

                setMargins(
                    0,
                    dp(3),
                    0,
                    dp(3)
                )
            }
        )

        return spinner
    }

    private fun addResultCard(
        title: String,
        text: String
    ) {

        if (!::resultContainer.isInitialized) {
            return
        }

        val card =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setBackgroundColor(
                    Color.WHITE
                )

                setPadding(
                    dp(15),
                    dp(12),
                    dp(15),
                    dp(12)
                )
            }

        card.addView(
            TextView(this).apply {

                this.text =
                    title

                textSize = 14f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    Color.rgb(
                        15,
                        75,
                        120
                    )
                )
            }
        )

        card.addView(
            TextView(this).apply {

                this.text =
                    text

                textSize = 13f

                setTextColor(
                    Color.rgb(
                        45,
                        55,
                        65
                    )
                )

                setPadding(
                    0,
                    dp(7),
                    0,
                    0
                )
            }
        )

        resultContainer.addView(
            card,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                setMargins(
                    0,
                    dp(5),
                    0,
                    dp(6)
                )
            }
        )
    }

    private fun readDouble(
        field: EditText,
        fieldName: String
    ): Double {

        val value =
            field.text
                .toString()
                .trim()
                .toDoubleOrNull()

        require(
            value != null
        ) {
            "$fieldName must be a valid number."
        }

        return value
    }

    private fun readDoubleOrZero(
        field: EditText
    ): Double {

        return field.text
            .toString()
            .trim()
            .toDoubleOrNull()
            ?: 0.0
    }

    private fun readable(
        value: String
    ): String {

        return value
            .lowercase()
            .replace(
                "_",
                " "
            )
            .replaceFirstChar {
                it.uppercaseChar()
            }
    }

    private fun dp(
        value: Int
    ): Int {

        return (
            value *
                resources.displayMetrics.density
            ).toInt()
    }

    companion object {

        private const val SOURCE_TRANSFORMER = 0
        private const val SOURCE_GENERATOR = 1
        private const val SOURCE_ANOTHER_PANEL = 2

        private const val CABLE_XLPE_COPPER = 0
        private const val CABLE_XLPE_ALUMINIUM = 1
        private const val CABLE_PVC_COPPER = 2
        private const val CABLE_PVC_ALUMINIUM = 3
    }
}

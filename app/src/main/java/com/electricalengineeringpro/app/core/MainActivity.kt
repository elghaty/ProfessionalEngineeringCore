package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
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
    private lateinit var installationSpinner: Spinner

    private lateinit var ambientInput: EditText
    private lateinit var groupingInput: EditText
    private lateinit var voltageDropInput: EditText

    private lateinit var resultContainer: LinearLayout

    private lateinit var sourceDataContainer: LinearLayout

    private val core =
        ProfessionalEngineeringCore.instance

    private val calculator
        get() = core.panelDesign

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        buildUi()
    }

    private fun buildUi() {

        val root =
            LinearLayout(this)

        root.orientation =
            LinearLayout.VERTICAL

        root.setBackgroundColor(
            Color.rgb(
                244,
                247,
                251
            )
        )

        root.addView(
            createHeader()
        )

        val scroll =
            ScrollView(this)

        content =
            LinearLayout(this)

        content.orientation =
            LinearLayout.VERTICAL

        content.setPadding(
            dp(16),
            dp(14),
            dp(16),
            dp(40)
        )

        scroll.addView(
            content
        )

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                -1,
                0,
                1f
            )
        )

        setContentView(
            root
        )

        buildDesignScreen()
    }

    private fun createHeader(): View {

        val header =
            LinearLayout(this)

        header.orientation =
            LinearLayout.VERTICAL

        header.setPadding(
            dp(20),
            dp(14),
            dp(20),
            dp(14)
        )

        header.setBackgroundColor(
            Color.rgb(
                11,
                41,
                66
            )
        )

        val title =
            TextView(this)

        title.text =
            "⚡ PROFESSIONAL ENGINEERING"

        title.textSize =
            20f

        title.typeface =
            Typeface.DEFAULT_BOLD

        title.setTextColor(
            Color.WHITE
        )

        header.addView(
            title
        )

        val subtitle =
            TextView(this)

        subtitle.text =
            "Electrical Design & Calculation System"

        subtitle.textSize =
            12f

        subtitle.setTextColor(
            Color.rgb(
                210,
                225,
                235
            )
        )

        subtitle.setPadding(
            0,
            dp(4),
            0,
            0
        )

        header.addView(
            subtitle
        )

        return header
    }

    private fun buildDesignScreen() {

        content.removeAllViews()

        addSectionTitle(
            "PANEL DESIGN",
            "Enter the electrical load in kW."
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
            LinearLayout(this)

        sourceDataContainer.orientation =
            LinearLayout.VERTICAL

        content.addView(
            sourceDataContainer
        )

        sourceKvaInput =
            addDynamicEdit(
                "Source Rating (kVA)",
                "630",
                true
            )

        sourceImpedanceInput =
            addDynamicEdit(
                "Transformer Impedance / Generator Xd'' (%)",
                "6.0",
                true
            )

        upstreamIscInput =
            addDynamicEdit(
                "Upstream Panel Short Circuit (kA)",
                "25",
                true
            )

        sourceSpinner.setOnItemSelectedListener(
            object :
                android.widget.AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    updateSourceFields(
                        position
                    )
                }
            }
        )

        updateSourceFields(
            0
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

        installationSpinner =
            addSpinner(
                "Installation Method",
                InstallationMethod.entries.map {
                    readable(
                        it.name
                    )
                }
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
            Button(this)

        calculateButton.text =
            "CALCULATE COMPLETE DESIGN"

        calculateButton.textSize =
            15f

        calculateButton.typeface =
            Typeface.DEFAULT_BOLD

        calculateButton.setTextColor(
            Color.WHITE
        )

        calculateButton.setBackgroundColor(
            Color.rgb(
                21,
                101,
                192
            )
        )

        calculateButton.setOnClickListener {
            calculateDesign()
        }

        val buttonParams =
            LinearLayout.LayoutParams(
                -1,
                dp(58)
            )

        buttonParams.setMargins(
            0,
            dp(18),
            0,
            dp(14)
        )

        content.addView(
            calculateButton,
            buttonParams
        )

        resultContainer =
            LinearLayout(this)

        resultContainer.orientation =
            LinearLayout.VERTICAL

        content.addView(
            resultContainer
        )
    }

    private fun updateSourceFields(
        position: Int
    ) {

        sourceKvaInput.visibility =
            if (position == 2) {
                View.GONE
            } else {
                View.VISIBLE
            }

        sourceImpedanceInput.visibility =
            if (position == 2) {
                View.GONE
            } else {
                View.VISIBLE
            }

        upstreamIscInput.visibility =
            if (position == 2) {
                View.VISIBLE
            } else {
                View.GONE
            }

        sourceKvaInput.hint =
            if (position == 0) {
                "Transformer Rating (kVA)"
            } else {
                "Generator Rating (kVA)"
            }

        sourceImpedanceInput.hint =
            if (position == 0) {
                "Transformer Impedance (%)"
            } else {
                "Generator Xd'' (%)"
            }
    }

    private fun calculateDesign() {

        resultContainer.removeAllViews()

        try {

            val sourceType =
                when (
                    sourceSpinner.selectedItemPosition
                ) {

                    0 ->
                        PanelSourceType.TRANSFORMER

                    1 ->
                        PanelSourceType.GENERATOR

                    else ->
                        PanelSourceType.OTHER_PANEL
                }

            val cableType =
                when (
                    cableSpinner.selectedItemPosition
                ) {

                    0 ->
                        CableType.XLPE_COPPER

                    1 ->
                        CableType.XLPE_ALUMINIUM

                    2 ->
                        CableType.PVC_COPPER

                    else ->
                        CableType.PVC_ALUMINIUM
                }

            val installationMethod =
                InstallationMethod.entries[
                    installationSpinner
                        .selectedItemPosition
                ]

            val input =
                PanelDesignInput(

                    panelName =
                        panelNameInput.text
                            .toString()
                            .trim(),

                    loadKw =
                        number(
                            loadInput,
                            "Panel Load"
                        ),

                    powerFactor =
                        number(
                            pfInput,
                            "Power Factor"
                        ),

                    voltageV =
                        number(
                            voltageInput,
                            "Voltage"
                        ),

                    lengthM =
                        number(
                            lengthInput,
                            "Cable Length"
                        ),

                    sourceType =
                        sourceType,

                    sourceKva =
                        if (
                            sourceType ==
                            PanelSourceType.OTHER_PANEL
                        ) {
                            0.0
                        } else {
                            number(
                                sourceKvaInput,
                                "Source kVA"
                            )
                        },

                    sourceImpedancePercent =
                        if (
                            sourceType ==
                            PanelSourceType.OTHER_PANEL
                        ) {
                            0.0
                        } else {
                            number(
                                sourceImpedanceInput,
                                "Source impedance"
                            )
                        },

                    upstreamShortCircuitKA =
                        if (
                            sourceType ==
                            PanelSourceType.OTHER_PANEL
                        ) {
                            number(
                                upstreamIscInput,
                                "Upstream short circuit"
                            )
                        } else {
                            0.0
                        },

                    cableType =
                        cableType,

                    installationMethod =
                        installationMethod,

                    ambientFactor =
                        number(
                            ambientInput,
                            "Ambient factor"
                        ),

                    groupingFactor =
                        number(
                            groupingInput,
                            "Grouping factor"
                        ),

                    targetVoltageDropPercent =
                        number(
                            voltageDropInput,
                            "Voltage drop"
                        )
                )

            val result =
                calculator.calculate(
                    input
                )

            showResult(
                result
            )

        } catch (
            exception: Exception
        ) {

            addResultCard(
                "DESIGN ERROR",
                exception.message
                    ?: "Invalid engineering input."
            )
        }
    }

    private fun showResult(
        result: PanelDesignResult
    ) {

        addSectionTitle(
            "DESIGN RESULTS",
            "Calculated automatically by ProfessionalEngineeringCore."
        )

        addResultCard(
            "LOAD",
            """
            Panel: ${result.panelName}
            
            Load: ${format1(result.loadKw)} kW
            
            Power Factor: ${format2(result.powerFactor)}
            
            Design Current: ${format1(result.designCurrentA)} A
            
            Voltage: ${format1(result.voltageV)} V
            """.trimIndent()
        )

        addResultCard(
            "SOURCE",
            """
            Source: ${sourceName(result.sourceType)}
            
            Required Capacity: ${format1(result.sourceRequiredKva)} kVA
            
            Recommended Capacity: ${format1(result.sourceRecommendedKva)} kVA
            
            Source Current: ${format1(result.sourceCurrentA)} A
            """.trimIndent()
        )

        addResultCard(
            "FEEDER CABLE",
            """
            Cable: ${result.cableDescription}
            
            Selected Size: ${format1(result.cableSizeMm2)} mm²
            
            Ampacity: ${format1(result.cableAmpacityA)} A
            
            Voltage Drop: ${format2(result.voltageDropPercent)} %
            """.trimIndent()
        )

        addResultCard(
            "PROTECTION",
            """
            Main Breaker: ${format1(result.breakerRatingA)} A
            
            Required Breaking Capacity: ${format1(result.breakerBreakingCapacityKA)} kA
            
            Load Current: ${format1(result.designCurrentA)} A
            """.trimIndent()
        )

        addResultCard(
            "SHORT CIRCUIT",
            """
            Panel Short Circuit: ${format2(result.shortCircuitKA)} kA
            
            Fault Level: ${format2(result.faultMva)} MVA
            """.trimIndent()
        )

        addSectionTitle(
            "SINGLE LINE DIAGRAM",
            "Electrical SLD generated from the calculated design."
        )

        val sldView =
            SldDiagramView(this)

        sldView.setDiagram(
            result.sld
        )

        val sldParams =
            LinearLayout.LayoutParams(
                -1,
                dp(
                    210 *
                        result.sld.nodes.size +
                        120
                )
            )

        sldParams.setMargins(
            0,
            dp(8),
            0,
            dp(20)
        )

        resultContainer.addView(
            sldView,
            sldParams
        )
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

        val titleView =
            TextView(this)

        titleView.text =
            title

        titleView.textSize =
            18f

        titleView.typeface =
            Typeface.DEFAULT_BOLD

        titleView.setTextColor(
            Color.rgb(
                11,
                65,
                105
            )
        )

        titleView.setPadding(
            dp(4),
            dp(14),
            dp(4),
            dp(3)
        )

        content.addView(
            titleView
        )

        val subtitleView =
            TextView(this)

        subtitleView.text =
            subtitle

        subtitleView.textSize =
            11f

        subtitleView.setTextColor(
            Color.rgb(
                90,
                105,
                120
            )
        )

        subtitleView.setPadding(
            dp(4),
            0,
            dp(4),
            dp(8)
        )

        content.addView(
            subtitleView
        )
    }

    private fun addEdit(
        label: String,
        defaultValue: String,
        numeric: Boolean = false
    ): EditText {

        val field =
            EditText(this)

        field.hint =
            label

        field.setText(
            defaultValue
        )

        field.textSize =
            14f

        field.setSingleLine(
            true
        )

        field.setPadding(
            dp(12),
            dp(4),
            dp(12),
            dp(4)
        )

        if (numeric) {

            field.inputType =
                InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val params =
            LinearLayout.LayoutParams(
                -1,
                dp(55)
            )

        params.setMargins(
            0,
            dp(3),
            0,
            dp(3)
        )

        content.addView(
            field,
            params
        )

        return field
    }

    private fun addDynamicEdit(
        label: String,
        defaultValue: String,
        numeric: Boolean
    ): EditText {

        val field =
            EditText(this)

        field.hint =
            label

        field.setText(
            defaultValue
        )

        field.textSize =
            14f

        field.setSingleLine(
            true
        )

        field.setPadding(
            dp(12),
            dp(4),
            dp(12),
            dp(4)
        )

        if (numeric) {

            field.inputType =
                InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val params =
            LinearLayout.LayoutParams(
                -1,
                dp(55)
            )

        params.setMargins(
            0,
            dp(3),
            0,
            dp(3)
        )

        sourceDataContainer.addView(
            field,
            params
        )

        return field
    }

    private fun addSpinner(
        label: String,
        values: List<String>
    ): Spinner {

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

        val params =
            LinearLayout.LayoutParams(
                -1,
                dp(55)
            )

        params.setMargins(
            0,
            dp(3),
            0,
            dp(3)
        )

        content.addView(
            spinner,
            params
        )

        return spinner
    }

    private fun addResultCard(
        title: String,
        text: String
    ) {

        val card =
            LinearLayout(this)

        card.orientation =
            LinearLayout.VERTICAL

        card.setBackgroundColor(
            Color.WHITE
        )

        card.setPadding(
            dp(15),
            dp(12),
            dp(15),
            dp(12)
        )

        val titleView =
            TextView(this)

        titleView.text =
            title

        titleView.textSize =
            14f

        titleView.typeface =
            Typeface.DEFAULT_BOLD

        titleView.setTextColor(
            Color.rgb(
                15,
                75,
                120
            )
        )

        card.addView(
            titleView
        )

        val valueView =
            TextView(this)

        valueView.text =
            text

        valueView.textSize =
            13f

        valueView.setTextColor(
            Color.rgb(
                45,
                55,
                65
            )
        )

        valueView.setPadding(
            0,
            dp(7),
            0,
            0
        )

        card.addView(
            valueView
        )

        val params =
            LinearLayout.LayoutParams(
                -1,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        params.setMargins(
            0,
            dp(5),
            0,
            dp(6)
        )

        resultContainer.addView(
            card,
            params
        )
    }

    private fun number(
        field: EditText,
        name: String
    ): Double {

        return field.text
            .toString()
            .trim()
            .toDoubleOrNull()
            ?: throw IllegalArgumentException(
                "$name is not a valid number."
            )
    }

    private fun format1(
        value: Double
    ): String =
        "%.1f".format(
            value
        )

    private fun format2(
        value: Double
    ): String =
        "%.2f".format(
            value
        )

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
                it.uppercase()
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
}

package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import com.electricalengineeringpro.app.core.calculation.PanelDesignCalculator
import com.electricalengineeringpro.app.core.model.CableType
import com.electricalengineeringpro.app.core.model.InstallationMethod
import com.electricalengineeringpro.app.core.model.PanelDesignInput
import com.electricalengineeringpro.app.core.model.PanelDesignResult
import com.electricalengineeringpro.app.core.model.PanelSourceType

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

    private val calculator =
        PanelDesignCalculator()

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
            dp(14),
            dp(14),
            dp(14),
            dp(30)
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

        header.gravity =
            Gravity.CENTER_VERTICAL

        header.setPadding(
            dp(18),
            dp(10),
            dp(18),
            dp(10)
        )

        header.setBackgroundColor(
            Color.WHITE
        )

        val title =
            TextView(this)

        title.text =
            "⚡ PROFESSIONAL ENGINEERING"

        title.textSize =
            19f

        title.typeface =
            Typeface.DEFAULT_BOLD

        title.setTextColor(
            Color.rgb(
                15,
                65,
                105
            )
        )

        header.addView(
            title
        )

        val subtitle =
            TextView(this)

        subtitle.text =
            "Automatic Electrical Panel Design"

        subtitle.textSize =
            11f

        subtitle.setTextColor(
            Color.rgb(
                90,
                105,
                120
            )
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
            "Enter the panel load. The program will complete the feeder design automatically."
        )

        panelNameInput =
            addEdit(
                "Panel Name",
                "MDB-01"
            )

        loadInput =
            addEdit(
                "Panel Load",
                "350"
            )

        pfInput =
            addEdit(
                "Power Factor",
                "0.90"
            )

        voltageInput =
            addEdit(
                "Voltage (V)",
                "400"
            )

        lengthInput =
            addEdit(
                "Cable Length (m)",
                "50"
            )

        addSectionTitle(
            "SOURCE OF SUPPLY",
            "Choose how this panel is supplied."
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

        sourceKvaInput =
            addEdit(
                "Source Rating (kVA)",
                "630"
            )

        sourceImpedanceInput =
            addEdit(
                "Transformer Impedance / Generator Xd'' (%)",
                "6"
            )

        upstreamIscInput =
            addEdit(
                "Upstream Panel Isc (kA)",
                "25"
            )

        addSectionTitle(
            "FEEDER CABLE",
            "Select cable type and installation method."
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
                    it.name.replace(
                        "_",
                        " "
                    )
                }
            )

        ambientInput =
            addEdit(
                "Ambient Correction Factor",
                "1.00"
            )

        groupingInput =
            addEdit(
                "Grouping Correction Factor",
                "1.00"
            )

        voltageDropInput =
            addEdit(
                "Maximum Voltage Drop (%)",
                "3.00"
            )

        val calculateButton =
            Button(this)

        calculateButton.text =
            "CALCULATE COMPLETE DESIGN"

        calculateButton.textSize =
            14f

        calculateButton.typeface =
            Typeface.DEFAULT_BOLD

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
            dp(16),
            0,
            dp(12)
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

            val installation =
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
                        number(
                            sourceKvaInput,
                            "Source kVA"
                        ),

                    sourceImpedancePercent =
                        number(
                            sourceImpedanceInput,
                            "Source impedance"
                        ),

                    upstreamShortCircuitKA =
                        number(
                            upstreamIscInput,
                            "Upstream Isc"
                        ),

                    cableType =
                        cableType,

                    installationMethod =
                        installation,

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
                    ?: "Invalid input."
            )
        }
    }

    private fun showResult(
        result: PanelDesignResult
    ) {

        addSectionTitle(
            "DESIGN RESULTS",
            "Automatically calculated electrical design."
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
            Source: ${result.sourceType}
            
            Required Capacity: ${format1(result.sourceRequiredKva)} kVA
            
            Recommended Capacity: ${format1(result.sourceRecommendedKva)} kVA
            
            Source Current: ${format1(result.sourceCurrentA)} A
            """.trimIndent()
        )

        addResultCard(
            "CABLE",
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
            
            Breaking Capacity: ${format1(result.breakerBreakingCapacityKA)} kA
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
            "Generated from the calculated electrical data."
        )

        result.sld.nodes.forEach { node ->

            val data =
                node.electricalData.entries
                    .joinToString(
                        "\n"
                    ) {
                        "${it.key}: ${it.value}"
                    }

            addResultCard(
                node.name,
                """
                Symbol: ${node.type}
                
                $data
                """.trimIndent()
            )
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
                15,
                65,
                105
            )
        )

        titleView.setPadding(
            dp(4),
            dp(12),
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
        defaultValue: String
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

    private fun addSpinner(
        label: String,
        values: List<String>
    ): Spinner {

        val spinner =
            Spinner(this)

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                values
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

    private fun dp(
        value: Int
    ): Int {

        return (
            value *
                resources.displayMetrics.density
            ).toInt()
    }
}

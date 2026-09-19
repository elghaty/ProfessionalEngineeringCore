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
private lateinit var installationSpinner: Spinner

private lateinit var ambientInput: EditText
private lateinit var groupingInput: EditText
private lateinit var voltageDropInput: EditText

private val core: ProfessionalEngineeringCore
    get() = ProfessionalEngineeringCore.instance

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    buildUi()
}

private fun buildUi() {

    val root = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(
            Color.rgb(244, 247, 251)
        )
    }

    root.addView(
        createHeader()
    )

    val scrollView = ScrollView(this)

    content = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(
            dp(16),
            dp(14),
            dp(16),
            dp(40)
        )
    }

    scrollView.addView(content)

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

        orientation = LinearLayout.VERTICAL

        setPadding(
            dp(20),
            dp(14),
            dp(20),
            dp(14)
        )

        setBackgroundColor(
            Color.rgb(11, 41, 66)
        )

        addView(
            TextView(this@MainActivity).apply {
                text = "⚡ PROFESSIONAL ENGINEERING"
                textSize = 20f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.WHITE)
            }
        )

        addView(
            TextView(this@MainActivity).apply {
                text = "Electrical Design & Calculation System"
                textSize = 12f
                setTextColor(
                    Color.rgb(210, 225, 235)
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

    panelNameInput = addEdit(
        label = "Panel Name",
        defaultValue = "MDB-01"
    )

    loadInput = addEdit(
        label = "Panel Load (kW)",
        defaultValue = "350",
        numeric = true
    )

    pfInput = addEdit(
        label = "Power Factor",
        defaultValue = "0.90",
        numeric = true
    )

    voltageInput = addEdit(
        label = "System Voltage (V)",
        defaultValue = "400",
        numeric = true
    )

    lengthInput = addEdit(
        label = "Feeder Cable Length (m)",
        defaultValue = "50",
        numeric = true
    )

    addSectionTitle(
        "SOURCE OF SUPPLY",
        "Select the source feeding this panel."
    )

    sourceSpinner = addSpinner(
        label = "Source Type",
        values = listOf(
            "Transformer",
            "Generator",
            "Another Panel"
        )
    )

    sourceDataContainer =
        LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

    content.addView(
        sourceDataContainer
    )

    sourceKvaInput = addDynamicEdit(
        label = "Source Rating (kVA)",
        defaultValue = "630"
    )

    sourceImpedanceInput = addDynamicEdit(
        label = "Transformer Impedance / Generator Xd'' (%)",
        defaultValue = "6.0"
    )

    upstreamIscInput = addDynamicEdit(
        label = "Upstream Panel Short Circuit (kA)",
        defaultValue = "25"
    )

    sourceSpinner.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(
                parent: AdapterView<*>?
            ) {
                // No UI action required.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateSourceFields(position)
            }
        }

    updateSourceFields(
        sourceSpinner.selectedItemPosition
    )

    addSectionTitle(
        "FEEDER CABLE",
        "Select cable construction and installation method."
    )

    cableSpinner = addSpinner(
        label = "Cable Type",
        values = listOf(
            "XLPE Copper",
            "XLPE Aluminium",
            "PVC Copper",
            "PVC Aluminium"
        )
    )

    installationSpinner = addSpinner(
        label = "Installation Method",
        values = InstallationMethod.values().map {
            readable(it.name)
        }
    )

    ambientInput = addEdit(
        label = "Ambient Correction Factor",
        defaultValue = "1.00",
        numeric = true
    )

    groupingInput = addEdit(
        label = "Grouping Correction Factor",
        defaultValue = "1.00",
        numeric = true
    )

    voltageDropInput = addEdit(
        label = "Maximum Voltage Drop (%)",
        defaultValue = "3.00",
        numeric = true
    )

    val calculateButton =
        Button(this).apply {

            text = "CALCULATE COMPLETE DESIGN"
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)

            setBackgroundColor(
                Color.rgb(21, 101, 192)
            )

            setOnClickListener {
                onCalculateClicked()
            }
        }

    val buttonParams =
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

    content.addView(
        calculateButton,
        buttonParams
    )

    resultContainer =
        LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

    content.addView(
        resultContainer
    )
}

private fun updateSourceFields(
    position: Int
) {

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
        if (position == SOURCE_TRANSFORMER) {
            "Transformer Rating (kVA)"
        } else {
            "Generator Rating (kVA)"
        }

    sourceImpedanceInput.hint =
        if (position == SOURCE_TRANSFORMER) {
            "Transformer Impedance (%)"
        } else {
            "Generator Xd'' (%)"
        }
}

/**
 * UI layer only.
 *
 * Reads fields and creates the engineering input model.
 * No electrical formula is implemented here.
 */
private fun onCalculateClicked() {

    resultContainer.removeAllViews()

    try {

        val input =
            createPanelDesignInputFromUi()

        val result =
            core.calculatePanelDesign(
                input
            )

        renderResult(
            result
        )

    } catch (exception: Exception) {

        addResultCard(
            "INPUT / DESIGN ERROR",
            exception.message
                ?: "Unable to complete the design."
        )
    }
}

/**
 * UI-to-model mapping only.
 *
 * Engineering calculations are performed by
 * ProfessionalEngineeringCore.
 */
private fun createPanelDesignInputFromUi():
    PanelDesignInput {

    return PanelDesignInput(

        panelName =
            panelNameInput.text
                .toString()
                .trim(),

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

        else ->
            PanelSourceType.OTHER_PANEL
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

        else ->
            CableType.PVC_ALUMINIUM
    }
}

private fun selectedInstallationMethod():
    InstallationMethod {

    val methods =
        InstallationMethod.values()

    val position =
        installationSpinner
            .selectedItemPosition
            .coerceIn(
                0,
                methods.lastIndex
            )

    return methods[position]
}

private fun renderResult(
    result: PanelDesignResult
) {

    addSectionTitle(
        "DESIGN RESULTS",
        "Results returned by ProfessionalEngineeringCore."
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
        
        Breaking Capacity: ${result.breakerBreakingCapacityKA} kA
        
        Load Current: ${result.designCurrentA} A
        """.trimIndent()
    )

    addResultCard(
        "SHORT CIRCUIT",
        """
        Panel Short Circuit: ${result.shortCircuitKA} kA
        
        Fault Level: ${result.faultMva} MVA
        """.trimIndent()
    )

    addSectionTitle(
        "SINGLE LINE DIAGRAM",
        "SLD generated by the engineering core."
    )

    val sldView =
        SldDiagramView(this)

    sldView.setDiagram(
        result.sld
    )

    val nodeCount =
        result.sld.nodes.size.coerceAtLeast(1)

    val sldHeight =
        dp(
            Sld_BASE_HEIGHT +
                Sld_NODE_HEIGHT *
                    nodeCount
        )

    val sldParams =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            sldHeight
        ).apply {
            setMargins(
                0,
                dp(8),
                0,
                dp(20)
            )
        }

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
        TextView(this).apply {

            text = title
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD

            setTextColor(
                Color.rgb(11, 65, 105)
            )

            setPadding(
                dp(4),
                dp(14),
                dp(4),
                dp(3)
            )
        }

    content.addView(
        titleView
    )

    val subtitleView =
        TextView(this).apply {

            text = subtitle
            textSize = 11f

            setTextColor(
                Color.rgb(90, 105, 120)
            )

            setPadding(
                dp(4),
                0,
                dp(4),
                dp(8)
            )
        }

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
        EditText(this).apply {

            hint = label
            setText(defaultValue)
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

    val params =
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

    content.addView(
        field,
        params
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
            setText(defaultValue)
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

    val params =
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

            this.text = title
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD

            setTextColor(
                Color.rgb(15, 75, 120)
            )
        }
    )

    card.addView(
        TextView(this).apply {

            this.text = text
            textSize = 13f

            setTextColor(
                Color.rgb(45, 55, 65)
            )

            setPadding(
                0,
                dp(7),
                0,
                0
            )
        }
    )

    val params =
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

    resultContainer.addView(
        card,
        params
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

    require(value != null) {
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

companion object {

    private const val SOURCE_TRANSFORMER = 0
    private const val SOURCE_GENERATOR = 1
    private const val SOURCE_ANOTHER_PANEL = 2

    private const val CABLE_XLPE_COPPER = 0
    private const val CABLE_XLPE_ALUMINIUM = 1
    private const val CABLE_PVC_COPPER = 2
    private const val CABLE_PVC_ALUMINIUM = 3

    private const val SLD_BASE_HEIGHT = 180
    private const val SLD_NODE_HEIGHT = 190
}

}

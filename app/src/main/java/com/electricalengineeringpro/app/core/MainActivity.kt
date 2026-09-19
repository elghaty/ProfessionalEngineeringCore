package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView

import com.electricalengineeringpro.app.core.calculation.CompleteDesignInput
import com.electricalengineeringpro.app.core.calculation.GeneratorInput
import com.electricalengineeringpro.app.core.calculation.MdbInput
import com.electricalengineeringpro.app.core.calculation.ProtectionInput

import com.electricalengineeringpro.app.core.model.BreakerInput
import com.electricalengineeringpro.app.core.model.BreakerType
import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.CableInsulation
import com.electricalengineeringpro.app.core.model.ConductorMaterial
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.InstallationMethod
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.MotorInput
import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.PumpInput
import com.electricalengineeringpro.app.core.model.ShortCircuitInput
import com.electricalengineeringpro.app.core.model.TransformerInput

import com.electricalengineeringpro.app.core.sld.SingleLineDiagram
import com.electricalengineeringpro.app.core.sld.SldNode

class MainActivity : Activity() {

    /*
     * IMPORTANT
     *
     * MainActivity is UI ONLY.
     *
     * All engineering calculations are delegated to
     * ProfessionalEngineeringCore and its existing calculators.
     */
    private val core =
        ProfessionalEngineeringCore.instance

    private val backgroundColor =
        Color.rgb(245, 248, 252)

    private val surfaceColor =
        Color.WHITE

    private val inputColor =
        Color.rgb(249, 251, 254)

    private val primaryColor =
        Color.rgb(25, 103, 175)

    private val primaryDarkColor =
        Color.rgb(14, 72, 125)

    private val textColor =
        Color.rgb(30, 43, 56)

    private val secondaryTextColor =
        Color.rgb(91, 105, 118)

    private val borderColor =
        Color.rgb(213, 222, 232)

    private val resultColor =
        Color.rgb(237, 248, 240)

    private val resultBorderColor =
        Color.rgb(186, 218, 194)

    private val resultTextColor =
        Color.rgb(35, 104, 57)

    private val warningColor =
        Color.rgb(255, 249, 231)

    private val warningBorderColor =
        Color.rgb(238, 215, 160)

    private val warningTextColor =
        Color.rgb(125, 91, 20)

    private lateinit var root: LinearLayout
    private lateinit var content: LinearLayout
    private lateinit var tabBar: LinearLayout

    private var inputRow: LinearLayout? = null
    private var inputCount = 0

    private data class Tab(
        val code: String,
        val title: String,
        val icon: String,
        val action: () -> Unit
    )

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        createRoot()
        showPower()
    }

    private fun createRoot() {

        root =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundColor(backgroundColor)
            }

        createHeader()
        createTabs()

        val scroll =
            ScrollView(this).apply {
                isFillViewport = true
                setBackgroundColor(backgroundColor)
            }

        content =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp(12),
                    dp(12),
                    dp(12),
                    dp(30)
                )
            }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }

    private fun createHeader() {

        val header =
            LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(
                    dp(15),
                    dp(8),
                    dp(15),
                    dp(8)
                )
                setBackgroundColor(surfaceColor)
            }

        val icon =
            TextView(this).apply {
                text = "⚡"
                textSize = 28f
                gravity = Gravity.CENTER
                setTextColor(primaryColor)
            }

        val textBox =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(10), 0, 0, 0)
            }

        val title =
            TextView(this).apply {
                text = "PROFESSIONAL ENGINEERING"
                textSize = 18f
                setTextColor(textColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val subtitle =
            TextView(this).apply {
                text = "Electrical Design & Calculation"
                textSize = 10f
                setTextColor(secondaryTextColor)
            }

        textBox.addView(title)
        textBox.addView(subtitle)

        header.addView(
            icon,
            LinearLayout.LayoutParams(
                dp(42),
                dp(42)
            )
        )

        header.addView(
            textBox,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        root.addView(
            header,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(60)
            )
        )
    }

    private fun createTabs() {

        val scroll =
            HorizontalScrollView(this).apply {
                isHorizontalScrollBarEnabled = false
                setBackgroundColor(surfaceColor)
                elevation = dp(2).toFloat()
            }

        tabBar =
            LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(
                    dp(6),
                    dp(5),
                    dp(6),
                    dp(5)
                )
                setBackgroundColor(surfaceColor)
            }

        scroll.addView(tabBar)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(74)
            )
        )

        val tabs =
            listOf(
                Tab("PWR", "Power", "⚡") {
                    showPower()
                },
                Tab("LOAD", "Loads", "▦") {
                    showLoad()
                },
                Tab("CBL", "Cable", "⌁") {
                    showCable()
                },
                Tab("VD", "Voltage Drop", "↘") {
                    showVoltageDrop()
                },
                Tab("SC", "Short Circuit", "⚠") {
                    showShortCircuit()
                },
                Tab("BRK", "Breaker", "▣") {
                    showBreaker()
                },
                Tab("TR", "Transformer", "⇅") {
                    showTransformer()
                },
                Tab("GEN", "Generator", "◉") {
                    showGenerator()
                },
                Tab("MTR", "Motor", "⚙") {
                    showMotor()
                },
                Tab("PMP", "Pump", "◈") {
                    showPump()
                },
                Tab("MDB", "MDB", "▤") {
                    showMdb()
                },
                Tab("PROT", "Protection", "🛡") {
                    showProtection()
                },
                Tab("NET", "Network", "⌘") {
                    showNetwork()
                },
                Tab("DES", "Complete", "◆") {
                    showCompleteDesign()
                },
                Tab("SLD", "SLD", "⌗") {
                    showSld()
                }
            )

        tabs.forEach { tab ->
            createTab(tab)
        }
    }

    private fun createTab(
        tab: Tab
    ) {

        val box =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(
                    dp(3),
                    dp(3),
                    dp(3),
                    dp(3)
                )
                background =
                    rounded(
                        surfaceColor,
                        borderColor,
                        1,
                        9
                    )
                setOnClickListener {
                    tab.action()
                }
            }

        val icon =
            TextView(this).apply {
                text = tab.icon
                textSize = 19f
                gravity = Gravity.CENTER
                setTextColor(primaryColor)
            }

        val code =
            TextView(this).apply {
                text = tab.code
                textSize = 8f
                gravity = Gravity.CENTER
                setTextColor(textColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val title =
            TextView(this).apply {
                text = tab.title
                textSize = 7f
                gravity = Gravity.CENTER
                setTextColor(secondaryTextColor)
                maxLines = 1
            }

        box.addView(
            icon,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(23)
            )
        )

        box.addView(
            code,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(15)
            )
        )

        box.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(16)
            )
        )

        tabBar.addView(
            box,
            LinearLayout.LayoutParams(
                dp(82),
                dp(60)
            ).apply {
                setMargins(
                    dp(3),
                    0,
                    dp(3),
                    0
                )
            }
        )
    }

    private fun clearPage() {

        flushInputRow()

        content.removeAllViews()

        inputRow = null
        inputCount = 0
    }

    private fun pageHeader(
        title: String,
        description: String
    ) {

        clearPage()

        val titleView =
            TextView(this).apply {
                text = title
                textSize = 21f
                setTextColor(textColor)
                typeface = Typeface.DEFAULT_BOLD
                setPadding(
                    dp(3),
                    dp(3),
                    dp(3),
                    0
                )
            }

        val descriptionView =
            TextView(this).apply {
                text = description
                textSize = 10f
                setTextColor(secondaryTextColor)
                setPadding(
                    dp(4),
                    dp(2),
                    dp(4),
                    dp(8)
                )
            }

        content.addView(
            titleView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(36)
            )
        )

        content.addView(
            descriptionView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(28)
            )
        )
    }

    private fun section(
        title: String,
        description: String
    ) {

        flushInputRow()

        val box =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp(11),
                    dp(8),
                    dp(11),
                    dp(8)
                )
                background =
                    rounded(
                        surfaceColor,
                        borderColor,
                        1,
                        9
                    )
            }

        val titleView =
            TextView(this).apply {
                text = title
                textSize = 13f
                setTextColor(primaryDarkColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val descriptionView =
            TextView(this).apply {
                text = description
                textSize = 9f
                setTextColor(secondaryTextColor)
            }

        box.addView(titleView)
        box.addView(descriptionView)

        content.addView(
            box,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    0,
                    dp(3),
                    0,
                    dp(7)
                )
            }
        )
    }

    private fun newInputRow() {

        flushInputRow()

        inputRow =
            LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.TOP
            }

        inputCount = 0
    }

    private fun addInput(
        view: View
    ) {

        if (
            inputRow == null ||
            inputCount >= 3
        ) {
            newInputRow()
        }

        inputRow?.addView(
            view,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(
                    dp(3),
                    0,
                    dp(3),
                    dp(7)
                )
            }
        )

        inputCount++

        if (inputCount == 3) {
            flushInputRow()
        }
    }

    private fun flushInputRow() {

        val row = inputRow ?: return

        if (row.childCount == 0) {
            inputRow = null
            inputCount = 0
            return
        }

        while (row.childCount < 3) {
            row.addView(
                View(this),
                LinearLayout.LayoutParams(
                    0,
                    dp(1),
                    1f
                ).apply {
                    setMargins(
                        dp(3),
                        0,
                        dp(3),
                        dp(7)
                    )
                }
            )
        }

        content.addView(
            row,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        inputRow = null
        inputCount = 0
    }

    private fun numberField(
        label: String,
        unit: String,
        initial: String
    ): EditText {

        val box =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp(6),
                    dp(5),
                    dp(6),
                    dp(5)
                )
                background =
                    rounded(
                        surfaceColor,
                        borderColor,
                        1,
                        8
                    )
            }

        val labelView =
            TextView(this).apply {
                text =
                    if (unit.isBlank()) {
                        label
                    } else {
                        "$label\n($unit)"
                    }

                textSize = 9f
                setTextColor(secondaryTextColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val edit =
            EditText(this).apply {
                setText(initial)
                textSize = 14f
                setTextColor(textColor)
                inputType =
                    InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL or
                        InputType.TYPE_NUMBER_FLAG_SIGNED
                setSingleLine(true)
                setPadding(
                    dp(7),
                    0,
                    dp(7),
                    0
                )
                background =
                    rounded(
                        inputColor,
                        borderColor,
                        1,
                        6
                    )
            }

        box.addView(
            labelView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(30)
            )
        )

        box.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(40)
            )
        )

        addInput(box)

        return edit
    }

    private fun textField(
        label: String,
        initial: String
    ): EditText {

        val box =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp(6),
                    dp(5),
                    dp(6),
                    dp(5)
                )
                background =
                    rounded(
                        surfaceColor,
                        borderColor,
                        1,
                        8
                    )
            }

        val labelView =
            TextView(this).apply {
                text = label
                textSize = 9f
                setTextColor(secondaryTextColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val edit =
            EditText(this).apply {
                setText(initial)
                textSize = 14f
                setTextColor(textColor)
                inputType = InputType.TYPE_CLASS_TEXT
                setSingleLine(true)
                setPadding(
                    dp(7),
                    0,
                    dp(7),
                    0
                )
                background =
                    rounded(
                        inputColor,
                        borderColor,
                        1,
                        6
                    )
            }

        box.addView(
            labelView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(30)
            )
        )

        box.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(40)
            )
        )

        addInput(box)

        return edit
    }

    private fun selector(
        label: String,
        values: List<String>
    ): Spinner {

        val box =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp(6),
                    dp(5),
                    dp(6),
                    dp(5)
                )
                background =
                    rounded(
                        surfaceColor,
                        borderColor,
                        1,
                        8
                    )
            }

        val labelView =
            TextView(this).apply {
                text = label
                textSize = 9f
                setTextColor(secondaryTextColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val spinner =
            Spinner(this).apply {
                adapter =
                    ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        values
                    )
                background =
                    rounded(
                        inputColor,
                        borderColor,
                        1,
                        6
                    )
            }

        box.addView(
            labelView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(30)
            )
        )

        box.addView(
            spinner,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(40)
            )
        )

        addInput(box)

        return spinner
    }

    private fun calculateButton(
        title: String,
        action: () -> Unit
    ) {

        flushInputRow()

        val button =
            Button(this).apply {
                text = title
                textSize = 11f
                setTextColor(Color.WHITE)
                typeface = Typeface.DEFAULT_BOLD
                background =
                    rounded(
                        primaryColor,
                        primaryDarkColor,
                        1,
                        9
                    )
                setOnClickListener {
                    action()
                }
            }

        content.addView(
            button,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(46)
            ).apply {
                setMargins(
                    dp(3),
                    dp(3),
                    dp(3),
                    dp(8)
                )
            }
        )
    }

    private fun showResult(
        name: String,
        value: String
    ) {

        flushInputRow()

        val row =
            LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(
                    dp(10),
                    dp(8),
                    dp(10),
                    dp(8)
                )
                background =
                    rounded(
                        resultColor,
                        resultBorderColor,
                        1,
                        8
                    )
            }

        val nameView =
            TextView(this).apply {
                text = name
                textSize = 10f
                setTextColor(resultTextColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val valueView =
            TextView(this).apply {
                text = value
                textSize = 13f
                setTextColor(textColor)
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.END
            }

        row.addView(
            nameView,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        row.addView(
            valueView,
            LinearLayout.LayoutParams(
                dp(170),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        content.addView(
            row,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    dp(3),
                    dp(2),
                    dp(3),
                    dp(5)
                )
            }
        )
    }

    private fun showError(
        message: String
    ) {

        flushInputRow()

        val box =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp(11),
                    dp(9),
                    dp(11),
                    dp(9)
                )
                background =
                    rounded(
                        warningColor,
                        warningBorderColor,
                        1,
                        8
                    )
            }

        val title =
            TextView(this).apply {
                text = "CALCULATION MESSAGE"
                textSize = 10f
                setTextColor(warningTextColor)
                typeface = Typeface.DEFAULT_BOLD
            }

        val body =
            TextView(this).apply {
                text = message
                textSize = 10f
                setTextColor(warningTextColor)
            }

        box.addView(title)
        box.addView(body)

        content.addView(
            box,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    dp(3),
                    dp(4),
                    dp(3),
                    dp(8)
                )
            }
        )
    }

    private fun read(
        field: EditText
    ): Double {
        return field.text
            .toString()
            .trim()
            .replace(",", ".")
            .toDouble()
    }

    private fun format(
        value: Double
    ): String {
        return String.format(
            "%.2f",
            value
        )
    }

    private fun rounded(
        fill: Int,
        stroke: Int,
        width: Int,
        radius: Int
    ): GradientDrawable {
        return GradientDrawable().apply {
            setColor(fill)
            setStroke(
                dp(width),
                stroke
            )
            cornerRadius =
                dp(radius).toFloat()
        }
    }

    /*
     * ============================================================
     * POWER
     * ============================================================
     */

    private fun showPower() {

        pageHeader(
            "POWER",
            "Power calculation using the existing PowerCalculator"
        )

        section(
            "Power Input",
            "Active power, voltage and power factor"
        )

        val power =
            numberField(
                "Active Power",
                "kW",
                "100"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.90"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        calculateButton(
            "CALCULATE POWER"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val result =
                    core.power.fromKw(
                        powerKw = read(power),
                        voltage = read(voltage),
                        powerFactor = read(pf),
                        phase = selectedPhase
                    )

                showResult(
                    "ACTIVE POWER",
                    "${format(result.activePowerKw)} kW"
                )

                showResult(
                    "APPARENT POWER",
                    "${format(result.apparentPowerKva)} kVA"
                )

                showResult(
                    "CURRENT",
                    "${format(result.currentA)} A"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Power calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * LOAD
     * ============================================================
     */

    private fun showLoad() {

        pageHeader(
            "LOAD",
            "Electrical load calculation"
        )

        section(
            "Load Data",
            "Load type, quantity, power and demand parameters"
        )

        val name =
            textField(
                "Load Name",
                "MAIN LOAD"
            )

        val type =
            selector(
                "Load Type",
                listOf(
                    "Lighting",
                    "Socket",
                    "HVAC",
                    "Motor",
                    "Pump",
                    "Fire Pump",
                    "Elevator",
                    "Mechanical",
                    "Miscellaneous"
                )
            )

        val quantity =
            numberField(
                "Quantity",
                "",
                "1"
            )

        val power =
            numberField(
                "Unit Power",
                "kW",
                "100"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.90"
            )

        val efficiency =
            numberField(
                "Efficiency",
                "",
                "1.00"
            )

        val demand =
            numberField(
                "Demand Factor",
                "",
                "0.80"
            )

        val diversity =
            numberField(
                "Diversity Factor",
                "",
                "1.00"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        calculateButton(
            "CALCULATE LOAD"
        ) {

            try {

                val loadType =
                    when (
                        type.selectedItemPosition
                    ) {
                        0 -> LoadType.LIGHTING
                        1 -> LoadType.SOCKET
                        2 -> LoadType.HVAC
                        3 -> LoadType.MOTOR
                        4 -> LoadType.PUMP
                        5 -> LoadType.FIRE_PUMP
                        6 -> LoadType.ELEVATOR
                        7 -> LoadType.MECHANICAL
                        else -> LoadType.MISCELLANEOUS
                    }

                val load =
                    ElectricalLoad(
                        name =
                            name.text
                                .toString()
                                .trim()
                                .ifEmpty {
                                    "LOAD"
                                },

                        type = loadType,

                        quantity =
                            read(quantity).toInt(),

                        powerKw =
                            read(power),

                        powerFactor =
                            read(pf),

                        efficiency =
                            read(efficiency),

                        demandFactor =
                            read(demand),

                        diversityFactor =
                            read(diversity),

                        voltage =
                            read(voltage),

                        phase =
                            Phase.THREE
                    )

                val result =
                    core.loads.calculate(load)

                showResult(
                    "CONNECTED LOAD",
                    "${format(result.connectedKw)} kW"
                )

                showResult(
                    "DEMAND LOAD",
                    "${format(result.demandKw)} kW"
                )

                showResult(
                    "DESIGN LOAD",
                    "${format(result.designKw)} kW"
                )

                showResult(
                    "APPARENT POWER",
                    "${format(result.apparentPowerKva)} kVA"
                )

                showResult(
                    "REACTIVE POWER",
                    "${format(result.reactivePowerKvar)} kVAr"
                )

                showResult(
                    "CURRENT",
                    "${format(result.currentA)} A"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Load calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * CABLE
     * ============================================================
     */

    private fun showCable() {

        pageHeader(
            "CABLE",
            "Cable sizing using the existing CableCalculator"
        )

        section(
            "Cable Design",
            "Select conductor, insulation and installation method"
        )

        val current =
            numberField(
                "Design Current",
                "A",
                "160"
            )

        val length =
            numberField(
                "Length",
                "m",
                "50"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.90"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        val material =
            selector(
                "Conductor Material",
                listOf(
                    "Copper",
                    "Aluminium"
                )
            )

        val insulation =
            selector(
                "Insulation",
                listOf(
                    "PVC",
                    "XLPE",
                    "EPR"
                )
            )

        val installation =
            selector(
                "Installation Method",
                listOf(
                    "Conduit",
                    "Tray",
                    "Ladder",
                    "Duct",
                    "Buried",
                    "Free Air"
                )
            )

        val ambient =
            numberField(
                "Ambient Factor",
                "",
                "1.00"
            )

        val grouping =
            numberField(
                "Grouping Factor",
                "",
                "1.00"
            )

        val targetDrop =
            numberField(
                "Target Voltage Drop",
                "%",
                "3.00"
            )

        calculateButton(
            "SIZE CABLE"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val selectedMaterial =
                    if (
                        material.selectedItemPosition == 0
                    ) {
                        ConductorMaterial.COPPER
                    } else {
                        ConductorMaterial.ALUMINIUM
                    }

                val selectedInsulation =
                    when (
                        insulation.selectedItemPosition
                    ) {
                        0 -> CableInsulation.PVC
                        1 -> CableInsulation.XLPE
                        else -> CableInsulation.EPR
                    }

                val selectedInstallation =
                    when (
                        installation.selectedItemPosition
                    ) {
                        0 -> InstallationMethod.CONDUIT
                        1 -> InstallationMethod.TRAY
                        2 -> InstallationMethod.LADDER
                        3 -> InstallationMethod.DUCT
                        4 -> InstallationMethod.BURIED
                        else -> InstallationMethod.FREE_AIR
                    }

                val result =
                    core.cable.calculate(
                        CableInput(
                            designCurrentA =
                                read(current),

                            lengthM =
                                read(length),

                            voltage =
                                read(voltage),

                            powerFactor =
                                read(pf),

                            phase =
                                selectedPhase,

                            material =
                                selectedMaterial,

                            insulation =
                                selectedInsulation,

                            installationMethod =
                                selectedInstallation,

                            ambientFactor =
                                read(ambient),

                            groupingFactor =
                                read(grouping),

                            targetVoltageDropPercent =
                                read(targetDrop)
                        )
                    )

                showResult(
                    "CABLE SIZE",
                    "${format(result.selectedSizeMm2)} mm²"
                )

                showResult(
                    "AMPACITY",
                    "${format(result.ampacityA)} A"
                )

                showResult(
                    "VOLTAGE DROP",
                    "${format(result.voltageDropPercent)} %"
                )

                showResult(
                    "UTILIZATION",
                    "${format(result.utilizationPercent)} %"
                )

                showResult(
                    "CONDUCTOR",
                    result.conductorDescription
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Cable calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * VOLTAGE DROP
     * ============================================================
     */

    private fun showVoltageDrop() {

        pageHeader(
            "VOLTAGE DROP",
            "Voltage drop verification using the existing calculator"
        )

        section(
            "Voltage Drop Data",
            "Current, length, cable resistance and reactance"
        )

        val current =
            numberField(
                "Current",
                "A",
                "160"
            )

        val length =
            numberField(
                "Length",
                "m",
                "50"
            )

        val resistance =
            numberField(
                "Resistance",
                "Ω/km",
                "0.125"
            )

        val reactance =
            numberField(
                "Reactance",
                "Ω/km",
                "0.080"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.90"
            )

        val maximum =
            numberField(
                "Maximum Drop",
                "%",
                "3.00"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        calculateButton(
            "CALCULATE VOLTAGE DROP"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val result =
                    core.voltageDrop.calculate(
                        currentA =
                            read(current),

                        lengthM =
                            read(length),

                        resistanceOhmPerKm =
                            read(resistance),

                        reactanceOhmPerKm =
                            read(reactance),

                        voltage =
                            read(voltage),

                        powerFactor =
                            read(pf),

                        phase =
                            selectedPhase,

                        maximumPercent =
                            read(maximum)
                    )

                showResult(
                    "DROP",
                    "${format(result.dropVolts)} V"
                )

                showResult(
                    "DROP PERCENT",
                    "${format(result.dropPercent)} %"
                )

                showResult(
                    "STATUS",
                    if (result.compliant) {
                        "COMPLIANT"
                    } else {
                        "NOT COMPLIANT"
                    }
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Voltage drop calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * SHORT CIRCUIT
     * ============================================================
     */

    private fun showShortCircuit() {

        pageHeader(
            "SHORT CIRCUIT",
            "Short-circuit calculation using ShortCircuitCalculator"
        )

        section(
            "Fault Level",
            "Transformer rating and impedance"
        )

        val voltage =
            numberField(
                "System Voltage",
                "V",
                "400"
            )

        val transformer =
            numberField(
                "Transformer Rating",
                "kVA",
                "1000"
            )

        val impedance =
            numberField(
                "Transformer Impedance",
                "%",
                "6.00"
            )

        val sourceMva =
            numberField(
                "Source Short Circuit",
                "MVA",
                "0"
            )

        calculateButton(
            "CALCULATE SHORT CIRCUIT"
        ) {

            try {

                val sourceValue =
                    read(sourceMva)

                val result =
                    core.shortCircuit.calculate(
                        ShortCircuitInput(
                            sourceVoltage =
                                read(voltage),

                            transformerKva =
                                read(transformer),

                            transformerImpedancePercent =
                                read(impedance),

                            sourceShortCircuitMva =
                                if (
                                    sourceValue > 0.0
                                ) {
                                    sourceValue
                                } else {
                                    null
                                }
                        )
                    )

                showResult(
                    "FAULT CURRENT",
                    "${format(result.faultCurrentKA)} kA"
                )

                showResult(
                    "FAULT LEVEL",
                    "${format(result.faultMva)} MVA"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Short circuit calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * BREAKER
     * ============================================================
     */

    private fun showBreaker() {

        pageHeader(
            "BREAKER",
            "Breaker selection using the existing BreakerCalculator"
        )

        section(
            "Breaker Data",
            "Design current and short-circuit current"
        )

        val current =
            numberField(
                "Design Current",
                "A",
                "160"
            )

        val shortCircuit =
            numberField(
                "Short Circuit",
                "kA",
                "25"
            )

        val type =
            selector(
                "Breaker Type",
                listOf(
                    "MCB",
                    "MCCB",
                    "ACB",
                    "RCBO",
                    "RCD"
                )
            )

        calculateButton(
            "SELECT BREAKER"
        ) {

            try {

                val breakerType =
                    when (
                        type.selectedItemPosition
                    ) {
                        0 -> BreakerType.MCB
                        1 -> BreakerType.MCCB
                        2 -> BreakerType.ACB
                        3 -> BreakerType.RCBO
                        else -> BreakerType.RCD
                    }

                val result =
                    core.breaker.calculate(
                        BreakerInput(
                            designCurrentA =
                                read(current),

                            shortCircuitCurrentKA =
                                read(shortCircuit),

                            preferredType =
                                breakerType
                        )
                    )

                showResult(
                    "TYPE",
                    result.type.name
                )

                showResult(
                    "RATED CURRENT",
                    "${format(result.ratedCurrentA)} A"
                )

                showResult(
                    "BREAKING CAPACITY",
                    "${format(result.breakingCapacityKA)} kA"
                )

                showResult(
                    "UTILIZATION",
                    "${format(result.utilizationPercent)} %"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Breaker calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * TRANSFORMER
     * ============================================================
     */

    private fun showTransformer() {

        pageHeader(
            "TRANSFORMER",
            "Transformer current and short-circuit calculation"
        )

        section(
            "Transformer Data",
            "Rating, primary voltage, secondary voltage and impedance"
        )

        val rating =
            numberField(
                "Rating",
                "kVA",
                "1000"
            )

        val primary =
            numberField(
                "Primary Voltage",
                "V",
                "11000"
            )

        val secondary =
            numberField(
                "Secondary Voltage",
                "V",
                "400"
            )

        val impedance =
            numberField(
                "Impedance",
                "%",
                "6.00"
            )

        calculateButton(
            "CALCULATE TRANSFORMER"
        ) {

            try {

                val result =
                    core.transformer.calculate(
                        TransformerInput(
                            ratingKva =
                                read(rating),

                            primaryVoltage =
                                read(primary),

                            secondaryVoltage =
                                read(secondary),

                            impedancePercent =
                                read(impedance)
                        )
                    )

                showResult(
                    "PRIMARY CURRENT",
                    "${format(result.primaryCurrentA)} A"
                )

                showResult(
                    "SECONDARY CURRENT",
                    "${format(result.secondaryCurrentA)} A"
                )

                showResult(
                    "SHORT CIRCUIT",
                    "${format(result.shortCircuitCurrentKA)} kA"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Transformer calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * GENERATOR
     * ============================================================
     */

    private fun showGenerator() {

        pageHeader(
            "GENERATOR",
            "Generator calculation using the existing GeneratorCalculator"
        )

        section(
            "Generator Data",
            "Generator rating and operating parameters"
        )

        val rating =
            numberField(
                "Generator Rating",
                "kVA",
                "500"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.80"
            )

        val efficiency =
            numberField(
                "Efficiency",
                "",
                "0.90"
            )

        val margin =
            numberField(
                "Design Margin",
                "×",
                "1.25"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        calculateButton(
            "CALCULATE GENERATOR"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val result =
                    core.generators.calculate(
                        GeneratorInput(
                            ratingKva =
                                read(rating),

                            voltageV =
                                read(voltage),

                            powerFactor =
                                read(pf),

                            phase =
                                selectedPhase,

                            efficiency =
                                read(efficiency),

                            designMarginFactor =
                                read(margin)
                        )
                    )

                showResult(
                    "ACTIVE POWER",
                    "${format(result.activePowerKw)} kW"
                )

                showResult(
                    "FULL LOAD CURRENT",
                    "${format(result.fullLoadCurrentA)} A"
                )

                showResult(
                    "RECOMMENDED BREAKER",
                    "${format(result.recommendedBreakerA)} A"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Generator calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * MOTOR
     * ============================================================
     */

    private fun showMotor() {

        pageHeader(
            "MOTOR",
            "Motor current calculation using MotorCalculator"
        )

        section(
            "Motor Data",
            "Motor power, voltage, efficiency and starting multiplier"
        )

        val power =
            numberField(
                "Motor Power",
                "kW",
                "75"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.85"
            )

        val efficiency =
            numberField(
                "Efficiency",
                "",
                "0.92"
            )

        val multiplier =
            numberField(
                "Starting Multiplier",
                "×",
                "6.00"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        calculateButton(
            "CALCULATE MOTOR"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val result =
                    core.motors.calculate(
                        MotorInput(
                            powerKw =
                                read(power),

                            voltage =
                                read(voltage),

                            powerFactor =
                                read(pf),

                            efficiency =
                                read(efficiency),

                            phase =
                                selectedPhase,

                            startingMultiplier =
                                read(multiplier)
                        )
                    )

                showResult(
                    "FULL LOAD CURRENT",
                    "${format(result.fullLoadCurrentA)} A"
                )

                showResult(
                    "STARTING CURRENT",
                    "${format(result.startingCurrentA)} A"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Motor calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * PUMP
     * ============================================================
     */

    private fun showPump() {

        pageHeader(
            "PUMP",
            "Pump power and current using PumpCalculator"
        )

        section(
            "Pump Data",
            "Flow, head and pump/motor efficiencies"
        )

        val flow =
            numberField(
                "Flow",
                "m³/s",
                "0.300"
            )

        val head =
            numberField(
                "Head",
                "m",
                "7.00"
            )

        val pumpEfficiency =
            numberField(
                "Pump Efficiency",
                "",
                "0.80"
            )

        val motorEfficiency =
            numberField(
                "Motor Efficiency",
                "",
                "0.92"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.85"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        calculateButton(
            "CALCULATE PUMP"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val result =
                    core.pumps.calculate(
                        PumpInput(
                            flowM3s =
                                read(flow),

                            headM =
                                read(head),

                            pumpEfficiency =
                                read(pumpEfficiency),

                            motorEfficiency =
                                read(motorEfficiency),

                            powerFactor =
                                read(pf),

                            voltage =
                                read(voltage),

                            phase =
                                selectedPhase
                        )
                    )

                showResult(
                    "HYDRAULIC POWER",
                    "${format(result.hydraulicPowerKw)} kW"
                )

                showResult(
                    "MOTOR POWER",
                    "${format(result.motorPowerKw)} kW"
                )

                showResult(
                    "CURRENT",
                    "${format(result.currentA)} A"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Pump calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * MDB
     * ============================================================
     */

    private fun showMdb() {

        pageHeader(
            "MDB",
            "Main distribution board sizing"
        )

        section(
            "MDB Data",
            "Connected load, demand, power factor and spare capacity"
        )

        val connected =
            numberField(
                "Connected Load",
                "kW",
                "1000"
            )

        val demand =
            numberField(
                "Demand Factor",
                "",
                "0.80"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.90"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val spare =
            numberField(
                "Spare Capacity",
                "",
                "0.20"
            )

        calculateButton(
            "CALCULATE MDB"
        ) {

            try {

                val result =
                    core.mdb.calculate(
                        MdbInput(
                            connectedLoadKw =
                                read(connected),

                            demandFactor =
                                read(demand),

                            powerFactor =
                                read(pf),

                            voltageV =
                                read(voltage),

                            spareCapacity =
                                read(spare)
                        )
                    )

                showResult(
                    "DEMAND LOAD",
                    "${format(result.demandLoadKw)} kW"
                )

                showResult(
                    "APPARENT POWER",
                    "${format(result.apparentPowerKva)} kVA"
                )

                showResult(
                    "DESIGN CURRENT",
                    "${format(result.designCurrentA)} A"
                )

                showResult(
                    "RECOMMENDED INCOMER",
                    "${format(result.recommendedIncomerA)} A"
                )

                showResult(
                    "RECOMMENDED BUSBAR",
                    "${format(result.recommendedBusbarA)} A"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "MDB calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * PROTECTION
     * ============================================================
     */

    private fun showProtection() {

        pageHeader(
            "PROTECTION",
            "Protection check using the existing ProtectionCalculator"
        )

        section(
            "Protection Data",
            "Design current, cable ampacity and short-circuit level"
        )

        val current =
            numberField(
                "Design Current",
                "A",
                "160"
            )

        val ampacity =
            numberField(
                "Cable Ampacity",
                "A",
                "200"
            )

        val shortCircuit =
            numberField(
                "Short Circuit",
                "kA",
                "25"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val margin =
            numberField(
                "Protection Margin",
                "×",
                "1.25"
            )

        calculateButton(
            "CHECK PROTECTION"
        ) {

            try {

                val result =
                    core.protection.calculate(
                        ProtectionInput(
                            designCurrentA =
                                read(current),

                            cableAmpacityA =
                                read(ampacity),

                            shortCircuitKA =
                                read(shortCircuit),

                            voltageV =
                                read(voltage),

                            margin =
                                read(margin)
                        )
                    )

                showResult(
                    "RECOMMENDED BREAKER",
                    "${format(result.recommendedBreakerA)} A"
                )

                showResult(
                    "BREAKING CAPACITY",
                    "${format(result.breakingCapacityKA)} kA"
                )

                showResult(
                    "CABLE PROTECTED",
                    if (result.cableProtected) {
                        "YES"
                    } else {
                        "NO"
                    }
                )

                showResult(
                    "SHORT CIRCUIT PROTECTED",
                    if (result.shortCircuitProtected) {
                        "YES"
                    } else {
                        "NO"
                    }
                )

                showResult(
                    "STATUS",
                    result.status
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Protection calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * NETWORK
     * ============================================================
     */

    private fun showNetwork() {

        pageHeader(
            "NETWORK",
            "Electrical network calculation"
        )

        section(
            "Network Data",
            "Network load and operating parameters"
        )

        val power =
            numberField(
                "Connected Load",
                "kW",
                "1000"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.90"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        calculateButton(
            "CALCULATE NETWORK"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val load =
                    ElectricalLoad(
                        name = "MAIN LOAD",
                        type = LoadType.MISCELLANEOUS,
                        quantity = 1,
                        powerKw = read(power),
                        powerFactor = read(pf),
                        voltage = read(voltage),
                        phase = selectedPhase
                    )

                val result =
                    core.network.calculate(
                        loads =
                            listOf(load),

                        voltageV =
                            read(voltage),

                        powerFactor =
                            read(pf),

                        phase =
                            selectedPhase
                    )

                showResult(
                    "CONNECTED LOAD",
                    "${format(result.totalConnectedKw)} kW"
                )

                showResult(
                    "DEMAND LOAD",
                    "${format(result.totalDemandKw)} kW"
                )

                showResult(
                    "DESIGN LOAD",
                    "${format(result.totalDesignKw)} kW"
                )

                showResult(
                    "APPARENT POWER",
                    "${format(result.totalApparentPowerKva)} kVA"
                )

                showResult(
                    "MAIN CURRENT",
                    "${format(result.mainCurrentA)} A"
                )

                showResult(
                    "ESTIMATED TRANSFORMER",
                    "${format(result.estimatedTransformerKva)} kVA"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Network calculation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * COMPLETE DESIGN
     * ============================================================
     */

    private fun showCompleteDesign() {

        pageHeader(
            "COMPLETE DESIGN",
            "Integrated design using CompleteDesignCalculator"
        )

        section(
            "Design Input",
            "Main electrical design parameters"
        )

        val power =
            numberField(
                "Connected Load",
                "kW",
                "1000"
            )

        val voltage =
            numberField(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            numberField(
                "Power Factor",
                "",
                "0.90"
            )

        val diversity =
            numberField(
                "Diversity Factor",
                "",
                "1.15"
            )

        val shortCircuit =
            numberField(
                "Short Circuit",
                "kA",
                "25"
            )

        val phase =
            selector(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        calculateButton(
            "RUN COMPLETE DESIGN"
        ) {

            try {

                val selectedPhase =
                    if (
                        phase.selectedItemPosition == 0
                    ) {
                        Phase.THREE
                    } else {
                        Phase.SINGLE
                    }

                val load =
                    ElectricalLoad(
                        name = "MAIN LOAD",
                        type = LoadType.MISCELLANEOUS,
                        quantity = 1,
                        powerKw = read(power),
                        powerFactor = read(pf),
                        voltage = read(voltage),
                        phase = selectedPhase
                    )

                val result =
                    core.completeDesign.calculate(
                        CompleteDesignInput(
                            loads =
                                listOf(load),

                            voltageV =
                                read(voltage),

                            powerFactor =
                                read(pf),

                            diversityFactor =
                                read(diversity),

                            shortCircuitKA =
                                read(shortCircuit),

                            phase =
                                selectedPhase
                        )
                    )

                showResult(
                    "CONNECTED LOAD",
                    "${format(result.connectedLoadKW)} kW"
                )

                showResult(
                    "DEMAND LOAD",
                    "${format(result.demandLoadKW)} kW"
                )

                showResult(
                    "DESIGN LOAD",
                    "${format(result.designLoadKW)} kW"
                )

                showResult(
                    "APPARENT POWER",
                    "${format(result.apparentPowerKVA)} kVA"
                )

                showResult(
                    "MAIN CURRENT",
                    "${format(result.mainCurrentA)} A"
                )

                showResult(
                    "TRANSFORMER REQUIRED",
                    "${format(result.transformerRequiredKVA)} kVA"
                )

                showResult(
                    "RECOMMENDED TRANSFORMER",
                    "${format(result.transformerRecommendedKVA)} kVA"
                )

                showResult(
                    "MAIN BREAKER",
                    "${format(result.mainBreakerA)} A"
                )

                showResult(
                    "BREAKING CAPACITY",
                    "${format(result.breakerBreakingCapacityKA)} kA"
                )

            } catch (e: Exception) {
                showError(
                    e.message ?: "Complete design failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * SLD
     * ============================================================
     */

    private fun showSld() {

        pageHeader(
            "SINGLE LINE DIAGRAM",
            "SLD generation using the existing SldGenerator"
        )

        section(
            "SLD Input",
            "Define the source, main panel and feeder"
        )

        val source =
            selector(
                "Supply Source",
                listOf(
                    "Utility",
                    "Transformer",
                    "Generator",
                    "Transformer + Generator"
                )
            )

        val rating =
            numberField(
                "Source Rating",
                "kVA",
                "1000"
            )

        val panel =
            textField(
                "Main Panel",
                "MDB"
            )

        val feeder =
            textField(
                "Feeder",
                "FEEDER-01"
            )

        calculateButton(
            "GENERATE SLD"
        ) {

            try {

                val sourceType =
                    when (
                        source.selectedItemPosition
                    ) {
                        1 ->
                            NetworkElementType.TRANSFORMER

                        2 ->
                            NetworkElementType.GENERATOR

                        else ->
                            NetworkElementType.SOURCE
                    }

                val sourceElement =
                    NetworkElement(
                        id = "SOURCE",
                        name =
                            source.selectedItem
                                .toString(),
                        type = sourceType,
                        ratingKva =
                            read(rating)
                    )

                val panelElement =
                    NetworkElement(
                        id = "MDB",
                        name =
                            panel.text
                                .toString()
                                .trim()
                                .ifEmpty {
                                    "MDB"
                                },
                        type =
                            NetworkElementType.PANEL,
                        parentId =
                            sourceElement.id,
                        ratingKva =
                            read(rating)
                    )

                val feederElement =
                    NetworkElement(
                        id = "FEEDER-01",
                        name =
                            feeder.text
                                .toString()
                                .trim()
                                .ifEmpty {
                                    "FEEDER-01"
                                },
                        type =
                            NetworkElementType.LOAD,
                        parentId =
                            panelElement.id
                    )

                val diagram =
                    core.sld.generate(
                        source =
                            sourceElement,

                        panels =
                            listOf(panelElement),

                        feeders =
                            listOf(feederElement)
                    )

                showResult(
                    "SLD STATUS",
                    "GENERATED"
                )

                showResult(
                    "NODES",
                    diagram.nodes.size.toString()
                )

                showResult(
                    "CONNECTIONS",
                    diagram.connections.size.toString()
                )

                renderSld(diagram)

            } catch (e: Exception) {
                showError(
                    e.message ?: "SLD generation failed"
                )
            }
        }
    }

    /*
     * ============================================================
     * SLD RENDERING
     *
     * Rendering only.
     * No electrical calculation is performed here.
     * ============================================================
     */

    private fun renderSld(
        diagram: SingleLineDiagram
    ) {

        section(
            "Generated SLD",
            "Graphical representation of the generated network"
        )

        val view =
            SldView(diagram)

        content.addView(
            view,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(620)
            ).apply {
                setMargins(
                    dp(3),
                    dp(3),
                    dp(3),
                    dp(10)
                )
            }
        )
    }

    private inner class SldView(
        private val diagram: SingleLineDiagram
    ) : View(this) {

        private val linePaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = primaryColor
                strokeWidth = dp(3).toFloat()
                style = Paint.Style.STROKE
            }

        private val fillPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = surfaceColor
                style = Paint.Style.FILL
            }

        private val borderPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = primaryColor
                strokeWidth = dp(2).toFloat()
                style = Paint.Style.STROKE
            }

        private val textPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = textColor
                textSize = dp(12).toFloat()
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }

        private val smallPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = secondaryTextColor
                textSize = dp(9).toFloat()
                textAlign = Paint.Align.CENTER
            }

        override fun onDraw(
            canvas: Canvas
        ) {

            canvas.drawColor(backgroundColor)

            if (diagram.nodes.isEmpty()) {
                return
            }

            val centerX =
                width / 2f

            val nodeWidth =
                dp(190).toFloat()

            val nodeHeight =
                dp(68).toFloat()

            val firstY =
                dp(65).toFloat()

            val spacing =
                dp(125).toFloat()

            val positions =
                mutableMapOf<
                    String,
                    Pair<Float, Float>
                >()

            diagram.nodes.forEachIndexed {
                    index,
                    node ->

                val x = centerX

                val y =
                    firstY +
                        index * spacing

                positions[node.id] =
                    Pair(x, y)

                drawNode(
                    canvas,
                    node,
                    x,
                    y,
                    nodeWidth,
                    nodeHeight
                )
            }

            diagram.connections.forEach {
                    connection ->

                val from =
                    positions[
                        connection.fromId
                    ]

                val to =
                    positions[
                        connection.toId
                    ]

                if (
                    from != null &&
                    to != null
                ) {

                    val startY =
                        from.second +
                            nodeHeight / 2f

                    val endY =
                        to.second -
                            nodeHeight / 2f

                    canvas.drawLine(
                        from.first,
                        startY,
                        to.first,
                        endY,
                        linePaint
                    )

                    drawArrow(
                        canvas,
                        to.first,
                        endY
                    )

                    if (
                        connection.label.isNotBlank()
                    ) {

                        canvas.drawText(
                            connection.label,
                            centerX + dp(70),
                            (startY + endY) / 2f,
                            smallPaint
                        )
                    }
                }
            }
        }

        private fun drawNode(
            canvas: Canvas,
            node: SldNode,
            centerX: Float,
            centerY: Float,
            width: Float,
            height: Float
        ) {

            val left =
                centerX - width / 2f

            val top =
                centerY - height / 2f

            val right =
                centerX + width / 2f

            val bottom =
                centerY + height / 2f

            canvas.drawRoundRect(
                left,
                top,
                right,
                bottom,
                dp(10).toFloat(),
                dp(10).toFloat(),
                fillPaint
            )

            canvas.drawRoundRect(
                left,
                top,
                right,
                bottom,
                dp(10).toFloat(),
                dp(10).toFloat(),
                borderPaint
            )

            canvas.drawText(
                node.name,
                centerX,
                centerY - dp(5),
                textPaint
            )

            canvas.drawText(
                node.type.name,
                centerX,
                centerY + dp(15),
                smallPaint
            )
        }

        private fun drawArrow(
            canvas: Canvas,
            x: Float,
            y: Float
        ) {

            val path =
                Path()

            path.moveTo(
                x - dp(7),
                y - dp(10)
            )

            path.lineTo(
                x,
                y
            )

            path.lineTo(
                x + dp(7),
                y - dp(10)
            )

            canvas.drawPath(
                path,
                linePaint
            )
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

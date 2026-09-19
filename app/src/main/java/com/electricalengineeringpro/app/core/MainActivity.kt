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
import kotlin.math.abs

import com.electricalengineeringpro.app.core.calculation.CompleteDesignInput
import com.electricalengineeringpro.app.core.calculation.GeneratorInput
import com.electricalengineeringpro.app.core.calculation.MdbInput
import com.electricalengineeringpro.app.core.calculation.ProtectionInput

import com.electricalengineeringpro.app.core.model.BreakerInput
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
import com.electricalengineeringpro.app.core.model.SupplySource
import com.electricalengineeringpro.app.core.model.TransformerInput

import com.electricalengineeringpro.app.core.sld.SingleLineDiagram

class MainActivity : Activity() {

    private val core =
        ProfessionalEngineeringCore.instance

    /*
     * UI COLORS ONLY
     *
     * No engineering calculations are implemented here.
     */

    private val backgroundColor =
        Color.rgb(244, 247, 251)

    private val surfaceColor =
        Color.WHITE

    private val inputColor =
        Color.rgb(248, 250, 253)

    private val primaryColor =
        Color.rgb(21, 101, 192)

    private val primaryDarkColor =
        Color.rgb(13, 71, 161)

    private val secondaryColor =
        Color.rgb(69, 90, 100)

    private val textColor =
        Color.rgb(27, 39, 51)

    private val secondaryTextColor =
        Color.rgb(92, 107, 120)

    private val borderColor =
        Color.rgb(215, 224, 234)

    private val resultColor =
        Color.rgb(235, 247, 238)

    private val resultTextColor =
        Color.rgb(30, 105, 55)

    private val warningColor =
        Color.rgb(255, 248, 225)

    private val warningTextColor =
        Color.rgb(130, 90, 15)

    private lateinit var root: LinearLayout
    private lateinit var tabBar: LinearLayout
    private lateinit var content: LinearLayout

    private var currentInputRow: LinearLayout? =
        null

    private var currentInputCount =
        0

    private data class EngineeringTab(
        val code: String,
        val name: String,
        val icon: String
    )

    private val tabs =
        listOf(

            EngineeringTab(
                "PWR",
                "Power",
                "⚡"
            ),

            EngineeringTab(
                "LOAD",
                "Loads",
                "▦"
            ),

            EngineeringTab(
                "CBL",
                "Cable",
                "⌁"
            ),

            EngineeringTab(
                "VD",
                "Voltage Drop",
                "↘"
            ),

            EngineeringTab(
                "SC",
                "Short Circuit",
                "⚠"
            ),

            EngineeringTab(
                "BRK",
                "Breaker",
                "▣"
            ),

            EngineeringTab(
                "TR",
                "Transformer",
                "⇅"
            ),

            EngineeringTab(
                "GEN",
                "Generator",
                "◉"
            ),

            EngineeringTab(
                "MTR",
                "Motor",
                "⚙"
            ),

            EngineeringTab(
                "PMP",
                "Pump",
                "◈"
            ),

            EngineeringTab(
                "MDB",
                "MDB",
                "▤"
            ),

            EngineeringTab(
                "PROT",
                "Protection",
                "🛡"
            ),

            EngineeringTab(
                "NET",
                "Network",
                "⌘"
            ),

            EngineeringTab(
                "DES",
                "Complete Design",
                "◆"
            ),

            EngineeringTab(
                "SLD",
                "Single Line",
                "⌗"
            )
        )

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        buildUi()

        showPower()
    }

    private fun buildUi() {

        root =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setBackgroundColor(
                    backgroundColor
                )
            }

        createHeader()

        createTabBar()

        val scroll =
            ScrollView(this).apply {

                isFillViewport =
                    true

                setBackgroundColor(
                    backgroundColor
                )
            }

        content =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(12),
                    dp(12),
                    dp(12),
                    dp(28)
                )
            }

        scroll.addView(
            content
        )

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(
            root
        )

        createTabs()
    }

    private fun createHeader() {

        val header =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL

                setPadding(
                    dp(16),
                    dp(10),
                    dp(16),
                    dp(10)
                )

                setBackgroundColor(
                    surfaceColor
                )
            }

        val icon =
            TextView(this).apply {

                text =
                    "⚡"

                textSize =
                    28f

                gravity =
                    Gravity.CENTER

                setTextColor(
                    primaryColor
                )
            }

        val texts =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(10),
                    0,
                    0,
                    0
                )
            }

        val title =
            TextView(this).apply {

                text =
                    "PROFESSIONAL ENGINEERING"

                textSize =
                    18f

                setTextColor(
                    textColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD
            }

        val subtitle =
            TextView(this).apply {

                text =
                    "Electrical Design & Calculation"

                textSize =
                    10f

                setTextColor(
                    secondaryTextColor
                )
            }

        texts.addView(
            title
        )

        texts.addView(
            subtitle
        )

        header.addView(
            icon,
            LinearLayout.LayoutParams(
                dp(42),
                dp(42)
            )
        )

        header.addView(
            texts,
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
                dp(62)
            )
        )
    }

    private fun createTabBar() {

        val horizontal =
            HorizontalScrollView(this).apply {

                isHorizontalScrollBarEnabled =
                    false

                setBackgroundColor(
                    surfaceColor

                elevation =
                    dp(2).toFloat()
            }

        tabBar =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                setPadding(
                    dp(6),
                    dp(5),
                    dp(6),
                    dp(5)
                )

                setBackgroundColor(
                    surfaceColor
                )
            }

        horizontal.addView(
            tabBar
        )

        root.addView(
            horizontal,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(76)
            )
        )
    }

    private fun createTabs() {

        tabBar.removeAllViews()

        tabs.forEach { tab ->

            val item =
                LinearLayout(this).apply {

                    orientation =
                        LinearLayout.VERTICAL

                    gravity =
                        Gravity.CENTER

                    setPadding(
                        dp(3),
                        dp(3),
                        dp(3),
                        dp(3)
                    )

                    background =
                        roundedBackground(
                            surfaceColor,
                            borderColor,
                            1,
                            10
                        )

                    elevation =
                        dp(1).toFloat()

                    setOnClickListener {

                        when (tab.code) {

                            "PWR" ->
                                showPower()

                            "LOAD" ->
                                showLoad()

                            "CBL" ->
                                showCable()

                            "VD" ->
                                showVoltageDrop()

                            "SC" ->
                                showShortCircuit()

                            "BRK" ->
                                showBreaker()

                            "TR" ->
                                showTransformer()

                            "GEN" ->
                                showGenerator()

                            "MTR" ->
                                showMotor()

                            "PMP" ->
                                showPump()

                            "MDB" ->
                                showMdb()

                            "PROT" ->
                                showProtection()

                            "NET" ->
                                showNetwork()

                            "DES" ->
                                showCompleteDesign()

                            "SLD" ->
                                showSld()
                        }
                    }
                }

            val icon =
                TextView(this).apply {

                    text =
                        tab.icon

                    textSize =
                        20f

                    gravity =
                        Gravity.CENTER

                    setTextColor(
                        primaryColor
                    )

                    typeface =
                        Typeface.DEFAULT_BOLD
                }

            val code =
                TextView(this).apply {

                    text =
                        tab.code

                    textSize =
                        8f

                    gravity =
                        Gravity.CENTER

                    setTextColor(
                        textColor
                    )

                    typeface =
                        Typeface.DEFAULT_BOLD
                }

            val name =
                TextView(this).apply {

                    text =
                        tab.name

                    textSize =
                        7f

                    gravity =
                        Gravity.CENTER

                    setTextColor(
                        secondaryTextColor
                    )

                    maxLines =
                        1
                }

            item.addView(
                icon,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(25)
                )
            )

            item.addView(
                code,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(16)
                )
            )

            item.addView(
                name,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(17)
                )
            )

            tabBar.addView(
                item,
                LinearLayout.LayoutParams(
                    dp(82),
                    dp(62)
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
    }

    private fun clearContent() {

        flushInputRow()

        content.removeAllViews()

        currentInputRow =
            null

        currentInputCount =
            0
    }

    private fun page(
        title: String,
        description: String
    ) {

        clearContent()

        val titleBox =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL

                setPadding(
                    dp(4),
                    dp(4),
                    dp(4),
                    dp(2)
                )
            }

        val titleView =
            TextView(this).apply {

                text =
                    title

                textSize =
                    21f

                setTextColor(
                    textColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD
            }

        val descriptionView =
            TextView(this).apply {

                text =
                    description

                textSize =
                    10f

                setTextColor(
                    secondaryTextColor
                )

                setPadding(
                    dp(4),
                    dp(2),
                    0,
                    dp(8)
                )
            }

        titleBox.addView(
            titleView
        )

        content.addView(
            titleBox,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(34)
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

    private fun sectionTitle(
        title: String,
        description: String
    ) {

        flushInputRow()

        val box =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(12),
                    dp(9),
                    dp(12),
                    dp(9)
                )

                background =
                    roundedBackground(
                        surfaceColor,
                        borderColor,
                        1,
                        10
                    )
            }

        val titleView =
            TextView(this).apply {

                text =
                    title

                textSize =
                    14f

                setTextColor(
                    primaryDarkColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD
            }

        val descriptionView =
            TextView(this).apply {

                text =
                    description

                textSize =
                    9f

                setTextColor(
                    secondaryTextColor
                )
            }

        box.addView(
            titleView
        )

        box.addView(
            descriptionView
        )

        content.addView(
            box,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                setMargins(
                    0,
                    dp(4),
                    0,
                    dp(7)
                )
            }
        )
    }

    private fun ensureInputRow() {

        if (
            currentInputRow == null ||
            currentInputCount >= 3
        ) {

            flushInputRow()

            currentInputRow =
                LinearLayout(this).apply {

                    orientation =
                        LinearLayout.HORIZONTAL

                    gravity =
                        Gravity.TOP
                }

            currentInputCount =
                0
        }
    }

    private fun addInputCell(
        cell: View
    ) {

        ensureInputRow()

        val row =
            currentInputRow
                ?: return

        row.addView(
            cell,
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

        currentInputCount++

        if (
            currentInputCount == 3
        ) {
            flushInputRow()
        }
    }

    private fun flushInputRow() {

        val row =
            currentInputRow
                ?: return

        if (
            row.childCount == 0
        ) {

            currentInputRow =
                null

            currentInputCount =
                0

            return
        }

        while (
            row.childCount < 3
        ) {

            val spacer =
                View(this)

            row.addView(
                spacer,
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

        currentInputRow =
            null

        currentInputCount =
            0
    }

    private fun field(
        label: String,
        unit: String,
        initial: String
    ): EditText {

        val container =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(7),
                    dp(5),
                    dp(7),
                    dp(5)
                )

                background =
                    roundedBackground(
                        surfaceColor,
                        borderColor,
                        1,
                        8
                    )
            }

        val labelView =
            TextView(this).apply {

                text =
                    if (
                        unit.isBlank()
                    ) {
                        label
                    } else {
                        "$label\n($unit)"
                    }

                textSize =
                    9f

                setTextColor(
                    secondaryTextColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        val edit =
            EditText(this).apply {

                setText(
                    initial
                )

                setTextColor(
                    textColor
                )

                textSize =
                    14f

                inputType =
                    InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL or
                        InputType.TYPE_NUMBER_FLAG_SIGNED

                setSingleLine(
                    true
                )

                setPadding(
                    dp(7),
                    0,
                    dp(7),
                    0
                )

                background =
                    roundedBackground(
                        inputColor,
                        borderColor,
                        1,
                        6
                    )
            }

        container.addView(
            labelView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(31)
            )
        )

        container.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(40)
            )
        )

        addInputCell(
            container
        )

        return edit
    }

    private fun textField(
        label: String,
        initial: String
    ): EditText {

        val container =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(7),
                    dp(5),
                    dp(7),
                    dp(5)
                )

                background =
                    roundedBackground(
                        surfaceColor,
                        borderColor,
                        1,
                        8
                    )
            }

        val labelView =
            TextView(this).apply {

                text =
                    label

                textSize =
                    9f

                setTextColor(
                    secondaryTextColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        val edit =
            EditText(this).apply {

                setText(
                    initial
                )

                setTextColor(
                    textColor
                )

                textSize =
                    14f

                inputType =
                    InputType.TYPE_CLASS_TEXT

                setSingleLine(
                    true
                )

                setPadding(
                    dp(7),
                    0,
                    dp(7),
                    0
                )

                background =
                    roundedBackground(
                        inputColor,
                        borderColor,
                        1,
                        6
                    )
            }

        container.addView(
            labelView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(31)
            )
        )

        container.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(40)
            )
        )

        addInputCell(
            container
        )

        return edit
    }

    private fun spinnerField(
        label: String,
        values: List<String>,
        selectedIndex: Int = 0
    ): Spinner {

        val container =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(7),
                    dp(5),
                    dp(7),
                    dp(5)
                )

                background =
                    roundedBackground(
                        surfaceColor,
                        borderColor,
                        1,
                        8
                    )
            }

        val labelView =
            TextView(this).apply {

                text =
                    label

                textSize =
                    9f

                setTextColor(
                    secondaryTextColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        val spinner =
            Spinner(this).apply {

                background =
                    roundedBackground(
                        inputColor,
                        borderColor,
                        1,
                        6
                    )

                adapter =
                    ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        values
                    )

                if (
                    values.isNotEmpty()
                ) {
                    setSelection(
                        selectedIndex.coerceIn(
                            0,
                            values.lastIndex
                        )
                    )
                }
            }

        container.addView(
            labelView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(31)
            )
        )

        container.addView(
            spinner,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(40)
            )
        )

        addInputCell(
            container
        )

        return spinner
    }

    private fun actionButton(
        title: String,
        action: () -> Unit
    ) {

        flushInputRow()

        val button =
            Button(this).apply {

                text =
                    title

                textSize =
                    11f

                setTextColor(
                    Color.WHITE
                )

                typeface =
                    Typeface.DEFAULT_BOLD

                background =
                    roundedBackground(
                        primaryColor,
                        primaryDarkColor,
                        1,
                        10
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

    private fun result(
        title: String,
        value: String
    ) {

        flushInputRow()

        val row =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL

                setPadding(
                    dp(11),
                    dp(8),
                    dp(11),
                    dp(8)
                )

                background =
                    roundedBackground(
                        resultColor,
                        Color.rgb(190, 220, 197),
                        1,
                        9
                    )
            }

        val titleView =
            TextView(this).apply {

                text =
                    title

                textSize =
                    10f

                setTextColor(
                    resultTextColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD
            }

        val valueView =
            TextView(this).apply {

                text =
                    value

                textSize =
                    13f

                setTextColor(
                    textColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD

                gravity =
                    Gravity.END
            }

        row.addView(
            titleView,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        row.addView(
            valueView,
            LinearLayout.LayoutParams(
                dp(160),
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

    private fun error(
        message: String
    ) {

        flushInputRow()

        val box =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(12),
                    dp(10),
                    dp(12),
                    dp(10)
                )

                background =
                    roundedBackground(
                        warningColor,
                        Color.rgb(240, 210, 150),
                        1,
                        9
                    )
            }

        val title =
            TextView(this).apply {

                text =
                    "INPUT / CALCULATION MESSAGE"

                textSize =
                    10f

                setTextColor(
                    warningTextColor
                )

                typeface =
                    Typeface.DEFAULT_BOLD
            }

        val body =
            TextView(this).apply {

                text =
                    message

                textSize =
                    11f

                setTextColor(
                    warningTextColor
                )

                setPadding(
                    0,
                    dp(4),
                    0,
                    0
                )
            }

        box.addView(
            title
        )

        box.addView(
            body
        )

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

    private fun space(
        height: Int
    ) {

        flushInputRow()

        content.addView(
            View(this),
            LinearLayout.LayoutParams(
                1,
                dp(height)
            )
        )
    }

    private fun value(
        field: EditText
    ): Double {

        return field.text
            .toString()
            .trim()
            .replace(",", ".")
            .toDouble()
    }

    private fun fmt(
        number: Double
    ): String {

        return if (
            abs(number) >= 1000.0
        ) {
            String.format(
                "%.0f",
                number
            )
        } else {
            String.format(
                "%.2f",
                number
            )
        }
    }

    private fun roundedBackground(
        fill: Int,
        stroke: Int,
        strokeWidth: Int,
        radiusDp: Int
    ): GradientDrawable {

        return GradientDrawable().apply {

            setColor(
                fill
            )

            setStroke(
                dp(strokeWidth),
                stroke
            )

            cornerRadius =
                dp(radiusDp).toFloat()
        }
    }

    /*
     * ============================================================
     * POWER
     * ============================================================
     */

    private fun showPower() {

        page(
            "POWER",
            "Electrical power, current and apparent power"
        )

        sectionTitle(
            "POWER INPUT",
            "Calculate electrical quantities using the ProfessionalEngineeringCore PowerCalculator"
        )

        val power =
            field(
                "Active Power",
                "kW",
                "100"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.90"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        actionButton(
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

                val r =
                    core.power.fromKw(
                        powerKw =
                            value(power),
                        voltage =
                            value(voltage),
                        powerFactor =
                            value(pf),
                        phase =
                            selectedPhase
                    )

                result(
                    "ACTIVE POWER",
                    "${fmt(r.activePowerKw)} kW"
                )

                result(
                    "APPARENT POWER",
                    "${fmt(r.apparentPowerKva)} kVA"
                )

                result(
                    "CURRENT",
                    "${fmt(r.currentA)} A"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Invalid power input"
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

        page(
            "LOAD",
            "Electrical load calculation and demand assessment"
        )

        sectionTitle(
            "LOAD DATA",
            "Load power, quantity, demand and diversity parameters"
        )

        val name =
            textField(
                "Load Name",
                "MAIN LOAD"
            )

        val type =
            spinnerField(
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
            field(
                "Quantity",
                "",
                "1"
            )

        val power =
            field(
                "Unit Power",
                "kW",
                "100"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.90"
            )

        val efficiency =
            field(
                "Efficiency",
                "",
                "1.00"
            )

        val demand =
            field(
                "Demand Factor",
                "",
                "0.80"
            )

        val diversity =
            field(
                "Diversity Factor",
                "",
                "1.00"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        actionButton(
            "CALCULATE LOAD"
        ) {

            try {

                val selectedType =
                    when (
                        type.selectedItemPosition
                    ) {

                        0 ->
                            LoadType.LIGHTING

                        1 ->
                            LoadType.SOCKET

                        2 ->
                            LoadType.HVAC

                        3 ->
                            LoadType.MOTOR

                        4 ->
                            LoadType.PUMP

                        5 ->
                            LoadType.FIRE_PUMP

                        6 ->
                            LoadType.ELEVATOR

                        7 ->
                            LoadType.MECHANICAL

                        else ->
                            LoadType.MISCELLANEOUS
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

                        type =
                            selectedType,

                        quantity =
                            value(quantity)
                                .toInt(),

                        powerKw =
                            value(power),

                        powerFactor =
                            value(pf),

                        efficiency =
                            value(efficiency),

                        demandFactor =
                            value(demand),

                        diversityFactor =
                            value(diversity),

                        voltage =
                            value(voltage),

                        phase =
                            Phase.THREE
                    )

                val r =
                    core.loads.calculate(
                        load
                    )

                result(
                    "CONNECTED LOAD",
                    "${fmt(r.connectedKw)} kW"
                )

                result(
                    "DEMAND LOAD",
                    "${fmt(r.demandKw)} kW"
                )

                result(
                    "DESIGN LOAD",
                    "${fmt(r.designKw)} kW"
                )

                result(
                    "APPARENT POWER",
                    "${fmt(r.apparentPowerKva)} kVA"
                )

                result(
                    "REACTIVE POWER",
                    "${fmt(r.reactivePowerKvar)} kVAr"
                )

                result(
                    "DESIGN CURRENT",
                    "${fmt(r.currentA)} A"
                )

                result(
                    "STARTING CURRENT",
                    "${fmt(r.startingCurrentA)} A"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Invalid load input"
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

        page(
            "CABLE",
            "Cable sizing and selection"
        )

        sectionTitle(
            "CABLE DESIGN INPUT",
            "Select conductor material, insulation and installation method"
        )

        val current =
            field(
                "Design Current",
                "A",
                "160"
            )

        val length =
            field(
                "Cable Length",
                "m",
                "50"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.90"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        val material =
            spinnerField(
                "Conductor Material",
                listOf(
                    "Copper",
                    "Aluminium"
                )
            )

        val insulation =
            spinnerField(
                "Insulation",
                listOf(
                    "PVC",
                    "XLPE",
                    "EPR"
                )
            )

        val installation =
            spinnerField(
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
            field(
                "Ambient Factor",
                "",
                "1.00"
            )

        val grouping =
            field(
                "Grouping Factor",
                "",
                "1.00"
            )

        val targetDrop =
            field(
                "Target Voltage Drop",
                "%",
                "3.00"
            )

        actionButton(
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

                        0 ->
                            CableInsulation.PVC

                        1 ->
                            CableInsulation.XLPE

                        else ->
                            CableInsulation.EPR
                    }

                val selectedInstallation =
                    when (
                        installation.selectedItemPosition
                    ) {

                        0 ->
                            InstallationMethod.CONDUIT

                        1 ->
                            InstallationMethod.TRAY

                        2 ->
                            InstallationMethod.LADDER

                        3 ->
                            InstallationMethod.DUCT

                        4 ->
                            InstallationMethod.BURIED

                        else ->
                            InstallationMethod.FREE_AIR
                    }

                val input =
                    CableInput(
                        designCurrentA =
                            value(current),

                        lengthM =
                            value(length),

                        voltage =
                            value(voltage),

                        powerFactor =
                            value(pf),

                        phase =
                            selectedPhase,

                        material =
                            selectedMaterial,

                        insulation =
                            selectedInsulation,

                        installationMethod =
                            selectedInstallation,

                        ambientFactor =
                            value(ambient),

                        groupingFactor =
                            value(grouping),

                        targetVoltageDropPercent =
                            value(targetDrop)
                    )

                val r =
                    core.cable.calculate(
                        input
                    )

                result(
                    "SELECTED CABLE",
                    "${fmt(r.selectedSizeMm2)} mm²"
                )

                result(
                    "AMPACITY",
                    "${fmt(r.ampacityA)} A"
                )

                result(
                    "VOLTAGE DROP",
                    "${fmt(r.voltageDropPercent)} %"
                )

                result(
                    "UTILIZATION",
                    "${fmt(r.utilizationPercent)} %"
                )

                result(
                    "CONDUCTOR",
                    r.conductorDescription
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Cable calculation failed"
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

        page(
            "VOLTAGE DROP",
            "Voltage drop verification"
        )

        sectionTitle(
            "VOLTAGE DROP INPUT",
            "Electrical and cable impedance data"
        )

        val current =
            field(
                "Current",
                "A",
                "160"
            )

        val length =
            field(
                "Length",
                "m",
                "50"
            )

        val resistance =
            field(
                "Resistance",
                "Ω/km",
                "0.125"
            )

        val reactance =
            field(
                "Reactance",
                "Ω/km",
                "0.080"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.90"
            )

        val maximum =
            field(
                "Maximum Drop",
                "%",
                "3.00"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        actionButton(
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

                val r =
                    core.voltageDrop.calculate(
                        currentA =
                            value(current),

                        lengthM =
                            value(length),

                        resistanceOhmPerKm =
                            value(resistance),

                        reactanceOhmPerKm =
                            value(reactance),

                        voltage =
                            value(voltage),

                        powerFactor =
                            value(pf),

                        phase =
                            selectedPhase,

                        maximumPercent =
                            value(maximum)
                    )

                result(
                    "VOLTAGE DROP",
                    "${fmt(r.dropVolts)} V"
                )

                result(
                    "DROP PERCENT",
                    "${fmt(r.dropPercent)} %"
                )

                result(
                    "STATUS",
                    if (r.compliant) {
                        "COMPLIANT"
                    } else {
                        "NOT COMPLIANT"
                    }
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Voltage drop calculation failed"
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

        page(
            "SHORT CIRCUIT",
            "Three-phase short-circuit current calculation"
        )

        sectionTitle(
            "FAULT LEVEL INPUT",
            "Transformer rating, impedance and upstream fault level"
        )

        val voltage =
            field(
                "System Voltage",
                "V",
                "400"
            )

        val transformer =
            field(
                "Transformer Rating",
                "kVA",
                "1000"
            )

        val impedance =
            field(
                "Transformer Impedance",
                "%",
                "6.00"
            )

        val sourceMva =
            field(
                "Source Short Circuit",
                "MVA",
                "0"
            )

        actionButton(
            "CALCULATE SHORT CIRCUIT"
        ) {

            try {

                val sourceValue =
                    value(sourceMva)

                val input =
                    ShortCircuitInput(
                        sourceVoltage =
                            value(voltage),

                        transformerKva =
                            value(transformer),

                        transformerImpedancePercent =
                            value(impedance),

                        sourceShortCircuitMva =
                            if (
                                sourceValue > 0.0
                            ) {
                                sourceValue
                            } else {
                                null
                            }
                    )

                val r =
                    core.shortCircuit.calculate(
                        input
                    )

                result(
                    "FAULT CURRENT",
                    "${fmt(r.faultCurrentKA)} kA"
                )

                result(
                    "FAULT LEVEL",
                    "${fmt(r.faultMva)} MVA"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Short-circuit calculation failed"
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

        page(
            "BREAKER",
            "Circuit breaker selection"
        )

        sectionTitle(
            "BREAKER INPUT",
            "Design current and available short-circuit current"
        )

        val current =
            field(
                "Design Current",
                "A",
                "160"
            )

        val shortCircuit =
            field(
                "Short Circuit",
                "kA",
                "25"
            )

        val type =
            spinnerField(
                "Preferred Breaker",
                listOf(
                    "MCB",
                    "MCCB",
                    "ACB",
                    "RCBO",
                    "RCD"
                )
            )

        actionButton(
            "SELECT BREAKER"
        ) {

            try {

                val preferred =
                    when (
                        type.selectedItemPosition
                    ) {

                        0 ->
                            com.electricalengineeringpro.app.core.model.BreakerType.MCB

                        1 ->
                            com.electricalengineeringpro.app.core.model.BreakerType.MCCB

                        2 ->
                            com.electricalengineeringpro.app.core.model.BreakerType.ACB

                        3 ->
                            com.electricalengineeringpro.app.core.model.BreakerType.RCBO

                        else ->
                            com.electricalengineeringpro.app.core.model.BreakerType.RCD
                    }

                val r =
                    core.breaker.calculate(
                        BreakerInput(
                            designCurrentA =
                                value(current),

                            shortCircuitCurrentKA =
                                value(shortCircuit),

                            preferredType =
                                preferred
                        )
                    )

                result(
                    "BREAKER TYPE",
                    r.type.name
                )

                result(
                    "RATED CURRENT",
                    "${fmt(r.ratedCurrentA)} A"
                )

                result(
                    "BREAKING CAPACITY",
                    "${fmt(r.breakingCapacityKA)} kA"
                )

                result(
                    "UTILIZATION",
                    "${fmt(r.utilizationPercent)} %"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Breaker calculation failed"
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

        page(
            "TRANSFORMER",
            "Transformer current and fault calculation"
        )

        sectionTitle(
            "TRANSFORMER DATA",
            "Transformer rating, primary/secondary voltage and impedance"
        )

        val rating =
            field(
                "Transformer Rating",
                "kVA",
                "1000"
            )

        val primary =
            field(
                "Primary Voltage",
                "V",
                "11000"
            )

        val secondary =
            field(
                "Secondary Voltage",
                "V",
                "400"
            )

        val impedance =
            field(
                "Impedance",
                "%",
                "6.00"
            )

        actionButton(
            "CALCULATE TRANSFORMER"
        ) {

            try {

                val r =
                    core.transformer.calculate(
                        TransformerInput(
                            ratingKva =
                                value(rating),

                            primaryVoltage =
                                value(primary),

                            secondaryVoltage =
                                value(secondary),

                            impedancePercent =
                                value(impedance)
                        )
                    )

                result(
                    "PRIMARY CURRENT",
                    "${fmt(r.primaryCurrentA)} A"
                )

                result(
                    "SECONDARY CURRENT",
                    "${fmt(r.secondaryCurrentA)} A"
                )

                result(
                    "SHORT CIRCUIT",
                    "${fmt(r.shortCircuitCurrentKA)} kA"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Transformer calculation failed"
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

        page(
            "GENERATOR",
            "Generator rating, current and breaker selection"
        )

        sectionTitle(
            "GENERATOR DATA",
            "Generator nameplate and design parameters"
        )

        val rating =
            field(
                "Generator Rating",
                "kVA",
                "500"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.80"
            )

        val efficiency =
            field(
                "Efficiency",
                "",
                "0.90"
            )

        val margin =
            field(
                "Design Margin",
                "",
                "1.25"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        actionButton(
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

                val r =
                    core.generators.calculate(
                        GeneratorInput(
                            ratingKva =
                                value(rating),

                            voltageV =
                                value(voltage),

                            powerFactor =
                                value(pf),

                            phase =
                                selectedPhase,

                            efficiency =
                                value(efficiency),

                            designMarginFactor =
                                value(margin)
                        )
                    )

                result(
                    "ACTIVE POWER",
                    "${fmt(r.activePowerKw)} kW"
                )

                result(
                    "FULL LOAD CURRENT",
                    "${fmt(r.fullLoadCurrentA)} A"
                )

                result(
                    "RECOMMENDED BREAKER",
                    "${fmt(r.recommendedBreakerA)} A"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Generator calculation failed"
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

        page(
            "MOTOR",
            "Motor full-load and starting current"
        )

        sectionTitle(
            "MOTOR DATA",
            "Motor power, voltage, power factor, efficiency and starting multiplier"
        )

        val power =
            field(
                "Motor Power",
                "kW",
                "75"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.85"
            )

        val efficiency =
            field(
                "Efficiency",
                "",
                "0.92"
            )

        val multiplier =
            field(
                "Starting Multiplier",
                "×",
                "6.00"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        actionButton(
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

                val r =
                    core.motors.calculate(
                        MotorInput(
                            powerKw =
                                value(power),

                            voltage =
                                value(voltage),

                            powerFactor =
                                value(pf),

                            efficiency =
                                value(efficiency),

                            phase =
                                selectedPhase,

                            startingMultiplier =
                                value(multiplier)
                        )
                    )

                result(
                    "FULL LOAD CURRENT",
                    "${fmt(r.fullLoadCurrentA)} A"
                )

                result(
                    "STARTING CURRENT",
                    "${fmt(r.startingCurrentA)} A"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Motor calculation failed"
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

        page(
            "PUMP",
            "Pump hydraulic power and electrical motor current"
        )

        sectionTitle(
            "PUMP DATA",
            "Flow, head, pump efficiency, motor efficiency and electrical parameters"
        )

        val flow =
            field(
                "Flow",
                "m³/s",
                "0.300"
            )

        val head =
            field(
                "Head",
                "m",
                "7.00"
            )

        val pumpEfficiency =
            field(
                "Pump Efficiency",
                "",
                "0.80"
            )

        val motorEfficiency =
            field(
                "Motor Efficiency",
                "",
                "0.92"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.85"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        actionButton(
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

                val r =
                    core.pumps.calculate(
                        PumpInput(
                            flowM3s =
                                value(flow),

                            headM =
                                value(head),

                            pumpEfficiency =
                                value(pumpEfficiency),

                            motorEfficiency =
                                value(motorEfficiency),

                            powerFactor =
                                value(pf),

                            voltage =
                                value(voltage),

                            phase =
                                selectedPhase
                        )
                    )

                result(
                    "HYDRAULIC POWER",
                    "${fmt(r.hydraulicPowerKw)} kW"
                )

                result(
                    "MOTOR POWER",
                    "${fmt(r.motorPowerKw)} kW"
                )

                result(
                    "MOTOR CURRENT",
                    "${fmt(r.currentA)} A"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Pump calculation failed"
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

        page(
            "MDB",
            "Main distribution board sizing"
        )

        sectionTitle(
            "MDB DATA",
            "Connected load, demand factor, power factor and spare capacity"
        )

        val connected =
            field(
                "Connected Load",
                "kW",
                "1000"
            )

        val demand =
            field(
                "Demand Factor",
                "",
                "0.80"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.90"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val spare =
            field(
                "Spare Capacity",
                "",
                "0.20"
            )

        actionButton(
            "CALCULATE MDB"
        ) {

            try {

                val r =
                    core.mdb.calculate(
                        MdbInput(
                            connectedLoadKw =
                                value(connected),

                            demandFactor =
                                value(demand),

                            powerFactor =
                                value(pf),

                            voltageV =
                                value(voltage),

                            spareCapacity =
                                value(spare)
                        )
                    )

                result(
                    "DEMAND LOAD",
                    "${fmt(r.demandLoadKw)} kW"
                )

                result(
                    "APPARENT POWER",
                    "${fmt(r.apparentPowerKva)} kVA"
                )

                result(
                    "DESIGN CURRENT",
                    "${fmt(r.designCurrentA)} A"
                )

                result(
                    "RECOMMENDED INCOMER",
                    "${fmt(r.recommendedIncomerA)} A"
                )

                result(
                    "RECOMMENDED BUSBAR",
                    "${fmt(r.recommendedBusbarA)} A"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "MDB calculation failed"
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

        page(
            "PROTECTION",
            "Protection coordination input check"
        )

        sectionTitle(
            "PROTECTION DATA",
            "Breaker current, cable ampacity and short-circuit withstand"
        )

        val current =
            field(
                "Design Current",
                "A",
                "160"
            )

        val ampacity =
            field(
                "Cable Ampacity",
                "A",
                "200"
            )

        val shortCircuit =
            field(
                "Short Circuit",
                "kA",
                "25"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val margin =
            field(
                "Protection Margin",
                "×",
                "1.25"
            )

        actionButton(
            "CHECK PROTECTION"
        ) {

            try {

                val r =
                    core.protection.calculate(
                        ProtectionInput(
                            designCurrentA =
                                value(current),

                            cableAmpacityA =
                                value(ampacity),

                            shortCircuitKA =
                                value(shortCircuit),

                            voltageV =
                                value(voltage),

                            margin =
                                value(margin)
                        )
                    )

                result(
                    "RECOMMENDED BREAKER",
                    "${fmt(r.recommendedBreakerA)} A"
                )

                result(
                    "BREAKING CAPACITY",
                    "${fmt(r.breakingCapacityKA)} kA"
                )

                result(
                    "CABLE PROTECTED",
                    if (r.cableProtected) {
                        "YES"
                    } else {
                        "NO"
                    }
                )

                result(
                    "SHORT CIRCUIT PROTECTED",
                    if (r.shortCircuitProtected) {
                        "YES"
                    } else {
                        "NO"
                    }
                )

                result(
                    "STATUS",
                    r.status
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Protection calculation failed"
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

        page(
            "NETWORK",
            "Electrical network summary"
        )

        sectionTitle(
            "NETWORK INPUT",
            "Main connected load and network operating parameters"
        )

        val connected =
            field(
                "Connected Load",
                "kW",
                "1000"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.90"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        actionButton(
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

                val electricalLoad =
                    ElectricalLoad(
                        name =
                            "MAIN LOAD",

                        type =
                            LoadType.MISCELLANEOUS,

                        quantity =
                            1,

                        powerKw =
                            value(connected),

                        powerFactor =
                            value(pf),

                        demandFactor =
                            0.80,

                        voltage =
                            value(voltage),

                        phase =
                            selectedPhase
                    )

                val r =
                    core.network.calculate(
                        loads =
                            listOf(
                                electricalLoad
                            ),

                        voltageV =
                            value(voltage),

                        powerFactor =
                            value(pf),

                        phase =
                            selectedPhase
                    )

                result(
                    "CONNECTED LOAD",
                    "${fmt(r.totalConnectedKw)} kW"
                )

                result(
                    "DEMAND LOAD",
                    "${fmt(r.totalDemandKw)} kW"
                )

                result(
                    "DESIGN LOAD",
                    "${fmt(r.totalDesignKw)} kW"
                )

                result(
                    "APPARENT POWER",
                    "${fmt(r.totalApparentPowerKva)} kVA"
                )

                result(
                    "MAIN CURRENT",
                    "${fmt(r.mainCurrentA)} A"
                )

                result(
                    "ESTIMATED TRANSFORMER",
                    "${fmt(r.estimatedTransformerKva)} kVA"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Network calculation failed"
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

        page(
            "COMPLETE DESIGN",
            "Integrated electrical design summary"
        )

        sectionTitle(
            "DESIGN INPUT",
            "The existing CompleteDesignCalculator performs the engineering calculations"
        )

        val connected =
            field(
                "Connected Load",
                "kW",
                "1000"
            )

        val pf =
            field(
                "Power Factor",
                "",
                "0.90"
            )

        val voltage =
            field(
                "Voltage",
                "V",
                "400"
            )

        val diversity =
            field(
                "Diversity Factor",
                "",
                "1.15"
            )

        val shortCircuit =
            field(
                "Short Circuit",
                "kA",
                "25"
            )

        val phase =
            spinnerField(
                "Phase",
                listOf(
                    "Three Phase",
                    "Single Phase"
                )
            )

        actionButton(
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
                        name =
                            "MAIN LOAD",

                        type =
                            LoadType.MISCELLANEOUS,

                        quantity =
                            1,

                        powerKw =
                            value(connected),

                        powerFactor =
                            value(pf),

                        demandFactor =
                            0.80,

                        voltage =
                            value(voltage),

                        phase =
                            selectedPhase
                    )

                val r =
                    core.completeDesign.calculate(
                        CompleteDesignInput(
                            loads =
                                listOf(
                                    load
                                ),

                            voltageV =
                                value(voltage),

                            powerFactor =
                                value(pf),

                            diversityFactor =
                                value(diversity),

                            shortCircuitKA =
                                value(shortCircuit),

                            phase =
                                selectedPhase
                        )
                    )

                result(
                    "CONNECTED LOAD",
                    "${fmt(r.connectedLoadKW)} kW"
                )

                result(
                    "DEMAND LOAD",
                    "${fmt(r.demandLoadKW)} kW"
                )

                result(
                    "DESIGN LOAD",
                    "${fmt(r.designLoadKW)} kW"
                )

                result(
                    "APPARENT POWER",
                    "${fmt(r.apparentPowerKVA)} kVA"
                )

                result(
                    "MAIN CURRENT",
                    "${fmt(r.mainCurrentA)} A"
                )

                result(
                    "TRANSFORMER REQUIRED",
                    "${fmt(r.transformerRequiredKVA)} kVA"
                )

                result(
                    "RECOMMENDED TRANSFORMER",
                    "${fmt(r.transformerRecommendedKVA)} kVA"
                )

                result(
                    "MAIN BREAKER",
                    "${fmt(r.mainBreakerA)} A"
                )

                result(
                    "BREAKER CAPACITY",
                    "${fmt(r.breakerBreakingCapacityKA)} kA"
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "Complete design failed"
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

        page(
            "SINGLE LINE DIAGRAM",
            "Generate and display the electrical single-line diagram"
        )

        sectionTitle(
            "SLD SOURCE",
            "Select the electrical supply source and define the main distribution"
        )

        val source =
            spinnerField(
                "Supply Source",
                listOf(
                    "Utility",
                    "Transformer",
                    "Generator",
                    "Transformer + Generator"
                )
            )

        val rating =
            field(
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

        actionButton(
            "GENERATE SLD"
        ) {

            try {

                val sourceName =
                    source.selectedItem
                        .toString()

                val panelName =
                    panel.text
                        .toString()
                        .trim()
                        .ifEmpty {
                            "MDB"
                        }

                val feederName =
                    feeder.text
                        .toString()
                        .trim()
                        .ifEmpty {
                            "FEEDER-01"
                        }

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
                        id =
                            "SOURCE",

                        name =
                            sourceName,

                        type =
                            sourceType,

                        ratingKva =
                            value(rating)
                    )

                val panelElement =
                    NetworkElement(
                        id =
                            "MDB",

                        name =
                            panelName,

                        type =
                            NetworkElementType.PANEL,

                        parentId =
                            sourceElement.id,

                        ratingKva =
                            value(rating)
                    )

                val feederElement =
                    NetworkElement(
                        id =
                            "FEEDER-01",

                        name =
                            feederName,

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
                            listOf(
                                panelElement
                            ),

                        feeders =
                            listOf(
                                feederElement
                            )
                    )

                result(
                    "SLD STATUS",
                    "GENERATED"
                )

                result(
                    "NODES",
                    diagram.nodes.size.toString()
                )

                result(
                    "CONNECTIONS",
                    diagram.connections.size.toString()
                )

                renderSld(
                    diagram
                )

            } catch (e: Exception) {

                error(
                    e.message
                        ?: "SLD generation failed"
                )
            }
        }
    }

    private fun renderSld(
        diagram: SingleLineDiagram
    ) {

        sectionTitle(
            "SINGLE LINE DIAGRAM",
            "Generated electrical network"
        )

        val view =
            SldView(
                diagram
            )

        content.addView(
            view,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(650)
            ).apply {

                setMargins(
                    dp(3),
                    dp(3),
                    dp(3),
                    dp(10)
                )
            }
        )

        diagram.nodes.forEachIndexed {
                index,
                node ->

            result(
                "NODE ${index + 1}",
                "${node.name} • ${node.type}"
            )
        }

        diagram.connections.forEachIndexed {
                index,
                connection ->

            result(
                "CONNECTION ${index + 1}",
                "${connection.fromId} → ${connection.toId}"
            )
        }
    }

    /*
     * ============================================================
     * SLD VIEW
     * ============================================================
     *
     * This section only renders the already-generated SLD model.
     * It does not perform electrical calculations.
     */

    private inner class SldView(
        private val diagram: SingleLineDiagram
    ) : View(this) {

        private val linePaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                color =
                    primaryColor

                strokeWidth =
                    dp(3).toFloat()

                style =
                    Paint.Style.STROKE
            }

        private val nodePaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                color =
                    surfaceColor

                style =
                    Paint.Style.FILL
            }

        private val borderPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                color =
                    primaryColor

                strokeWidth =
                    dp(2).toFloat()

                style =
                    Paint.Style.STROKE
            }

        private val textPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                color =
                    textColor

                textSize =
                    dp(12).toFloat()

                typeface =
                    Typeface.DEFAULT_BOLD

                textAlign =
                    Paint.Align.CENTER
            }

        private val smallTextPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                color =
                    secondaryTextColor

                textSize =
                    dp(9).toFloat()

                textAlign =
                    Paint.Align.CENTER
            }

        override fun onDraw(
            canvas: Canvas
        ) {

            super.onDraw(
                canvas
            )

            canvas.drawColor(
                backgroundColor
            )

            if (
                diagram.nodes.isEmpty()
            ) {
                return
            }

            val centerX =
                width / 2f

            val nodeWidth =
                dp(185).toFloat()

            val nodeHeight =
                dp(70).toFloat()

            val startTop =
                dp(60).toFloat()

            val gap =
                dp(125).toFloat()

            val positions =
                mutableMapOf<
                    String,
                    Pair<Float, Float>
                >()

            diagram.nodes.forEachIndexed {
                    index,
                    node ->

                val x =
                    centerX

                val y =
                    startTop +
                        index * gap

                positions[
                    node.id
                ] =
                    Pair(
                        x,
                        y
                    )

                drawNode(
                    canvas =
                        canvas,

                    node =
                        node,

                    centerX =
                        x,

                    centerY =
                        y,

                    width =
                        nodeWidth,

                    height =
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
                        connection.label
                            .isNotBlank()
                    ) {

                        canvas.drawText(
                            connection.label,
                            centerX +
                                dp(65),
                            (startY + endY) / 2f,
                            smallTextPaint
                        )
                    }
                }
            }
        }

        private fun drawNode(
            canvas: Canvas,
            node: com.electricalengineeringpro.app.core.sld.SldNode,
            centerX: Float,
            centerY: Float,
            width: Float,
            height: Float
        ) {

            val left =
                centerX -
                    width / 2f

            val top =
                centerY -
                    height / 2f

            val right =
                centerX +
                    width / 2f

            val bottom =
                centerY +
                    height / 2f

            canvas.drawRoundRect(
                left,
                top,
                right,
                bottom,
                dp(10).toFloat(),
                dp(10).toFloat(),
                nodePaint
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
                smallTextPaint
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
                resources
                    .displayMetrics
                    .density
            ).toInt()
    }
                    }

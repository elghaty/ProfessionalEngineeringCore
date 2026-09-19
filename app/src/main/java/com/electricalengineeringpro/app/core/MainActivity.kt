package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.electricalengineeringpro.app.core.calculation.BreakerCalculator
import com.electricalengineeringpro.app.core.calculation.CompleteDesignCalculator
import com.electricalengineeringpro.app.core.calculation.GeneratorCalculator
import com.electricalengineeringpro.app.core.calculation.MdbCalculator
import com.electricalengineeringpro.app.core.calculation.MotorCalculator
import com.electricalengineeringpro.app.core.calculation.PowerCalculator
import com.electricalengineeringpro.app.core.calculation.ProtectionCalculator
import com.electricalengineeringpro.app.core.calculation.PumpCalculator
import com.electricalengineeringpro.app.core.calculation.ShortCircuitCalculator
import com.electricalengineeringpro.app.core.calculation.TransformerCalculator
import com.electricalengineeringpro.app.core.calculation.VoltageDropCalculator
import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.CableInsulation
import com.electricalengineeringpro.app.core.model.ConductorMaterial
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.InstallationMethod
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.MotorInput
import com.electricalengineeringpro.app.core.model.MdbInput
import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.PumpInput
import com.electricalengineeringpro.app.core.model.ShortCircuitInput
import com.electricalengineeringpro.app.core.model.SupplySource
import com.electricalengineeringpro.app.core.model.TransformerInput
import com.electricalengineeringpro.app.core.sld.SingleLineDiagram
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : Activity() {

    private val core = ProfessionalEngineeringCore.instance

    private lateinit var content: LinearLayout
    private lateinit var tabContainer: LinearLayout
    private lateinit var tabScroll: HorizontalScrollView

    private val bg = Color.rgb(7, 14, 24)
    private val panel = Color.rgb(15, 27, 42)
    private val panel2 = Color.rgb(20, 35, 52)
    private val inputBg = Color.rgb(25, 42, 60)
    private val resultBg = Color.rgb(18, 38, 54)
    private val accent = Color.rgb(0, 190, 175)
    private val accentDark = Color.rgb(0, 105, 100)
    private val white = Color.WHITE
    private val gray = Color.rgb(170, 185, 200)
    private val warning = Color.rgb(245, 180, 60)

    private val tabs = mutableListOf<LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        buildInterface()
        selectTab(0)
        showPower()
    }

    private fun buildInterface() {

        val outerScroll = ScrollView(this).apply {
            isFillViewport = true
            setBackgroundColor(bg)
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(24)
            )
        }

        root.addView(buildHeader())

        val section = TextView(this).apply {
            text = "ENGINEERING TOOLS"
            textSize = 11f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(4), dp(8), 0, dp(6))
        }

        root.addView(section)

        tabScroll = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
            setBackgroundColor(panel)
            setPadding(dp(4), dp(3), dp(4), dp(3))
        }

        tabContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        tabScroll.addView(tabContainer)

        root.addView(
            tabScroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(76)
            )
        )

        createTabs()

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(4), 0, 0)
        }

        root.addView(
            content,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(buildFooter())

        outerScroll.addView(root)
        setContentView(outerScroll)
    }

    private fun buildHeader(): View {

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(4), dp(2), dp(4), dp(8))
        }

        val title = TextView(this).apply {
            text = "PROFESSIONAL ENGINEERING"
            textSize = 22f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        header.addView(title)

        val subtitle = TextView(this).apply {
            text = "Electrical Design & Calculation System"
            textSize = 12f
            setTextColor(gray)
            setPadding(0, dp(3), 0, 0)
        }

        header.addView(subtitle)

        val owner = TextView(this).apply {
            text = "Engineer: Abdelraouf Elghaty"
            textSize = 10f
            setTextColor(accent)
            setPadding(0, dp(4), 0, 0)
        }

        header.addView(owner)

        return header
    }

    private fun buildFooter(): View {

        return TextView(this).apply {
            text = "Professional Engineering Core\n© Eng. Abdelraouf Elghaty"
            textSize = 10f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, dp(28), 0, 0)
        }
    }

    private fun createTabs() {

        addTab("PWR", "Power") {
            showPower()
        }

        addTab("LOAD", "Loads") {
            showLoad()
        }

        addTab("CBL", "Cable") {
            showCable()
        }

        addTab("VD", "Voltage Drop") {
            showVoltageDrop()
        }

        addTab("SC", "Short Circuit") {
            showShortCircuit()
        }

        addTab("BRK", "Breaker") {
            showBreaker()
        }

        addTab("TR", "Transformer") {
            showTransformer()
        }

        addTab("GEN", "Generator") {
            showGenerator()
        }

        addTab("MTR", "Motor") {
            showMotor()
        }

        addTab("PMP", "Pump") {
            showPump()
        }

        addTab("MDB", "MDB") {
            showMdb()
        }

        addTab("PROT", "Protection") {
            showProtection()
        }

        addTab("NET", "Network") {
            showNetwork()
        }

        addTab("DES", "Complete Design") {
            showCompleteDesign()
        }

        addTab("SLD", "Single Line") {
            showSld()
        }
    }

    private fun addTab(
        shortName: String,
        fullName: String,
        action: () -> Unit
    ) {

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(
                dp(5),
                dp(3),
                dp(5),
                dp(3)
            )

            setBackgroundColor(panel2)

            setOnClickListener {
                val index = tabs.indexOf(this)
                if (index >= 0) {
                    selectTab(index)
                }
                action()
            }
        }

        val code = TextView(this).apply {
            text = shortName
            textSize = 11f
            gravity = Gravity.CENTER
            setTextColor(gray)
            typeface = Typeface.DEFAULT_BOLD
        }

        box.addView(
            code,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(24)
            )
        )

        val name = TextView(this).apply {
            text = fullName
            textSize = 8f
            gravity = Gravity.CENTER
            setTextColor(gray)
            maxLines = 2
        }

        box.addView(
            name,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(25)
            )
        )

        val params = LinearLayout.LayoutParams(
            dp(82),
            dp(62)
        )

        params.setMargins(
            dp(2),
            dp(4),
            dp(2),
            dp(4)
        )

        tabContainer.addView(box, params)
        tabs.add(box)
    }

    private fun selectTab(index: Int) {

        tabs.forEachIndexed { i, tab ->

            if (i == index) {
                tab.setBackgroundColor(accentDark)

                val code = tab.getChildAt(0) as TextView
                code.setTextColor(white)

                val name = tab.getChildAt(1) as TextView
                name.setTextColor(white)
            } else {
                tab.setBackgroundColor(panel2)

                val code = tab.getChildAt(0) as TextView
                code.setTextColor(gray)

                val name = tab.getChildAt(1) as TextView
                name.setTextColor(gray)
            }
        }
    }

    private fun page(
        title: String,
        description: String
    ) {

        content.removeAllViews()

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                dp(4),
                dp(14),
                dp(4),
                dp(8)
            )
        }

        val titleView = TextView(this).apply {
            text = title
            textSize = 21f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        header.addView(titleView)

        val descriptionView = TextView(this).apply {
            text = description
            textSize = 12f
            setTextColor(gray)
            setPadding(0, dp(4), 0, 0)
        }

        header.addView(descriptionView)

        content.addView(header)
    }

    private fun sectionTitle(
        title: String,
        description: String = ""
    ) {

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(7)
            )
            setBackgroundColor(panel)
        }

        val t = TextView(this).apply {
            text = title
            textSize = 14f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
        }

        box.addView(t)

        if (description.isNotEmpty()) {

            val d = TextView(this).apply {
                text = description
                textSize = 10f
                setTextColor(gray)
                setPadding(0, dp(3), 0, 0)
            }

            box.addView(d)
        }

        content.addView(
            box,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, dp(5), 0, dp(5))
            }
        )
    }

    private fun field(
        label: String,
        unit: String = "",
        value: String = ""
    ): EditText {

        return createField(
            label = label,
            unit = unit,
            value = value,
            numeric = true
        )
    }

    private fun textField(
        label: String,
        value: String = ""
    ): EditText {

        return createField(
            label = label,
            unit = "",
            value = value,
            numeric = false
        )
    }

    private fun createField(
        label: String,
        unit: String,
        value: String,
        numeric: Boolean
    ): EditText {

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                dp(11),
                dp(7),
                dp(11),
                dp(7)
            )
            setBackgroundColor(panel)
        }

        val labelRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val labelView = TextView(this).apply {
            text = label
            textSize = 12f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        labelRow.addView(
            labelView,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        if (unit.isNotEmpty()) {

            val unitView = TextView(this).apply {
                text = unit
                textSize = 10f
                setTextColor(accent)
                typeface = Typeface.DEFAULT_BOLD
            }

            labelRow.addView(unitView)
        }

        container.addView(labelRow)

        val edit = EditText(this).apply {

            setText(value)

            textSize = 15f
            setTextColor(white)
            setHintTextColor(Color.rgb(105, 125, 145))

            setSingleLine(true)

            inputType =
                if (numeric) {
                    InputType.TYPE_CLASS_NUMBER or
                            InputType.TYPE_NUMBER_FLAG_DECIMAL or
                            InputType.TYPE_NUMBER_FLAG_SIGNED
                } else {
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                }

            setPadding(
                dp(11),
                0,
                dp(11),
                0
            )

            setBackgroundColor(inputBg)
        }

        container.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(46)
            ).apply {
                setMargins(0, dp(4), 0, 0)
            }
        )

        content.addView(
            container,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    0,
                    dp(3),
                    0,
                    dp(3)
                )
            }
        )

        return edit
    }

    private fun actionButton(
        text: String,
        action: () -> Unit
    ) {

        val button = Button(this).apply {

            this.text = text
            textSize = 13f
            isAllCaps = false
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD

            setOnClickListener {
                action()
            }
        }

        content.addView(
            button,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(50)
            ).apply {
                setMargins(
                    0,
                    dp(10),
                    0,
                    dp(7)
                )
            }
        )
    }

    private fun result(
        name: String,
        value: String
    ) {

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                dp(12),
                dp(8),
                dp(12),
                dp(8)
            )
            setBackgroundColor(resultBg)
        }

        val label = TextView(this).apply {
            text = name
            textSize = 9f
            setTextColor(gray)
            typeface = Typeface.DEFAULT_BOLD
        }

        box.addView(label)

        val valueView = TextView(this).apply {
            text = value
            textSize = 17f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, dp(2), 0, 0)
        }

        box.addView(valueView)

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
                    dp(3)
                )
            }
        )
    }

    private fun error(message: String) {
        result(
            "CALCULATION STATUS",
            message
        )
    }

    private fun value(edit: EditText): Double {
        val text = edit.text.toString().trim()

        if (text.isEmpty()) {
            throw IllegalArgumentException(
                "Please enter: ${edit.hint ?: "required value"}"
            )
        }

        return text.toDouble()
    }

    private fun fmt(value: Double): String {

        if (!value.isFinite()) {
            return "—"
        }

        return if (
            abs(value - value.roundToInt()) < 0.0001
        ) {
            value.roundToInt().toString()
        } else {
            String.format("%.2f", value)
        }
    }

    private fun dp(value: Int): Int {
        return (
            value * resources.displayMetrics.density
        ).roundToInt()
    }

    // =========================================================
    // POWER
    // =========================================================

    private fun showPower() {

        page(
            "POWER",
            "Active power, apparent power and current"
        )

        sectionTitle(
            "INPUT DATA",
            "Enter active power and electrical supply data."
        )

        val kw = field(
            "Active Power",
            "kW",
            "100"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.90"
        )

        actionButton("CALCULATE POWER") {

            try {

                val r = core.power.fromKw(
                    value(kw),
                    value(voltage),
                    value(pf),
                    Phase.THREE
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // LOAD
    // =========================================================

    private fun showLoad() {

        page(
            "LOAD",
            "Electrical load and demand calculation"
        )

        sectionTitle(
            "LOAD IDENTIFICATION",
            "Identify the load before entering its electrical data."
        )

        val name = textField(
            "Load Name",
            "MAIN LOAD"
        )

        val quantity = field(
            "Quantity",
            "No.",
            "1"
        )

        val power = field(
            "Unit Power",
            "kW",
            "10"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.90"
        )

        val demand = field(
            "Demand Factor",
            "",
            "1.00"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        actionButton("CALCULATE LOAD") {

            try {

                val load = ElectricalLoad(
                    name = name.text.toString().trim().ifEmpty {
                        "LOAD"
                    },
                    type = LoadType.MISCELLANEOUS,
                    quantity = value(quantity).toInt(),
                    powerKw = value(power),
                    powerFactor = value(pf),
                    demandFactor = value(demand),
                    voltage = value(voltage),
                    phase = Phase.THREE
                )

                val r = core.loads.calculate(load)

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
                    "CURRENT",
                    "${fmt(r.currentA)} A"
                )

                result(
                    "STARTING CURRENT",
                    "${fmt(r.startingCurrentA)} A"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // CABLE
    // =========================================================

    private fun showCable() {

        page(
            "CABLE",
            "Cable selection based on current and voltage drop"
        )

        sectionTitle(
            "CABLE INPUT",
            "The engineering core performs the cable selection."
        )

        val current = field(
            "Design Current",
            "A",
            "100"
        )

        val length = field(
            "Cable Length",
            "m",
            "50"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.90"
        )

        actionButton("CALCULATE CABLE") {

            try {

                val r = core.cable.calculate(
                    CableInput(
                        designCurrentA = value(current),
                        lengthM = value(length),
                        voltage = value(voltage),
                        powerFactor = value(pf),
                        phase = Phase.THREE,
                        material = ConductorMaterial.COPPER,
                        insulation = CableInsulation.XLPE,
                        installationMethod = InstallationMethod.TRAY,
                        targetVoltageDropPercent = 3.0
                    )
                )

                result(
                    "RECOMMENDED CABLE",
                    r.conductorDescription
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

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // VOLTAGE DROP
    // =========================================================

    private fun showVoltageDrop() {

        page(
            "VOLTAGE DROP",
            "Voltage drop using conductor resistance and reactance"
        )

        val current = field(
            "Load Current",
            "A",
            "100"
        )

        val length = field(
            "Cable Length",
            "m",
            "50"
        )

        val resistance = field(
            "Resistance",
            "Ω/km",
            "0.20"
        )

        val reactance = field(
            "Reactance",
            "Ω/km",
            "0.08"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.90"
        )

        val maximum = field(
            "Maximum Voltage Drop",
            "%",
            "3"
        )

        actionButton("CALCULATE VOLTAGE DROP") {

            try {

                val r = core.voltageDrop.calculate(
                    currentA = value(current),
                    lengthM = value(length),
                    resistanceOhmPerKm = value(resistance),
                    reactanceOhmPerKm = value(reactance),
                    voltage = value(voltage),
                    powerFactor = value(pf),
                    phase = Phase.THREE,
                    maximumPercent = value(maximum)
                )

                result(
                    "VOLTAGE DROP",
                    "${fmt(r.dropVolts)} V"
                )

                result(
                    "VOLTAGE DROP PERCENT",
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // SHORT CIRCUIT
    // =========================================================

    private fun showShortCircuit() {

        page(
            "SHORT CIRCUIT",
            "Transformer and source fault-current calculation"
        )

        val voltage = field(
            "System Voltage",
            "V",
            "400"
        )

        val kva = field(
            "Transformer Rating",
            "kVA",
            "1000"
        )

        val impedance = field(
            "Transformer Impedance",
            "%",
            "6"
        )

        val sourceMva = field(
            "Source Short-Circuit Level",
            "MVA",
            "500"
        )

        actionButton("CALCULATE SHORT CIRCUIT") {

            try {

                val sourceValue = value(sourceMva)

                val r = core.shortCircuit.calculate(
                    ShortCircuitInput(
                        sourceVoltage = value(voltage),
                        transformerKva = value(kva),
                        transformerImpedancePercent = value(impedance),
                        sourceShortCircuitMva =
                            if (sourceValue > 0.0) {
                                sourceValue
                            } else {
                                null
                            }
                    )
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // BREAKER
    // =========================================================

    private fun showBreaker() {

        page(
            "BREAKER",
            "Breaker rating and short-circuit breaking capacity"
        )

        val current = field(
            "Design Current",
            "A",
            "250"
        )

        val shortCircuit = field(
            "Short Circuit Current",
            "kA",
            "25"
        )

        actionButton("SELECT BREAKER") {

            try {

                val r = core.breaker.calculate(
                    BreakerCalculator.BreakerInput(
                        designCurrentA = value(current),
                        shortCircuitCurrentKA = value(shortCircuit)
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // TRANSFORMER
    // =========================================================

    private fun showTransformer() {

        page(
            "TRANSFORMER",
            "Transformer currents and prospective short circuit current"
        )

        val kva = field(
            "Transformer Rating",
            "kVA",
            "1000"
        )

        val primary = field(
            "Primary Voltage",
            "V",
            "11000"
        )

        val secondary = field(
            "Secondary Voltage",
            "V",
            "400"
        )

        val impedance = field(
            "Transformer Impedance",
            "%",
            "6"
        )

        actionButton("CALCULATE TRANSFORMER") {

            try {

                val r = core.transformer.calculate(
                    TransformerInput(
                        ratingKva = value(kva),
                        primaryVoltage = value(primary),
                        secondaryVoltage = value(secondary),
                        impedancePercent = value(impedance)
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
                    "SHORT CIRCUIT CURRENT",
                    "${fmt(r.shortCircuitCurrentKA)} kA"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // GENERATOR
    // =========================================================

    private fun showGenerator() {

        page(
            "GENERATOR",
            "Generator active power, current and breaker"
        )

        val kva = field(
            "Generator Rating",
            "kVA",
            "500"
        )

        val voltage = field(
            "Generator Voltage",
            "V",
            "400"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.80"
        )

        actionButton("CALCULATE GENERATOR") {

            try {

                val r = core.generators.calculate(
                    GeneratorCalculator.GeneratorInput(
                        ratingKva = value(kva),
                        voltageV = value(voltage),
                        powerFactor = value(pf)
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // MOTOR
    // =========================================================

    private fun showMotor() {

        page(
            "MOTOR",
            "Motor full-load and starting current"
        )

        val power = field(
            "Motor Power",
            "kW",
            "75"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.85"
        )

        val efficiency = field(
            "Motor Efficiency",
            "",
            "0.92"
        )

        val starting = field(
            "Starting Current Multiplier",
            "x",
            "6"
        )

        actionButton("CALCULATE MOTOR") {

            try {

                val r = core.motors.calculate(
                    MotorInput(
                        powerKw = value(power),
                        voltage = value(voltage),
                        powerFactor = value(pf),
                        efficiency = value(efficiency),
                        phase = Phase.THREE,
                        startingMultiplier = value(starting)
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // PUMP
    // =========================================================

    private fun showPump() {

        page(
            "PUMP",
            "Hydraulic power, motor power and current"
        )

        val flow = field(
            "Flow",
            "m³/s",
            "0.10"
        )

        val head = field(
            "Head",
            "m",
            "30"
        )

        val pumpEfficiency = field(
            "Pump Efficiency",
            "",
            "0.75"
        )

        val motorEfficiency = field(
            "Motor Efficiency",
            "",
            "0.92"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.85"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        actionButton("CALCULATE PUMP") {

            try {

                val r = core.pumps.calculate(
                    PumpInput(
                        flowM3s = value(flow),
                        headM = value(head),
                        pumpEfficiency = value(pumpEfficiency),
                        motorEfficiency = value(motorEfficiency),
                        powerFactor = value(pf),
                        voltage = value(voltage),
                        phase = Phase.THREE
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // MDB
    // =========================================================

    private fun showMdb() {

        page(
            "MDB",
            "Main distribution board load and incomer selection"
        )

        val load = field(
            "Connected Load",
            "kW",
            "500"
        )

        val demand = field(
            "Demand Factor",
            "",
            "0.80"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.90"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        val spare = field(
            "Spare Capacity",
            "",
            "0.20"
        )

        actionButton("CALCULATE MDB") {

            try {

                val r = core.mdb.calculate(
                    MdbInput(
                        connectedLoadKw = value(load),
                        demandFactor = value(demand),
                        powerFactor = value(pf),
                        voltageV = value(voltage),
                        spareCapacity = value(spare)
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // PROTECTION
    // =========================================================

    private fun showProtection() {

        page(
            "PROTECTION",
            "Cable, breaker and short-circuit protection check"
        )

        val current = field(
            "Design Current",
            "A",
            "250"
        )

        val cable = field(
            "Cable Ampacity",
            "A",
            "300"
        )

        val shortCircuit = field(
            "Short Circuit Current",
            "kA",
            "25"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        actionButton("CHECK PROTECTION") {

            try {

                val r = core.protection.calculate(
                    ProtectionCalculator.ProtectionInput(
                        designCurrentA = value(current),
                        cableAmpacityA = value(cable),
                        shortCircuitKA = value(shortCircuit),
                        voltageV = value(voltage)
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
                    "CABLE PROTECTION",
                    if (r.cableProtected) {
                        "OK"
                    } else {
                        "NOT OK"
                    }
                )

                result(
                    "SHORT CIRCUIT PROTECTION",
                    if (r.shortCircuitProtected) {
                        "OK"
                    } else {
                        "NOT OK"
                    }
                )

                result(
                    "STATUS",
                    r.status
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // NETWORK
    // =========================================================

    private fun showNetwork() {

        page(
            "NETWORK",
            "Electrical network load and transformer estimation"
        )

        val load = field(
            "Connected Load",
            "kW",
            "1000"
        )

        val demand = field(
            "Demand Factor",
            "",
            "0.80"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.90"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        actionButton("CALCULATE NETWORK") {

            try {

                val electricalLoad = ElectricalLoad(
                    name = "MAIN LOAD",
                    type = LoadType.MISCELLANEOUS,
                    quantity = 1,
                    powerKw = value(load),
                    powerFactor = value(pf),
                    demandFactor = value(demand),
                    voltage = value(voltage),
                    phase = Phase.THREE
                )

                val r = core.network.calculate(
                    loads = listOf(electricalLoad),
                    voltageV = value(voltage),
                    powerFactor = value(pf),
                    phase = Phase.THREE
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // COMPLETE DESIGN
    // =========================================================

    private fun showCompleteDesign() {

        page(
            "COMPLETE DESIGN",
            "Integrated preliminary electrical design"
        )

        val load = field(
            "Connected Load",
            "kW",
            "1000"
        )

        val demand = field(
            "Demand Factor",
            "",
            "0.80"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.90"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        val shortCircuit = field(
            "Short Circuit Current",
            "kA",
            "25"
        )

        actionButton("RUN COMPLETE DESIGN") {

            try {

                val electricalLoad = ElectricalLoad(
                    name = "MAIN LOAD",
                    type = LoadType.MISCELLANEOUS,
                    quantity = 1,
                    powerKw = value(load),
                    powerFactor = value(pf),
                    demandFactor = value(demand),
                    voltage = value(voltage),
                    phase = Phase.THREE
                )

                val r = core.completeDesign.calculate(
                    CompleteDesignCalculator.CompleteDesignInput(
                        loads = listOf(electricalLoad),
                        voltageV = value(voltage),
                        powerFactor = value(pf),
                        shortCircuitKA = value(shortCircuit),
                        phase = Phase.THREE
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
                    "BREAKER BREAKING CAPACITY",
                    "${fmt(r.breakerBreakingCapacityKA)} kA"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // =========================================================
    // SLD
    // =========================================================

    private fun showSld() {

        page(
            "SINGLE LINE DIAGRAM",
            "Build and display the electrical network single-line diagram"
        )

        sectionTitle(
            "SOURCE",
            "Select the electrical source for the network."
        )

        val source = textField(
            "Supply Source",
            "UTILITY"
        )

        val transformer = field(
            "Transformer Rating",
            "kVA",
            "1000"
        )

        sectionTitle(
            "NETWORK",
            "Define the main panel and outgoing feeder."
        )

        val panel = textField(
            "Main Panel",
            "MDB"
        )

        val feeder = textField(
            "Feeder",
            "FEEDER-01"
        )

        actionButton("GENERATE SLD") {

            try {

                val sourceName =
                    source.text.toString()
                        .trim()
                        .ifEmpty { "UTILITY" }

                val panelName =
                    panel.text.toString()
                        .trim()
                        .ifEmpty { "MDB" }

                val feederName =
                    feeder.text.toString()
                        .trim()
                        .ifEmpty { "FEEDER-01" }

                val sourceType =
                    when {
                        sourceName.equals(
                            "TRANSFORMER",
                            true
                        ) ->
                            NetworkElementType.TRANSFORMER

                        sourceName.equals(
                            "GENERATOR",
                            true
                        ) ->
                            NetworkElementType.GENERATOR

                        else ->
                            NetworkElementType.SOURCE
                    }

                val sourceElement = NetworkElement(
                    id = "SOURCE",
                    name = sourceName,
                    type = sourceType,
                    ratingKva = value(transformer)
                )

                val panelElement = NetworkElement(
                    id = "MDB",
                    name = panelName,
                    type = NetworkElementType.PANEL,
                    parentId = sourceElement.id,
                    ratingKva = value(transformer)
                )

                val feederElement = NetworkElement(
                    id = "F1",
                    name = feederName,
                    type = NetworkElementType.LOAD,
                    parentId = panelElement.id
                )

                val diagram = core.sld.generate(
                    source = sourceElement,
                    panels = listOf(panelElement),
                    feeders = listOf(feederElement)
                )

                result(
                    "SLD STATUS",
                    "GENERATED"
                )

                result(
                    "SOURCE",
                    sourceElement.name
                )

                result(
                    "NODES",
                    diagram.nodes.size.toString()
                )

                result(
                    "CONNECTIONS",
                    diagram.connections.size.toString()
                )

                renderSld(diagram)

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
            "Graphical representation of the generated electrical network."
        )

        val canvasView = SldView(
            diagram = diagram
        )

        content.addView(
            canvasView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(560)
            ).apply {
                setMargins(
                    0,
                    dp(4),
                    0,
                    dp(10)
                )
            }
        )

        diagram.nodes.forEachIndexed { index, node ->

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

    private inner class SldView(
        private val diagram: SingleLineDiagram
    ) : View(this) {

        private val linePaint = Paint(
            Paint.ANTI_ALIAS_FLAG
        ).apply {
            color = accent
            strokeWidth = dp(3).toFloat()
            style = Paint.Style.STROKE
        }

        private val nodePaint = Paint(
            Paint.ANTI_ALIAS_FLAG
        ).apply {
            color = panel2
            style = Paint.Style.FILL
        }

        private val borderPaint = Paint(
            Paint.ANTI_ALIAS_FLAG
        ).apply {
            color = accent
            strokeWidth = dp(2).toFloat()
            style = Paint.Style.STROKE
        }

        private val textPaint = Paint(
            Paint.ANTI_ALIAS_FLAG
        ).apply {
            color = white
            textSize = dp(12).toFloat()
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }

        private val smallTextPaint = Paint(
            Paint.ANTI_ALIAS_FLAG
        ).apply {
            color = gray
            textSize = dp(9).toFloat()
            textAlign = Paint.Align.CENTER
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            canvas.drawColor(bg)

            if (diagram.nodes.isEmpty()) {
                return
            }

            val centerX = width / 2f
            val top = dp(45).toFloat()
            val gap = dp(125).toFloat()
            val nodeWidth = dp(150).toFloat()
            val nodeHeight = dp(64).toFloat()

            val centers = mutableMapOf<String, Pair<Float, Float>>()

            diagram.nodes.forEachIndexed { index, node ->

                val x = centerX
                val y = top + index * gap

                centers[node.id] =
                    Pair(x, y)

                drawNode(
                    canvas = canvas,
                    nodeName = node.name,
                    nodeType = node.type.name,
                    centerX = x,
                    centerY = y,
                    width = nodeWidth,
                    height = nodeHeight
                )
            }

            diagram.connections.forEach { connection ->

                val from = centers[connection.fromId]
                val to = centers[connection.toId]

                if (from != null && to != null) {

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

                    if (connection.label.isNotEmpty()) {

                        canvas.drawText(
                            connection.label,
                            centerX + dp(48),
                            (startY + endY) / 2f,
                            smallTextPaint
                        )
                    }
                }
            }
        }

        private fun drawNode(
            canvas: Canvas,
            nodeName: String,
            nodeType: String,
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
                dp(8).toFloat(),
                dp(8).toFloat(),
                nodePaint
            )

            canvas.drawRoundRect(
                left,
                top,
                right,
                bottom,
                dp(8).toFloat(),
                dp(8).toFloat(),
                borderPaint
            )

            canvas.drawText(
                nodeName,
                centerX,
                centerY - dp(5),
                textPaint
            )

            canvas.drawText(
                nodeType,
                centerX,
                centerY + dp(14),
                smallTextPaint
            )
        }

        private fun drawArrow(
            canvas: Canvas,
            x: Float,
            y: Float
        ) {

            val path = Path()

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
}

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
import com.electricalengineeringpro.app.core.calculation.BreakerSelectionInput
import com.electricalengineeringpro.app.core.calculation.CableCalculator
import com.electricalengineeringpro.app.core.calculation.CompleteDesignInput
import com.electricalengineeringpro.app.core.calculation.GeneratorInput
import com.electricalengineeringpro.app.core.calculation.MdbInput
import com.electricalengineeringpro.app.core.calculation.MotorCalculator
import com.electricalengineeringpro.app.core.calculation.PowerCalculator
import com.electricalengineeringpro.app.core.calculation.ProtectionInput
import com.electricalengineeringpro.app.core.calculation.PumpCalculator
import com.electricalengineeringpro.app.core.calculation.ShortCircuitCalculator
import com.electricalengineeringpro.app.core.calculation.TransformerCalculator

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

import com.electricalengineeringpro.app.core.sld.SldNode
import com.electricalengineeringpro.app.core.sld.SingleLineDiagram

class MainActivity : Activity() {

    private val core = ProfessionalEngineeringCore.instance

    private val backgroundColor = Color.rgb(11, 18, 32)
    private val cardColor = Color.rgb(20, 30, 48)
    private val cardColor2 = Color.rgb(27, 39, 61)
    private val accentColor = Color.rgb(38, 166, 154)
    private val textColor = Color.WHITE
    private val secondaryTextColor = Color.rgb(170, 180, 195)

    private lateinit var root: LinearLayout
    private lateinit var tabBar: LinearLayout
    private lateinit var content: LinearLayout

    private val tabs = listOf(
        "PWR" to "Power",
        "LOAD" to "Loads",
        "CBL" to "Cable",
        "VD" to "Voltage Drop",
        "SC" to "Short Circuit",
        "BRK" to "Breaker",
        "TR" to "Transformer",
        "GEN" to "Generator",
        "MTR" to "Motor",
        "PMP" to "Pump",
        "MDB" to "MDB",
        "PROT" to "Protection",
        "NET" to "Network",
        "DES" to "Complete Design",
        "SLD" to "Single Line"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildUi()
        showPower()
    }

    private fun buildUi() {

        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(backgroundColor)
        }

        val header = TextView(this).apply {
            text = "PROFESSIONAL ENGINEERING"
            setTextColor(textColor)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(10), dp(16), dp(2))
        }

        root.addView(
            header,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(48)
            )
        )

        val subtitle = TextView(this).apply {
            text = "Electrical Design & Calculation Core"
            setTextColor(secondaryTextColor)
            textSize = 12f
            setPadding(dp(16), 0, dp(16), dp(8))
        }

        root.addView(
            subtitle,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(30)
            )
        )

        val horizontal = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
            setBackgroundColor(cardColor)
        }

        tabBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(8), dp(7), dp(8), dp(7))
        }

        horizontal.addView(tabBar)

        root.addView(
            horizontal,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(72)
            )
        )

        val scroll = ScrollView(this)

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(12), dp(12), dp(24))
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

        createTabs()
    }

    private fun createTabs() {

        tabBar.removeAllViews()

        tabs.forEach { tab ->

            val button = TextView(this).apply {

                text = "${tab.first}\n${tab.second}"

                setTextColor(textColor)
                textSize = 9.5f
                gravity = Gravity.CENTER
                typeface = Typeface.DEFAULT_BOLD

                setPadding(
                    dp(5),
                    dp(3),
                    dp(5),
                    dp(3)
                )

                setBackgroundColor(cardColor2)

                setOnClickListener {

                    when (tab.first) {

                        "PWR" -> showPower()
                        "LOAD" -> showLoad()
                        "CBL" -> showCable()
                        "VD" -> showVoltageDrop()
                        "SC" -> showShortCircuit()
                        "BRK" -> showBreaker()
                        "TR" -> showTransformer()
                        "GEN" -> showGenerator()
                        "MTR" -> showMotor()
                        "PMP" -> showPump()
                        "MDB" -> showMdb()
                        "PROT" -> showProtection()
                        "NET" -> showNetwork()
                        "DES" -> showCompleteDesign()
                        "SLD" -> showSld()
                    }
                }
            }

            tabBar.addView(
                button,
                LinearLayout.LayoutParams(
                    dp(86),
                    dp(56)
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
        content.removeAllViews()
    }

    private fun page(
        title: String,
        description: String
    ) {

        clearContent()

        text(
            title,
            22f,
            textColor,
            Typeface.DEFAULT_BOLD
        )

        text(
            description,
            12f,
            secondaryTextColor,
            Typeface.DEFAULT
        )

        space(10)
    }

    private fun sectionTitle(
        title: String,
        description: String
    ) {

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(cardColor)
            setPadding(
                dp(12),
                dp(9),
                dp(12),
                dp(9)
            )
        }

        val titleView = TextView(this).apply {
            text = title
            setTextColor(textColor)
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
        }

        val descriptionView = TextView(this).apply {
            text = description
            setTextColor(secondaryTextColor)
            textSize = 11f
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
                    0,
                    0,
                    dp(8)
                )
            }
        )
    }

    private fun field(
        label: String,
        unit: String,
        initial: String
    ): EditText {

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val labelView = TextView(this).apply {

            text =
                if (unit.isBlank()) {
                    label
                } else {
                    "$label ($unit)"
                }

            setTextColor(textColor)
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setPadding(
                0,
                dp(3),
                0,
                dp(3)
            )
        }

        val edit = EditText(this).apply {

            setText(initial)

            setTextColor(textColor)
            setHintTextColor(secondaryTextColor)

            textSize = 15f

            inputType =
                InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED

            setSingleLine(true)

            setPadding(
                dp(10),
                0,
                dp(10),
                0
            )

            setBackgroundColor(cardColor2)
        }

        container.addView(labelView)

        container.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(46)
            )
        )

        content.addView(
            container,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    0,
                    0,
                    0,
                    dp(7)
                )
            }
        )

        return edit
    }

    private fun textField(
        label: String,
        initial: String
    ): EditText {

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val labelView = TextView(this).apply {
            text = label
            setTextColor(textColor)
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setPadding(
                0,
                dp(3),
                0,
                dp(3)
            )
        }

        val edit = EditText(this).apply {

            setText(initial)

            setTextColor(textColor)
            setHintTextColor(secondaryTextColor)

            textSize = 15f

            inputType = InputType.TYPE_CLASS_TEXT

            setSingleLine(true)

            setPadding(
                dp(10),
                0,
                dp(10),
                0
            )

            setBackgroundColor(cardColor2)
        }

        container.addView(labelView)

        container.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(46)
            )
        )

        content.addView(
            container,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    0,
                    0,
                    0,
                    dp(7)
                )
            }
        )

        return edit
    }

    private fun actionButton(
        title: String,
        action: () -> Unit
    ) {

        val button = Button(this).apply {

            text = title

            setTextColor(textColor)

            textSize = 12f

            typeface = Typeface.DEFAULT_BOLD

            setBackgroundColor(accentColor)

            setOnClickListener {
                action()
            }
        }

        content.addView(
            button,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(48)
            ).apply {
                setMargins(
                    0,
                    dp(3),
                    0,
                    dp(9)
                )
            }
        )
    }

    private fun result(
        title: String,
        value: String
    ) {

        val row = LinearLayout(this).apply {

            orientation = LinearLayout.HORIZONTAL

            gravity = Gravity.CENTER_VERTICAL

            setBackgroundColor(cardColor)

            setPadding(
                dp(12),
                dp(8),
                dp(12),
                dp(8)
            )
        }

        val titleView = TextView(this).apply {
            text = title
            setTextColor(secondaryTextColor)
            textSize = 11f
        }

        val valueView = TextView(this).apply {
            text = value
            setTextColor(textColor)
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.END
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
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        content.addView(
            row,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    0,
                    0,
                    0,
                    dp(4)
                )
            }
        )
    }

    private fun error(message: String) {

        result(
            "STATUS",
            message
        )
    }

    private fun text(
        value: String,
        size: Float,
        color: Int,
        typeface: Typeface
    ) {

        val view = TextView(this).apply {

            text = value

            textSize = size

            setTextColor(color)

            this.typeface = typeface
        }

        content.addView(
            view,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    0,
                    0,
                    0,
                    dp(4)
                )
            }
        )
    }

    private fun space(height: Int) {

        content.addView(
            View(this),
            LinearLayout.LayoutParams(
                1,
                dp(height)
            )
        )
    }

    private fun value(editText: EditText): Double {

        return editText.text
            .toString()
            .trim()
            .replace(",", ".")
            .toDouble()
    }

    private fun fmt(value: Double): String {

        return if (
            value.isFinite() &&
            kotlin.math.abs(
                value - value.toLong()
            ) < 0.000001
        ) {
            value.toLong().toString()
        } else {
            "%.3f".format(value)
        }
    }

    private fun showPower() {

        page(
            "POWER",
            "Power calculation"
        )

        val power = field(
            "Active Power",
            "kW",
            "100"
        )

        val voltage = field(
            "System Voltage",
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

                val r =
                    core.power.fromKw(
                        powerKw = value(power),
                        voltage = value(voltage),
                        powerFactor = value(pf),
                        phase = Phase.THREE
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

    private fun showLoad() {

        page(
            "LOADS",
            "Electrical load calculation"
        )

        val name = textField(
            "Load Name",
            "LOAD-01"
        )

        val power = field(
            "Load Power",
            "kW",
            "50"
        )

        val quantity = field(
            "Quantity",
            "No.",
            "1"
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

        val diversity = field(
            "Diversity Factor",
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

                val load =
                    ElectricalLoad(
                        name =
                            name.text
                                .toString()
                                .trim(),

                        type =
                            LoadType.MISCELLANEOUS,

                        quantity =
                            value(quantity)
                                .toInt(),

                        powerKw =
                            value(power),

                        powerFactor =
                            value(pf),

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
                    core.loads.calculate(load)

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
                    "${fmt(r.reactivePowerKvar)} kvar"
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

    private fun showCable() {

        page(
            "CABLE",
            "Cable sizing and voltage-drop design"
        )

        val current = field(
            "Design Current",
            "A",
            "250"
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
                            Phase.THREE,

                        material =
                            ConductorMaterial.COPPER,

                        insulation =
                            CableInsulation.XLPE,

                        installationMethod =
                            InstallationMethod.TRAY
                    )

                val r =
                    core.cable.calculate(input)

                result(
                    "SELECTED SIZE",
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    private fun showVoltageDrop() {

        page(
            "VOLTAGE DROP",
            "Voltage-drop verification"
        )

        val current = field(
            "Current",
            "A",
            "250"
        )

        val length = field(
            "Length",
            "m",
            "50"
        )

        val resistance = field(
            "Resistance",
            "Ω/km",
            "0.075"
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

        actionButton("CHECK VOLTAGE DROP") {

            try {

                val r =
                    core.voltageDrop.calculate(
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
                    "DROP VOLTAGE",
                    "${fmt(r.dropVolts)} V"
                )

                result(
                    "DROP PERCENT",
                    "${fmt(r.dropPercent)} %"
                )

                result(
                    "STATUS",
                    if (r.compliant) "COMPLIANT" else "NOT COMPLIANT"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    private fun showShortCircuit() {

        page(
            "SHORT CIRCUIT",
            "Three-phase short-circuit calculation"
        )

        val voltage = field(
            "System Voltage",
            "V",
            "400"
        )

        val transformer = field(
            "Transformer Rating",
            "kVA",
            "1000"
        )

        val impedance = field(
            "Transformer Impedance",
            "%",
            "6"
        )

        actionButton("CALCULATE SHORT CIRCUIT") {

            try {

                val r =
                    core.shortCircuit.calculate(
                        ShortCircuitInput(
                            sourceVoltage =
                                value(voltage),

                            transformerKva =
                                value(transformer),

                            transformerImpedancePercent =
                                value(impedance)
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

    private fun showBreaker() {

        page(
            "BREAKER",
            "Breaker rating and breaking capacity"
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

        actionButton("CALCULATE BREAKER") {

            try {

                val r =
                    core.breaker.calculate(
                        com.electricalengineeringpro.app.core.calculation.BreakerInput(
                            designCurrentA =
                                value(current),

                            shortCircuitCurrentKA =
                                value(shortCircuit)
                        )
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
                    "TYPE",
                    r.type.name
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

    private fun showTransformer() {

        page(
            "TRANSFORMER",
            "Transformer current and fault calculation"
        )

        val rating = field(
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
            "Impedance",
            "%",
            "6"
        )

        actionButton("CALCULATE TRANSFORMER") {

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
                error(e.message ?: "Invalid input")
            }
        }
    }

    private fun showGenerator() {

        page(
            "GENERATOR",
            "Generator rating and full-load current"
        )

        val rating = field(
            "Generator Rating",
            "kVA",
            "500"
        )

        val voltage = field(
            "Voltage",
            "V",
            "400"
        )

        val pf = field(
            "Power Factor",
            "",
            "0.80"
        )

        val efficiency = field(
            "Efficiency",
            "",
            "0.90"
        )

        actionButton("CALCULATE GENERATOR") {

            try {

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
                                Phase.THREE,

                            efficiency =
                                value(efficiency)
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
            "Efficiency",
            "",
            "0.92"
        )

        val starting = field(
            "Starting Multiplier",
            "×",
            "6"
        )

        actionButton("CALCULATE MOTOR") {

            try {

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
                                Phase.THREE,

                            startingMultiplier =
                                value(starting)
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

    private fun showPump() {

        page(
            "PUMP",
            "Pump hydraulic and electrical power"
        )

        val flow = field(
            "Flow",
            "m³/s",
            "0.300"
        )

        val head = field(
            "Head",
            "m",
            "7"
        )

        val pumpEfficiency = field(
            "Pump Efficiency",
            "",
            "0.80"
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
                                Phase.THREE
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
                    "CURRENT",
                    "${fmt(r.currentA)} A"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    private fun showMdb() {

        page(
            "MDB",
            "Main distribution board sizing"
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

        val spare = field(
            "Spare Capacity",
            "",
            "0.20"
        )

        actionButton("CALCULATE MDB") {

            try {

                val r =
                    core.mdb.calculate(
                        MdbInput(
                            connectedLoadKw =
                                value(load),

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
                error(e.message ?: "Invalid input")
            }
        }
    }

    private fun showProtection() {

        page(
            "PROTECTION",
            "Cable and short-circuit protection check"
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

                val r =
                    core.protection.calculate(
                        ProtectionInput(
                            designCurrentA =
                                value(current),

                            cableAmpacityA =
                                value(cable),

                            shortCircuitKA =
                                value(shortCircuit),

                            voltageV =
                                value(voltage)
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

    private fun showNetwork() {

        page(
            "NETWORK",
            "Electrical network summary"
        )

        val load = field(
            "Connected Load",
            "kW",
            "1000"
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

                val electricalLoad =
                    ElectricalLoad(
                        name = "MAIN LOAD",
                        type =
                            LoadType.MISCELLANEOUS,
                        quantity = 1,
                        powerKw =
                            value(load),
                        powerFactor =
                            value(pf),
                        demandFactor =
                            0.80,
                        voltage =
                            value(voltage),
                        phase =
                            Phase.THREE
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
                            Phase.THREE
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

                val electricalLoad =
                    ElectricalLoad(
                        name = "MAIN LOAD",
                        type =
                            LoadType.MISCELLANEOUS,
                        quantity = 1,
                        powerKw =
                            value(load),
                        powerFactor =
                            value(pf),
                        demandFactor =
                            0.80,
                        voltage =
                            value(voltage),
                        phase =
                            Phase.THREE
                    )

                val r =
                    core.completeDesign.calculate(
                        CompleteDesignInput(
                            loads =
                                listOf(
                                    electricalLoad
                                ),
                            voltageV =
                                value(voltage),
                            powerFactor =
                                value(pf),
                            shortCircuitKA =
                                value(shortCircuit),
                            phase =
                                Phase.THREE
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
                error(e.message ?: "Invalid input")
            }
        }
    }

    private fun showSld() {

        page(
            "SINGLE LINE DIAGRAM",
            "Electrical network single-line diagram"
        )

        sectionTitle(
            "SOURCE",
            "Electrical supply source"
        )

        val source = textField(
            "Supply Source",
            "UTILITY"
        )

        val rating = field(
            "Source Rating",
            "kVA",
            "1000"
        )

        sectionTitle(
            "MAIN PANEL",
            "Main distribution panel"
        )

        val panel = textField(
            "Main Panel Name",
            "MDB"
        )

        val feeder = textField(
            "Feeder Name",
            "FEEDER-01"
        )

        actionButton("GENERATE SLD") {

            try {

                val sourceName =
                    source.text
                        .toString()
                        .trim()
                        .ifEmpty {
                            "UTILITY"
                        }

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
                    when {

                        sourceName.equals(
                            "TRANSFORMER",
                            ignoreCase = true
                        ) ->
                            NetworkElementType.TRANSFORMER

                        sourceName.equals(
                            "GENERATOR",
                            ignoreCase = true
                        ) ->
                            NetworkElementType.GENERATOR

                        else ->
                            NetworkElementType.SOURCE
                    }

                val sourceElement =
                    NetworkElement(
                        id = "SOURCE",
                        name = sourceName,
                        type = sourceType,
                        ratingKva =
                            value(rating)
                    )

                val panelElement =
                    NetworkElement(
                        id = "MDB",
                        name = panelName,
                        type =
                            NetworkElementType.PANEL,
                        parentId =
                            sourceElement.id,
                        ratingKva =
                            value(rating)
                    )

                val feederElement =
                    NetworkElement(
                        id = "FEEDER-01",
                        name = feederName,
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
            "Generated electrical network"
        )

        val view =
            SldView(diagram)

        content.addView(
            view,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(600)
            ).apply {
                setMargins(
                    0,
                    dp(4),
                    0,
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

    private inner class SldView(
        private val diagram: SingleLineDiagram
    ) : View(this) {

        private val linePaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                color =
                    accentColor

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
                    cardColor2

                style =
                    Paint.Style.FILL
            }

        private val borderPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                color =
                    accentColor

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
                    dp(13).toFloat()

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
                    dp(10).toFloat()

                textAlign =
                    Paint.Align.CENTER
            }

        override fun onDraw(
            canvas: Canvas
        ) {

            super.onDraw(canvas)

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
                dp(170).toFloat()

            val nodeHeight =
                dp(70).toFloat()

            val startTop =
                dp(60).toFloat()

            val gap =
                dp(145).toFloat()

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
                    canvas = canvas,
                    node = node,
                    centerX = x,
                    centerY = y,
                    width = nodeWidth,
                    height = nodeHeight
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
                                dp(60),
                            (startY + endY) / 2f,
                            smallTextPaint
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

    private fun dp(value: Int): Int {

        return (
            value *
                resources
                    .displayMetrics
                    .density
            ).toInt()
    }
}

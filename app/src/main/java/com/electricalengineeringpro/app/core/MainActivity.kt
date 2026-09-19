package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.*
import com.electricalengineeringpro.app.core.calculation.*
import com.electricalengineeringpro.app.core.model.*
import kotlin.math.roundToInt

class MainActivity : Activity() {

    private val core = ProfessionalEngineeringCore.instance

    private lateinit var content: LinearLayout
    private lateinit var tabContainer: LinearLayout

    private val bg = Color.rgb(7, 14, 24)
    private val panel = Color.rgb(15, 27, 42)
    private val inputBg = Color.rgb(24, 39, 56)
    private val resultBg = Color.rgb(18, 34, 50)
    private val accent = Color.rgb(0, 180, 170)
    private val accentDark = Color.rgb(0, 105, 100)
    private val white = Color.WHITE
    private val gray = Color.rgb(170, 185, 200)

    private val tabs = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        buildInterface()
        showPower()
    }

    private fun buildInterface() {

        val scroll = ScrollView(this)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(dp(12), dp(12), dp(12), dp(24))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(4), dp(4), dp(4), dp(12))
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
            setPadding(0, dp(4), 0, 0)
        }

        header.addView(subtitle)

        root.addView(header)

        val section = TextView(this).apply {
            text = "ENGINEERING CALCULATORS"
            textSize = 11f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(4), dp(4), 0, dp(6))
        }

        root.addView(section)

        val horizontal = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
            setBackgroundColor(panel)
            setPadding(dp(4), dp(2), dp(4), dp(2))
        }

        tabContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        horizontal.addView(tabContainer)

        root.addView(
            horizontal,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(74)
            )
        )

        createTabs()

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(
            content,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val footer = TextView(this).apply {
            text = "Professional Engineering Core\n© Eng. Abdelraouf Elghaty"
            textSize = 10f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, dp(28), 0, 0)
        }

        root.addView(footer)

        scroll.addView(root)
        setContentView(scroll)
    }

    private fun createTabs() {

        addTab("POWER", "Power") { showPower() }
        addTab("LOAD", "Loads") { showLoad() }
        addTab("CABLE", "Cable") { showCable() }
        addTab("V-DROP", "Voltage Drop") { showVoltageDrop() }
        addTab("SHORT", "Short Circuit") { showShortCircuit() }
        addTab("BREAKER", "Breaker") { showBreaker() }
        addTab("TR", "Transformer") { showTransformer() }
        addTab("GEN", "Generator") { showGenerator() }
        addTab("MOTOR", "Motor") { showMotor() }
        addTab("PUMP", "Pump") { showPump() }
        addTab("MDB", "MDB") { showMdb() }
        addTab("PROT", "Protection") { showProtection() }
        addTab("NET", "Network") { showNetwork() }
        addTab("DESIGN", "Complete Design") { showCompleteDesign() }
        addTab("SLD", "Single Line") { showSld() }
    }

    private fun addTab(
        shortName: String,
        fullName: String,
        action: () -> Unit
    ) {

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(7), dp(4), dp(7), dp(4))
            setBackgroundColor(panel)
            setOnClickListener {
                selectTab(this)
                action()
            }
        }

        val code = TextView(this).apply {
            text = shortName
            textSize = 12f
            gravity = Gravity.CENTER
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        box.addView(code)

        val name = TextView(this).apply {
            text = fullName
            textSize = 8f
            gravity = Gravity.CENTER
            setTextColor(gray)
        }

        box.addView(name)

        val params = LinearLayout.LayoutParams(
            dp(78),
            dp(62)
        )

        params.setMargins(dp(3), dp(5), dp(3), dp(5))

        tabContainer.addView(box, params)

        tabs.add(code)
    }

    private fun selectTab(selected: LinearLayout) {

        for (i in 0 until tabContainer.childCount) {

            val item = tabContainer.getChildAt(i)

            if (item == selected) {
                item.setBackgroundColor(accentDark)

                val code = item.getChildAt(0) as TextView
                code.setTextColor(white)
            } else {
                item.setBackgroundColor(panel)

                val code = item.getChildAt(0) as TextView
                code.setTextColor(gray)
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
            setPadding(dp(4), dp(18), dp(4), dp(10))
        }

        val t = TextView(this).apply {
            text = title
            textSize = 22f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        header.addView(t)

        val d = TextView(this).apply {
            text = description
            textSize = 12f
            setTextColor(gray)
            setPadding(0, dp(4), 0, 0)
        }

        header.addView(d)

        content.addView(header)
    }

    private fun field(
        label: String,
        unit: String = "",
        value: String = ""
    ): EditText {

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(8), dp(12), dp(8))
            setBackgroundColor(panel)
        }

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val labelView = TextView(this).apply {
            text = label
            textSize = 13f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        row.addView(
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
                textSize = 11f
                setTextColor(accent)
                typeface = Typeface.DEFAULT_BOLD
            }

            row.addView(unitView)
        }

        container.addView(row)

        val edit = EditText(this).apply {
            setText(value)
            textSize = 16f
            setTextColor(white)
            setHintTextColor(Color.rgb(105, 125, 145))
            setSingleLine(true)

            inputType =
                InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL or
                        InputType.TYPE_NUMBER_FLAG_SIGNED

            setPadding(dp(12), 0, dp(12), 0)
            setBackgroundColor(inputBg)
        }

        container.addView(
            edit,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(48)
            ).apply {
                setMargins(0, dp(5), 0, 0)
            }
        )

        content.addView(
            container,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, dp(4), 0, dp(4))
            }
        )

        return edit
    }

    private fun actionButton(
        text: String,
        action: () -> Unit
    ) {

        val b = Button(this).apply {
            this.text = text
            textSize = 14f
            isAllCaps = false
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
            setOnClickListener {
                action()
            }
        }

        content.addView(
            b,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(52)
            ).apply {
                setMargins(0, dp(14), 0, dp(8))
            }
        )
    }

    private fun result(
        name: String,
        value: String
    ) {

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(10), dp(14), dp(10))
            setBackgroundColor(resultBg)
        }

        val label = TextView(this).apply {
            text = name
            textSize = 10f
            setTextColor(gray)
            typeface = Typeface.DEFAULT_BOLD
        }

        box.addView(label)

        val valueView = TextView(this).apply {
            text = value
            textSize = 19f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, dp(3), 0, 0)
        }

        box.addView(valueView)

        content.addView(
            box,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, dp(4), 0, dp(4))
            }
        )
    }

    private fun error(message: String) {
        result("CALCULATION STATUS", message)
    }

    private fun value(e: EditText): Double {
        return e.text.toString().trim().toDouble()
    }

    private fun fmt(v: Double): String {

        return if (
            kotlin.math.abs(v - v.roundToInt()) < 0.0001
        ) {
            v.roundToInt().toString()
        } else {
            String.format("%.2f", v)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).roundToInt()
    }

    // =========================================================
    // POWER
    // =========================================================

    private fun showPower() {

        page(
            "POWER",
            "Active power, apparent power and current calculation"
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

                result("ACTIVE POWER", "${fmt(r.activePowerKw)} kW")
                result("APPARENT POWER", "${fmt(r.apparentPowerKva)} kVA")
                result("CURRENT", "${fmt(r.currentA)} A")

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
            "Connected, demand and design load calculation"
        )

        val name = field(
            "Load Name",
            "",
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
                    name = name.text.toString(),
                    type = LoadType.MISCELLANEOUS,
                    quantity = value(quantity).toInt(),
                    powerKw = value(power),
                    powerFactor = value(pf),
                    demandFactor = value(demand),
                    voltage = value(voltage),
                    phase = Phase.THREE
                )

                val r = core.loads.calculate(load)

                result("CONNECTED LOAD", "${fmt(r.connectedKw)} kW")
                result("DEMAND LOAD", "${fmt(r.demandKw)} kW")
                result("DESIGN LOAD", "${fmt(r.designKw)} kW")
                result("APPARENT POWER", "${fmt(r.apparentPowerKva)} kVA")
                result("CURRENT", "${fmt(r.currentA)} A")
                result("STARTING CURRENT", "${fmt(r.startingCurrentA)} A")

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
            "Cable sizing according to current and voltage drop"
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
            "Voltage drop calculation using R and X"
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
                    "VOLTAGE DROP",
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

    // =========================================================
    // SHORT CIRCUIT
    // =========================================================

    private fun showShortCircuit() {

        page(
            "SHORT CIRCUIT",
            "Transformer fault current and fault level"
        )

        val voltage = field(
            "Secondary Voltage",
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

        actionButton("CALCULATE SHORT CIRCUIT") {

            try {

                val r = core.shortCircuit.calculate(
                    ShortCircuitInput(
                        sourceVoltage = value(voltage),
                        transformerKva = value(kva),
                        transformerImpedancePercent = value(impedance)
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
            "Load Current",
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

                val r = core.breakerSelection.calculate(
                    BreakerSelectionInput(
                        loadCurrentA = value(current),
                        shortCircuitKA = value(shortCircuit)
                    )
                )

                result(
                    "DESIGN CURRENT",
                    "${fmt(r.designCurrentA)} A"
                )

                result(
                    "RECOMMENDED BREAKER",
                    "${fmt(r.recommendedRatingA)} A"
                )

                result(
                    "BREAKING CAPACITY",
                    "${fmt(r.recommendedBreakingCapacityKA)} kA"
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
            "Transformer current and short-circuit calculation"
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
                    GeneratorInput(
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
            "Hydraulic power, motor power and pump current"
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
            "MDB demand load, incomer and busbar selection"
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
                    ProtectionInput(
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
                    if (r.cableProtected) "OK" else "NOT OK"
                )

                result(
                    "SHORT CIRCUIT PROTECTION",
                    if (r.shortCircuitProtected) "OK" else "NOT OK"
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
                    CompleteDesignInput(
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
            "Generate the electrical network structure"
        )

        val source = field(
            "Supply Source",
            "",
            "UTILITY"
        )

        val transformer = field(
            "Transformer Rating",
            "kVA",
            "1000"
        )

        val panel = field(
            "Main Panel",
            "",
            "MDB"
        )

        val feeder = field(
            "Feeder",
            "",
            "FEEDER-01"
        )

        actionButton("GENERATE SLD") {

            try {

                val sourceElement = NetworkElement(
                    id = "SOURCE",
                    name = source.text.toString(),
                    type = NetworkElementType.SOURCE,
                    ratingKva = value(transformer)
                )

                val panelElement = NetworkElement(
                    id = "MDB",
                    name = panel.text.toString(),
                    type = NetworkElementType.PANEL
                )

                val feederElement = NetworkElement(
                    id = "F1",
                    name = feeder.text.toString(),
                    type = NetworkElementType.LOAD
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
                    "NUMBER OF NODES",
                    diagram.nodes.size.toString()
                )

                result(
                    "NUMBER OF CONNECTIONS",
                    diagram.connections.size.toString()
                )

                diagram.nodes.forEachIndexed { index, node ->

                    result(
                        "NODE ${index + 1}",
                        "${node.name} • ${node.type}"
                    )
                }

                diagram.connections.forEachIndexed { index, connection ->

                    result(
                        "CONNECTION ${index + 1}",
                        "${connection.fromId} → ${connection.toId}"
                    )
                }

            } catch (e: Exception) {
                error(e.message ?: "SLD generation failed")
            }
        }
    }
}

package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.*
import com.electricalengineeringpro.app.core.calculation.*
import com.electricalengineeringpro.app.core.model.*
import kotlin.math.roundToInt

class MainActivity : Activity() {

    private val core = ProfessionalEngineeringCore.instance

    private lateinit var content: LinearLayout
    private lateinit var title: TextView

    private val bg = Color.rgb(8, 16, 28)
    private val card = Color.rgb(18, 30, 46)
    private val field = Color.rgb(25, 40, 58)
    private val accent = Color.rgb(0, 150, 136)
    private val white = Color.WHITE
    private val secondary = Color.rgb(180, 195, 210)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        buildMainInterface()
        showPower()
    }

    private fun buildMainInterface() {

        val scroll = ScrollView(this)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(dp(16), dp(14), dp(16), dp(30))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        title = TextView(this).apply {
            text = "PROFESSIONAL ENGINEERING"
            textSize = 21f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        header.addView(
            title,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        val version = TextView(this).apply {
            text = "V1.0"
            textSize = 12f
            setTextColor(accent)
            typeface = Typeface.DEFAULT_BOLD
        }

        header.addView(version)

        root.addView(
            header,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val subtitle = TextView(this).apply {
            text = "Electrical Design & Calculation System"
            textSize = 13f
            setTextColor(secondary)
            setPadding(0, dp(5), 0, dp(14))
        }

        root.addView(subtitle)

        val tabs = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
        }

        val tabContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        addTab(tabContainer, "POWER") { showPower() }
        addTab(tabContainer, "LOAD") { showLoad() }
        addTab(tabContainer, "CABLE") { showCable() }
        addTab(tabContainer, "V-DROP") { showVoltageDrop() }
        addTab(tabContainer, "SHORT") { showShortCircuit() }
        addTab(tabContainer, "BREAKER") { showBreaker() }
        addTab(tabContainer, "TRANSFORMER") { showTransformer() }
        addTab(tabContainer, "GENERATOR") { showGenerator() }
        addTab(tabContainer, "MOTOR") { showMotor() }
        addTab(tabContainer, "PUMP") { showPump() }
        addTab(tabContainer, "MDB") { showMdb() }
        addTab(tabContainer, "PROTECTION") { showProtection() }
        addTab(tabContainer, "NETWORK") { showNetwork() }
        addTab(tabContainer, "DESIGN") { showCompleteDesign() }
        addTab(tabContainer, "SLD") { showSld() }

        tabs.addView(tabContainer)

        root.addView(
            tabs,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(55)
            )
        )

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
            text = "\nProfessional Engineering Core\n© Eng. Abdelraouf Elghaty"
            textSize = 11f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, dp(25), 0, dp(5))
        }

        root.addView(footer)

        scroll.addView(root)
        setContentView(scroll)
    }

    private fun addTab(
        parent: LinearLayout,
        text: String,
        action: () -> Unit
    ) {
        val button = TextView(this).apply {
            this.text = text
            textSize = 11f
            setTextColor(white)
            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(15), 0, dp(15), 0)
            setBackgroundColor(Color.rgb(28, 45, 62))
            setOnClickListener {
                action()
            }
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            dp(42)
        )

        params.setMargins(dp(3), dp(4), dp(3), dp(4))

        parent.addView(button, params)
    }

    private fun pageHeader(
        name: String,
        description: String
    ) {
        content.removeAllViews()

        val heading = TextView(this).apply {
            text = name
            textSize = 22f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, dp(18), 0, dp(3))
        }

        content.addView(heading)

        val desc = TextView(this).apply {
            text = description
            textSize = 13f
            setTextColor(secondary)
            setPadding(0, 0, 0, dp(15))
        }

        content.addView(desc)
    }

    private fun input(
        hint: String,
        value: String = ""
    ): EditText {

        val e = EditText(this).apply {
            setHint(hint)
            setText(value)
            textSize = 15f
            setTextColor(white)
            setHintTextColor(Color.rgb(125, 145, 165))
            setSingleLine(true)
            inputType =
                InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
            setPadding(dp(14), 0, dp(14), 0)
            setBackgroundColor(field)
        }

        content.addView(
            e,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(52)
            ).apply {
                setMargins(0, dp(5), 0, dp(5))
            }
        )

        return e
    }

    private fun button(
        text: String,
        action: () -> Unit
    ) {
        val b = Button(this).apply {
            this.text = text
            textSize = 14f
            isAllCaps = false
            setTextColor(white)
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
                setMargins(0, dp(12), 0, dp(5))
            }
        )
    }

    private fun resultCard(
        title: String,
        value: String
    ) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(12), dp(16), dp(12))
            setBackgroundColor(card)
        }

        val t = TextView(this).apply {
            text = title
            textSize = 12f
            setTextColor(secondary)
        }

        val v = TextView(this).apply {
            text = value
            textSize = 20f
            setTextColor(white)
            typeface = Typeface.DEFAULT_BOLD
        }

        box.addView(t)
        box.addView(v)

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

    private fun error(message: String) {
        resultCard("INPUT / CALCULATION ERROR", message)
    }

    private fun number(e: EditText): Double {
        return e.text.toString().trim().toDouble()
    }

    private fun safeNumber(e: EditText, default: Double): Double {
        val text = e.text.toString().trim()
        return if (text.isEmpty()) default else text.toDouble()
    }

    private fun fmt(v: Double): String {
        return if (kotlin.math.abs(v - v.roundToInt()) < 0.0001) {
            v.roundToInt().toString()
        } else {
            String.format("%.2f", v)
        }
    }

    // ---------------------------------------------------------
    // POWER
    // ---------------------------------------------------------

    private fun showPower() {

        pageHeader(
            "Power Calculator",
            "Calculate current, kVA and active power."
        )

        val kw = input("Active Power (kW)", "100")
        val voltage = input("Voltage (V)", "400")
        val pf = input("Power Factor", "0.90")

        button("CALCULATE POWER") {

            try {
                val result = core.power.fromKw(
                    powerKw = number(kw),
                    voltage = number(voltage),
                    powerFactor = number(pf),
                    phase = Phase.THREE
                )

                resultCard("ACTIVE POWER", "${fmt(result.activePowerKw)} kW")
                resultCard("APPARENT POWER", "${fmt(result.apparentPowerKva)} kVA")
                resultCard("CURRENT", "${fmt(result.currentA)} A")

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // LOAD
    // ---------------------------------------------------------

    private fun showLoad() {

        pageHeader(
            "Load Calculator",
            "Calculate connected, demand and design load."
        )

        val name = input("Load Name", "Load")
        val qty = input("Quantity", "1")
        val power = input("Unit Power (kW)", "10")
        val pf = input("Power Factor", "0.90")
        val demand = input("Demand Factor", "1.0")
        val voltage = input("Voltage (V)", "400")

        button("CALCULATE LOAD") {

            try {

                val load = ElectricalLoad(
                    name = name.text.toString(),
                    type = LoadType.MISCELLANEOUS,
                    quantity = number(qty).toInt(),
                    powerKw = number(power),
                    powerFactor = number(pf),
                    demandFactor = number(demand),
                    voltage = number(voltage),
                    phase = Phase.THREE
                )

                val r = core.loads.calculate(load)

                resultCard("CONNECTED LOAD", "${fmt(r.connectedKw)} kW")
                resultCard("DEMAND LOAD", "${fmt(r.demandKw)} kW")
                resultCard("DESIGN LOAD", "${fmt(r.designKw)} kW")
                resultCard("APPARENT POWER", "${fmt(r.apparentPowerKva)} kVA")
                resultCard("CURRENT", "${fmt(r.currentA)} A")
                resultCard("STARTING CURRENT", "${fmt(r.startingCurrentA)} A")

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // CABLE
    // ---------------------------------------------------------

    private fun showCable() {

        pageHeader(
            "Cable Sizing",
            "Select conductor size from design current and voltage drop."
        )

        val current = input("Design Current (A)", "100")
        val length = input("Cable Length (m)", "50")
        val voltage = input("Voltage (V)", "400")
        val pf = input("Power Factor", "0.90")

        button("CALCULATE CABLE") {

            try {

                val r = core.cable.calculate(
                    CableInput(
                        designCurrentA = number(current),
                        lengthM = number(length),
                        voltage = number(voltage),
                        powerFactor = number(pf),
                        phase = Phase.THREE,
                        material = ConductorMaterial.COPPER,
                        insulation = CableInsulation.XLPE,
                        installationMethod = InstallationMethod.TRAY,
                        targetVoltageDropPercent = 3.0
                    )
                )

                resultCard(
                    "RECOMMENDED CABLE",
                    r.conductorDescription
                )

                resultCard(
                    "AMPACITY",
                    "${fmt(r.ampacityA)} A"
                )

                resultCard(
                    "VOLTAGE DROP",
                    "${fmt(r.voltageDropPercent)} %"
                )

                resultCard(
                    "UTILIZATION",
                    "${fmt(r.utilizationPercent)} %"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // VOLTAGE DROP
    // ---------------------------------------------------------

    private fun showVoltageDrop() {

        pageHeader(
            "Voltage Drop",
            "Calculate voltage drop using cable R and X."
        )

        val current = input("Current (A)", "100")
        val length = input("Length (m)", "50")
        val resistance = input("Resistance (Ω/km)", "0.20")
        val reactance = input("Reactance (Ω/km)", "0.08")
        val voltage = input("Voltage (V)", "400")
        val pf = input("Power Factor", "0.90")
        val max = input("Maximum Voltage Drop (%)", "3")

        button("CALCULATE VOLTAGE DROP") {

            try {

                val r = core.voltageDrop.calculate(
                    currentA = number(current),
                    lengthM = number(length),
                    resistanceOhmPerKm = number(resistance),
                    reactanceOhmPerKm = number(reactance),
                    voltage = number(voltage),
                    powerFactor = number(pf),
                    phase = Phase.THREE,
                    maximumPercent = number(max)
                )

                resultCard("VOLTAGE DROP", "${fmt(r.dropVolts)} V")
                resultCard("VOLTAGE DROP %", "${fmt(r.dropPercent)} %")
                resultCard(
                    "STATUS",
                    if (r.compliant) "COMPLIANT" else "NOT COMPLIANT"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // SHORT CIRCUIT
    // ---------------------------------------------------------

    private fun showShortCircuit() {

        pageHeader(
            "Short Circuit",
            "Calculate transformer fault current."
        )

        val voltage = input("Secondary Voltage (V)", "400")
        val kva = input("Transformer Rating (kVA)", "1000")
        val z = input("Transformer Impedance (%)", "6")

        button("CALCULATE SHORT CIRCUIT") {

            try {

                val r = core.shortCircuit.calculate(
                    ShortCircuitInput(
                        sourceVoltage = number(voltage),
                        transformerKva = number(kva),
                        transformerImpedancePercent = number(z)
                    )
                )

                resultCard(
                    "FAULT CURRENT",
                    "${fmt(r.faultCurrentKA)} kA"
                )

                resultCard(
                    "FAULT LEVEL",
                    "${fmt(r.faultMva)} MVA"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // BREAKER
    // ---------------------------------------------------------

    private fun showBreaker() {

        pageHeader(
            "Breaker Selection",
            "Select standard breaker rating and breaking capacity."
        )

        val current = input("Load Current (A)", "250")
        val sc = input("Short Circuit (kA)", "25")

        button("SELECT BREAKER") {

            try {

                val r = core.breakerSelection.calculate(
                    BreakerSelectionInput(
                        loadCurrentA = number(current),
                        shortCircuitKA = number(sc)
                    )
                )

                resultCard(
                    "DESIGN CURRENT",
                    "${fmt(r.designCurrentA)} A"
                )

                resultCard(
                    "RECOMMENDED BREAKER",
                    "${fmt(r.recommendedRatingA)} A"
                )

                resultCard(
                    "BREAKING CAPACITY",
                    "${fmt(r.recommendedBreakingCapacityKA)} kA"
                )

                resultCard(
                    "UTILIZATION",
                    "${fmt(r.utilizationPercent)} %"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // TRANSFORMER
    // ---------------------------------------------------------

    private fun showTransformer() {

        pageHeader(
            "Transformer",
            "Calculate transformer currents and short-circuit current."
        )

        val kva = input("Transformer Rating (kVA)", "1000")
        val primary = input("Primary Voltage (V)", "11000")
        val secondary = input("Secondary Voltage (V)", "400")
        val z = input("Impedance (%)", "6")

        button("CALCULATE TRANSFORMER") {

            try {

                val r = core.transformer.calculate(
                    TransformerInput(
                        ratingKva = number(kva),
                        primaryVoltage = number(primary),
                        secondaryVoltage = number(secondary),
                        impedancePercent = number(z)
                    )
                )

                resultCard(
                    "PRIMARY CURRENT",
                    "${fmt(r.primaryCurrentA)} A"
                )

                resultCard(
                    "SECONDARY CURRENT",
                    "${fmt(r.secondaryCurrentA)} A"
                )

                resultCard(
                    "SHORT CIRCUIT CURRENT",
                    "${fmt(r.shortCircuitCurrentKA)} kA"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // GENERATOR
    // ---------------------------------------------------------

    private fun showGenerator() {

        pageHeader(
            "Generator",
            "Calculate generator output, current and breaker."
        )

        val kva = input("Generator Rating (kVA)", "500")
        val voltage = input("Voltage (V)", "400")
        val pf = input("Power Factor", "0.80")

        button("CALCULATE GENERATOR") {

            try {

                val r = core.generators.calculate(
                    GeneratorInput(
                        ratingKva = number(kva),
                        voltageV = number(voltage),
                        powerFactor = number(pf)
                    )
                )

                resultCard(
                    "ACTIVE POWER",
                    "${fmt(r.activePowerKw)} kW"
                )

                resultCard(
                    "FULL LOAD CURRENT",
                    "${fmt(r.fullLoadCurrentA)} A"
                )

                resultCard(
                    "RECOMMENDED BREAKER",
                    "${fmt(r.recommendedBreakerA)} A"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // MOTOR
    // ---------------------------------------------------------

    private fun showMotor() {

        pageHeader(
            "Motor",
            "Calculate motor full-load and starting current."
        )

        val power = input("Motor Power (kW)", "75")
        val voltage = input("Voltage (V)", "400")
        val pf = input("Power Factor", "0.85")
        val efficiency = input("Efficiency", "0.92")
        val starting = input("Starting Current Multiplier", "6")

        button("CALCULATE MOTOR") {

            try {

                val r = core.motors.calculate(
                    MotorInput(
                        powerKw = number(power),
                        voltage = number(voltage),
                        powerFactor = number(pf),
                        efficiency = number(efficiency),
                        phase = Phase.THREE,
                        startingMultiplier = number(starting)
                    )
                )

                resultCard(
                    "FULL LOAD CURRENT",
                    "${fmt(r.fullLoadCurrentA)} A"
                )

                resultCard(
                    "STARTING CURRENT",
                    "${fmt(r.startingCurrentA)} A"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // PUMP
    // ---------------------------------------------------------

    private fun showPump() {

        pageHeader(
            "Pump",
            "Calculate hydraulic power, motor power and current."
        )

        val flow = input("Flow (m³/s)", "0.10")
        val head = input("Head (m)", "30")
        val pumpEff = input("Pump Efficiency", "0.75")
        val motorEff = input("Motor Efficiency", "0.92")
        val pf = input("Power Factor", "0.85")
        val voltage = input("Voltage (V)", "400")

        button("CALCULATE PUMP") {

            try {

                val r = core.pumps.calculate(
                    PumpInput(
                        flowM3s = number(flow),
                        headM = number(head),
                        pumpEfficiency = number(pumpEff),
                        motorEfficiency = number(motorEff),
                        powerFactor = number(pf),
                        voltage = number(voltage),
                        phase = Phase.THREE
                    )
                )

                resultCard(
                    "HYDRAULIC POWER",
                    "${fmt(r.hydraulicPowerKw)} kW"
                )

                resultCard(
                    "MOTOR POWER",
                    "${fmt(r.motorPowerKw)} kW"
                )

                resultCard(
                    "MOTOR CURRENT",
                    "${fmt(r.currentA)} A"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // MDB
    // ---------------------------------------------------------

    private fun showMdb() {

        pageHeader(
            "MDB Design",
            "Preliminary MDB incomer and busbar selection."
        )

        val load = input("Connected Load (kW)", "500")
        val demand = input("Demand Factor", "0.80")
        val pf = input("Power Factor", "0.90")
        val voltage = input("Voltage (V)", "400")
        val spare = input("Spare Capacity", "0.20")

        button("CALCULATE MDB") {

            try {

                val r = core.mdb.calculate(
                    MdbInput(
                        connectedLoadKw = number(load),
                        demandFactor = number(demand),
                        powerFactor = number(pf),
                        voltageV = number(voltage),
                        spareCapacity = number(spare)
                    )
                )

                resultCard(
                    "DEMAND LOAD",
                    "${fmt(r.demandLoadKw)} kW"
                )

                resultCard(
                    "DESIGN APPARENT POWER",
                    "${fmt(r.apparentPowerKva)} kVA"
                )

                resultCard(
                    "DESIGN CURRENT",
                    "${fmt(r.designCurrentA)} A"
                )

                resultCard(
                    "RECOMMENDED INCOMER",
                    "${fmt(r.recommendedIncomerA)} A"
                )

                resultCard(
                    "RECOMMENDED BUSBAR",
                    "${fmt(r.recommendedBusbarA)} A"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // PROTECTION
    // ---------------------------------------------------------

    private fun showProtection() {

        pageHeader(
            "Protection",
            "Preliminary breaker, cable and short-circuit coordination."
        )

        val current = input("Design Current (A)", "250")
        val cable = input("Cable Ampacity (A)", "300")
        val sc = input("Short Circuit (kA)", "25")
        val voltage = input("Voltage (V)", "400")

        button("CHECK PROTECTION") {

            try {

                val r = core.protection.calculate(
                    ProtectionInput(
                        designCurrentA = number(current),
                        cableAmpacityA = number(cable),
                        shortCircuitKA = number(sc),
                        voltageV = number(voltage)
                    )
                )

                resultCard(
                    "RECOMMENDED BREAKER",
                    "${fmt(r.recommendedBreakerA)} A"
                )

                resultCard(
                    "BREAKING CAPACITY",
                    "${fmt(r.breakingCapacityKA)} kA"
                )

                resultCard(
                    "CABLE PROTECTION",
                    if (r.cableProtected) "OK" else "NOT OK"
                )

                resultCard(
                    "SHORT CIRCUIT PROTECTION",
                    if (r.shortCircuitProtected) "OK" else "NOT OK"
                )

                resultCard("STATUS", r.status)

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // NETWORK
    // ---------------------------------------------------------

    private fun showNetwork() {

        pageHeader(
            "Network Design",
            "Preliminary electrical network calculation."
        )

        val load = input("Total Connected Load (kW)", "1000")
        val demand = input("Demand Factor", "0.80")
        val pf = input("Power Factor", "0.90")
        val voltage = input("Voltage (V)", "400")

        button("CALCULATE NETWORK") {

            try {

                val electricalLoad = ElectricalLoad(
                    name = "MAIN LOAD",
                    type = LoadType.MISCELLANEOUS,
                    quantity = 1,
                    powerKw = number(load),
                    powerFactor = number(pf),
                    demandFactor = number(demand),
                    voltage = number(voltage),
                    phase = Phase.THREE
                )

                val r = core.network.calculate(
                    loads = listOf(electricalLoad),
                    voltageV = number(voltage),
                    powerFactor = number(pf),
                    phase = Phase.THREE
                )

                resultCard(
                    "CONNECTED LOAD",
                    "${fmt(r.totalConnectedKw)} kW"
                )

                resultCard(
                    "DEMAND LOAD",
                    "${fmt(r.totalDemandKw)} kW"
                )

                resultCard(
                    "DESIGN LOAD",
                    "${fmt(r.totalDesignKw)} kW"
                )

                resultCard(
                    "APPARENT POWER",
                    "${fmt(r.totalApparentPowerKva)} kVA"
                )

                resultCard(
                    "MAIN CURRENT",
                    "${fmt(r.mainCurrentA)} A"
                )

                resultCard(
                    "ESTIMATED TRANSFORMER",
                    "${fmt(r.estimatedTransformerKva)} kVA"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // COMPLETE DESIGN
    // ---------------------------------------------------------

    private fun showCompleteDesign() {

        pageHeader(
            "Complete Design",
            "Integrated preliminary electrical design."
        )

        val load = input("Connected Load (kW)", "1000")
        val demand = input("Demand Factor", "0.80")
        val pf = input("Power Factor", "0.90")
        val voltage = input("Voltage (V)", "400")
        val sc = input("Short Circuit (kA)", "25")

        button("RUN COMPLETE DESIGN") {

            try {

                val electricalLoad = ElectricalLoad(
                    name = "MAIN LOAD",
                    type = LoadType.MISCELLANEOUS,
                    quantity = 1,
                    powerKw = number(load),
                    powerFactor = number(pf),
                    demandFactor = number(demand),
                    voltage = number(voltage),
                    phase = Phase.THREE
                )

                val r = core.completeDesign.calculate(
                    CompleteDesignInput(
                        loads = listOf(electricalLoad),
                        voltageV = number(voltage),
                        powerFactor = number(pf),
                        shortCircuitKA = number(sc),
                        phase = Phase.THREE
                    )
                )

                resultCard(
                    "CONNECTED LOAD",
                    "${fmt(r.connectedLoadKW)} kW"
                )

                resultCard(
                    "DEMAND LOAD",
                    "${fmt(r.demandLoadKW)} kW"
                )

                resultCard(
                    "DESIGN LOAD",
                    "${fmt(r.designLoadKW)} kW"
                )

                resultCard(
                    "APPARENT POWER",
                    "${fmt(r.apparentPowerKVA)} kVA"
                )

                resultCard(
                    "MAIN CURRENT",
                    "${fmt(r.mainCurrentA)} A"
                )

                resultCard(
                    "TRANSFORMER REQUIRED",
                    "${fmt(r.transformerRequiredKVA)} kVA"
                )

                resultCard(
                    "RECOMMENDED TRANSFORMER",
                    "${fmt(r.transformerRecommendedKVA)} kVA"
                )

                resultCard(
                    "MAIN BREAKER",
                    "${fmt(r.mainBreakerA)} A"
                )

                resultCard(
                    "BREAKER Icu",
                    "${fmt(r.breakerBreakingCapacityKA)} kA"
                )

            } catch (e: Exception) {
                error(e.message ?: "Invalid input")
            }
        }
    }

    // ---------------------------------------------------------
    // SLD
    // ---------------------------------------------------------

    private fun showSld() {

        pageHeader(
            "Single Line Diagram",
            "Generate a preliminary electrical single-line diagram."
        )

        val sourceName = input("Source Name", "UTILITY")
        val transformerKva = input("Transformer Rating (kVA)", "1000")
        val mainPanel = input("Main Panel", "MDB")
        val feeder = input("Feeder", "FEEDER-01")

        button("GENERATE SLD") {

            try {

                val source = NetworkElement(
                    id = "SOURCE",
                    name = sourceName.text.toString(),
                    type = NetworkElementType.SOURCE,
                    ratingKva = number(transformerKva)
                )

                val panel = NetworkElement(
                    id = "MDB",
                    name = mainPanel.text.toString(),
                    type = NetworkElementType.PANEL
                )

                val feederElement = NetworkElement(
                    id = "F1",
                    name = feeder.text.toString(),
                    type = NetworkElementType.LOAD
                )

                val diagram = core.sld.generate(
                    source = source,
                    panels = listOf(panel),
                    feeders = listOf(feederElement)
                )

                resultCard(
                    "SLD STATUS",
                    "GENERATED"
                )

                resultCard(
                    "SOURCE",
                    source.name
                )

                resultCard(
                    "NODES",
                    diagram.nodes.size.toString()
                )

                resultCard(
                    "CONNECTIONS",
                    diagram.connections.size.toString()
                )

                diagram.nodes.forEachIndexed { index, node ->

                    resultCard(
                        "NODE ${index + 1}",
                        "${node.name}  •  ${node.type}"
                    )
                }

                diagram.connections.forEachIndexed { index, connection ->

                    resultCard(
                        "CONNECTION ${index + 1}",
                        "${connection.fromId} → ${connection.toId}"
                    )
                }

                resultCard(
                    "NEXT STEP",
                    "Graphical SLD renderer will use this generated network."
                )

            } catch (e: Exception) {
                error(e.message ?: "SLD generation failed")
            }
        }
    }

    // ---------------------------------------------------------
    // UTILITY
    // ---------------------------------------------------------

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).roundToInt()
    }
}

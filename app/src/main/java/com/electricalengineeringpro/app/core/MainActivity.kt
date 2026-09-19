package com.electricalengineeringpro.app.core

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var content: LinearLayout

    private val core =
        ProfessionalEngineeringCore.instance

    private val blue =
        Color.rgb(25, 103, 175)

    private val darkBlue =
        Color.rgb(14, 72, 125)

    private val background =
        Color.rgb(245, 248, 252)

    private val white =
        Color.WHITE

    private val text =
        Color.rgb(30, 43, 56)

    private val secondary =
        Color.rgb(91, 105, 118)

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        createInterface()

        showHome()
    }

    /*
     * ============================================================
     * USER INTERFACE
     * ============================================================
     *
     * THIS FILE CONTAINS UI ONLY.
     *
     * Engineering calculations remain inside:
     *
     * ProfessionalEngineeringCore
     *      └── existing calculation classes
     *
     * ============================================================
     */

    private fun createInterface() {

        val root =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.VERTICAL

                setBackgroundColor(
                    background
                )
            }

        val header =
            TextView(this).apply {
                text =
                    "⚡  PROFESSIONAL ENGINEERING"

                textSize =
                    19f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    text
                )

                gravity =
                    Gravity.CENTER_VERTICAL

                setPadding(
                    dp(16),
                    0,
                    dp(16),
                    0
                )

                setBackgroundColor(
                    white
                )
            }

        root.addView(
            header,
            LinearLayout.LayoutParams(
                -1,
                dp(58)
            )
        )

        val tabs =
            HorizontalScrollView(this).apply {
                isHorizontalScrollBarEnabled =
                    false

                setBackgroundColor(
                    white
                )
            }

        val tabContainer =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.HORIZONTAL

                setPadding(
                    dp(6),
                    dp(5),
                    dp(6),
                    dp(5)
                )
            }

        addTab(
            tabContainer,
            "⚡\nPOWER",
            "Power calculation"
        ) {
            showPower()
        }

        addTab(
            tabContainer,
            "▦\nLOAD",
            "Electrical loads"
        ) {
            showMessage(
                "LOAD",
                "Electrical load calculation is provided by the existing LoadCalculator."
            )
        }

        addTab(
            tabContainer,
            "⌁\nCABLE",
            "Cable sizing"
        ) {
            showMessage(
                "CABLE",
                "Cable sizing is provided by the existing CableCalculator."
            )
        }

        addTab(
            tabContainer,
            "↘\nV-DROP",
            "Voltage drop"
        ) {
            showMessage(
                "VOLTAGE DROP",
                "Voltage-drop calculation is provided by the existing VoltageDropCalculator."
            )
        }

        addTab(
            tabContainer,
            "⚠\nS.C.",
            "Short circuit"
        ) {
            showMessage(
                "SHORT CIRCUIT",
                "Short-circuit calculation is provided by the existing ShortCircuitCalculator."
            )
        }

        addTab(
            tabContainer,
            "▣\nBREAKER",
            "Breaker"
        ) {
            showMessage(
                "BREAKER",
                "Breaker calculation is provided by the existing BreakerCalculator."
            )
        }

        addTab(
            tabContainer,
            "⇅\nTRANS.",
            "Transformer"
        ) {
            showMessage(
                "TRANSFORMER",
                "Transformer calculation is provided by the existing TransformerCalculator."
            )
        }

        addTab(
            tabContainer,
            "◉\nGEN.",
            "Generator"
        ) {
            showMessage(
                "GENERATOR",
                "Generator calculation is provided by the existing GeneratorCalculator."
            )
        }

        addTab(
            tabContainer,
            "⚙\nMOTOR",
            "Motor"
        ) {
            showMessage(
                "MOTOR",
                "Motor calculation is provided by the existing MotorCalculator."
            )
        }

        addTab(
            tabContainer,
            "◈\nPUMP",
            "Pump"
        ) {
            showMessage(
                "PUMP",
                "Pump calculation is provided by the existing PumpCalculator."
            )
        }

        addTab(
            tabContainer,
            "▤\nMDB",
            "MDB"
        ) {
            showMessage(
                "MDB",
                "MDB calculation is provided by the existing MdbCalculator."
            )
        }

        addTab(
            tabContainer,
            "🛡\nPROT.",
            "Protection"
        ) {
            showMessage(
                "PROTECTION",
                "Protection calculation is provided by the existing ProtectionCalculator."
            )
        }

        addTab(
            tabContainer,
            "⌘\nNETWORK",
            "Network"
        ) {
            showMessage(
                "NETWORK",
                "Network calculation is provided by the existing ElectricalNetworkCalculator."
            )
        }

        addTab(
            tabContainer,
            "◆\nDESIGN",
            "Complete design"
        ) {
            showMessage(
                "COMPLETE DESIGN",
                "Complete design is provided by the existing CompleteDesignCalculator."
            )
        }

        addTab(
            tabContainer,
            "⌗\nSLD",
            "Single line"
        ) {
            showMessage(
                "SINGLE LINE DIAGRAM",
                "SLD generation is provided by the existing SldGenerator."
            )
        }

        tabs.addView(
            tabContainer
        )

        root.addView(
            tabs,
            LinearLayout.LayoutParams(
                -1,
                dp(78)
            )
        )

        val scroll =
            ScrollView(this).apply {
                setBackgroundColor(
                    background
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
                    dp(30)
                )
            }

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
    }

    private fun addTab(
        container: LinearLayout,
        title: String,
        description: String,
        action: () -> Unit
    ) {

        val button =
            Button(this).apply {

                text =
                    title

                textSize =
                    9f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    darkBlue
                )

                setPadding(
                    dp(4),
                    0,
                    dp(4),
                    0
                )

                setOnClickListener {
                    action()
                }

                contentDescription =
                    description
            }

        container.addView(
            button,
            LinearLayout.LayoutParams(
                dp(82),
                dp(64)
            ).apply {
                setMargins(
                    dp(2),
                    0,
                    dp(2),
                    0
                )
            }
        )
    }

    /*
     * ============================================================
     * PAGES
     * ============================================================
     */

    private fun clear() {
        content.removeAllViews()
    }

    private fun showHome() {

        clear()

        title(
            "ENGINEERING CALCULATIONS",
            "Select an engineering function"
        )

        card(
            "ProfessionalEngineeringCore",
            "All engineering calculations are handled by the existing core and its existing calculation modules."
        )

        card(
            "Calculation Architecture",
            "UI → ProfessionalEngineeringCore → Existing Calculators"
        )

        card(
            "Engineering Units",
            "Power: kW / kVA     Current: A     Fault level: kA     Cable: mm²"
        )
    }

    private fun showPower() {

        clear()

        title(
            "POWER",
            "Active / apparent power and current"
        )

        card(
            "Power Calculator",
            "The calculation engine already exists in ProfessionalEngineeringCore."
        )

        action(
            "OPEN POWER CALCULATOR"
        ) {
            showMessage(
                "POWER",
                "The PowerCalculator already exists in the core."
            )
        }
    }

    private fun showMessage(
        name: String,
        description: String
    ) {

        clear()

        title(
            name,
            description
        )

        card(
            "Existing Engineering Module",
            description
        )

        card(
            "Architecture",
            "This screen does not contain engineering formulas."
        )

        card(
            "Calculation Engine",
            "ProfessionalEngineeringCore.instance"
        )
    }

    private fun title(
        title: String,
        subtitle: String
    ) {

        val t =
            TextView(this).apply {
                text =
                    title

                textSize =
                    21f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    text
                )

                setPadding(
                    dp(4),
                    dp(4),
                    dp(4),
                    0
                )
            }

        val s =
            TextView(this).apply {
                text =
                    subtitle

                textSize =
                    10f

                setTextColor(
                    secondary
                )

                setPadding(
                    dp(4),
                    dp(2),
                    dp(4),
                    dp(10)
                )
            }

        content.addView(t)

        content.addView(s)
    }

    private fun card(
        title: String,
        description: String
    ) {

        val box =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    dp(14),
                    dp(12),
                    dp(14),
                    dp(12)
                )

                setBackgroundColor(
                    white
                )
            }

        val t =
            TextView(this).apply {
                text =
                    title

                textSize =
                    14f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    darkBlue
                )
            }

        val d =
            TextView(this).apply {
                text =
                    description

                textSize =
                    10f

                setTextColor(
                    secondary
                )

                setPadding(
                    0,
                    dp(4),
                    0,
                    0
                )
            }

        box.addView(t)
        box.addView(d)

        content.addView(
            box,
            LinearLayout.LayoutParams(
                -1,
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

    private fun action(
        text: String,
        action: () -> Unit
    ) {

        val button =
            Button(this).apply {

                this.text =
                    text

                textSize =
                    11f

                typeface =
                    Typeface.DEFAULT_BOLD

                setTextColor(
                    Color.WHITE
                )

                setBackgroundColor(
                    blue
                )

                setOnClickListener {
                    action()
                }
            }

        content.addView(
            button,
            LinearLayout.LayoutParams(
                -1,
                dp(46)
            ).apply {
                setMargins(
                    0,
                    dp(6),
                    0,
                    dp(8)
                )
            }
        )
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

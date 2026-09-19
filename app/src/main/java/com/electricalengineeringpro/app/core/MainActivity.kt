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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildUi()
        showHome()
    }

    /*
     * ============================================================
     * UI ONLY
     * ============================================================
     *
     * No engineering calculations are implemented here.
     *
     * Engineering calculations remain in:
     *
     * ProfessionalEngineeringCore
     *        |
     *        +-- calculation/
     *        |
     *        +-- sld/
     *
     * ============================================================
     */

    private fun buildUi() {

        val root = LinearLayout(this)

        root.orientation = LinearLayout.VERTICAL

        root.setBackgroundColor(
            Color.rgb(245, 248, 252)
        )

        val header = TextView(this)

        header.text =
            "⚡  PROFESSIONAL ENGINEERING"

        header.textSize = 19f

        header.typeface =
            Typeface.DEFAULT_BOLD

        header.gravity =
            Gravity.CENTER_VERTICAL

        header.setTextColor(
            Color.rgb(25, 45, 65)
        )

        header.setPadding(
            dp(16),
            0,
            dp(16),
            0
        )

        header.setBackgroundColor(
            Color.WHITE
        )

        root.addView(
            header,
            LinearLayout.LayoutParams(
                -1,
                dp(58)
            )
        )

        val tabsScroll =
            HorizontalScrollView(this)

        tabsScroll.isHorizontalScrollBarEnabled =
            false

        tabsScroll.setBackgroundColor(
            Color.WHITE
        )

        val tabs =
            LinearLayout(this)

        tabs.orientation =
            LinearLayout.HORIZONTAL

        tabs.setPadding(
            dp(6),
            dp(6),
            dp(6),
            dp(6)
        )

        addTab(
            tabs,
            "⚡\nPOWER",
            "Power"
        )

        addTab(
            tabs,
            "▦\nLOAD",
            "Loads"
        )

        addTab(
            tabs,
            "⌁\nCABLE",
            "Cable"
        )

        addTab(
            tabs,
            "↘\nV-DROP",
            "Voltage Drop"
        )

        addTab(
            tabs,
            "⚠\nS.C.",
            "Short Circuit"
        )

        addTab(
            tabs,
            "▣\nBREAKER",
            "Breaker"
        )

        addTab(
            tabs,
            "⇅\nTRANS.",
            "Transformer"
        )

        addTab(
            tabs,
            "◉\nGEN.",
            "Generator"
        )

        addTab(
            tabs,
            "⚙\nMOTOR",
            "Motor"
        )

        addTab(
            tabs,
            "◈\nPUMP",
            "Pump"
        )

        addTab(
            tabs,
            "▤\nMDB",
            "MDB"
        )

        addTab(
            tabs,
            "🛡\nPROT.",
            "Protection"
        )

        addTab(
            tabs,
            "⌘\nNETWORK",
            "Network"
        )

        addTab(
            tabs,
            "◆\nDESIGN",
            "Complete Design"
        )

        addTab(
            tabs,
            "⌗\nSLD",
            "Single Line Diagram"
        )

        tabsScroll.addView(
            tabs
        )

        root.addView(
            tabsScroll,
            LinearLayout.LayoutParams(
                -1,
                dp(78)
            )
        )

        val scroll =
            ScrollView(this)

        content =
            LinearLayout(this)

        content.orientation =
            LinearLayout.VERTICAL

        content.setPadding(
            dp(12),
            dp(12),
            dp(12),
            dp(24)
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

        setContentView(root)
    }

    private fun addTab(
        parent: LinearLayout,
        shortName: String,
        description: String
    ) {

        val button =
            Button(this)

        button.text =
            shortName

        button.textSize =
            8.5f

        button.typeface =
            Typeface.DEFAULT_BOLD

        button.setTextColor(
            Color.rgb(20, 75, 125)
        )

        button.setPadding(
            dp(3),
            0,
            dp(3),
            0
        )

        button.contentDescription =
            description

        button.setOnClickListener {

            showSection(
                description
            )
        }

        val params =
            LinearLayout.LayoutParams(
                dp(78),
                dp(64)
            )

        params.setMargins(
            dp(2),
            0,
            dp(2),
            0
        )

        parent.addView(
            button,
            params
        )
    }

    private fun showHome() {

        content.removeAllViews()

        addTitle(
            "PROFESSIONAL ENGINEERING",
            "Electrical design and calculation system"
        )

        addCard(
            "Calculation Core",
            "ProfessionalEngineeringCore"
        )

        addCard(
            "Engineering Modules",
            "Power • Loads • Cable • Voltage Drop • Short Circuit • Protection"
        )

        addCard(
            "Equipment",
            "Transformer • Generator • Motor • Pump • MDB"
        )

        addCard(
            "System Design",
            "Network • Complete Design • Single Line Diagram"
        )

        addCard(
            "Units",
            "Power: kW / kVA     Current: A     Fault Current: kA     Cable: mm²"
        )
    }

    private fun showSection(
        name: String
    ) {

        content.removeAllViews()

        addTitle(
            name,
            sectionDescription(name)
        )

        addCard(
            "Engineering Module",
            moduleName(name)
        )

        addCard(
            "Calculation Engine",
            "The engineering calculation is implemented in the existing calculation module."
        )

        addCard(
            "Core",
            "ProfessionalEngineeringCore"
        )

        if (name == "Single Line Diagram") {

            addCard(
                "SLD",
                "Single Line Diagram generation is implemented by the existing SldGenerator."
            )
        }
    }

    private fun sectionDescription(
        name: String
    ): String {

        return when (name) {

            "Power" ->
                "Electrical power and current"

            "Loads" ->
                "Electrical load calculations"

            "Cable" ->
                "Cable sizing and selection"

            "Voltage Drop" ->
                "Voltage-drop assessment"

            "Short Circuit" ->
                "Short-circuit fault calculation"

            "Breaker" ->
                "Circuit-breaker calculation"

            "Transformer" ->
                "Transformer calculations"

            "Generator" ->
                "Generator calculations"

            "Motor" ->
                "Motor calculations"

            "Pump" ->
                "Pump electrical calculations"

            "MDB" ->
                "Main distribution board"

            "Protection" ->
                "Electrical protection"

            "Network" ->
                "Electrical network calculation"

            "Complete Design" ->
                "Complete electrical design"

            "Single Line Diagram" ->
                "Electrical single-line diagram"

            else ->
                ""
        }
    }

    private fun moduleName(
        name: String
    ): String {

        return when (name) {

            "Power" ->
                "PowerCalculator"

            "Loads" ->
                "LoadCalculator"

            "Cable" ->
                "CableCalculator"

            "Voltage Drop" ->
                "VoltageDropCalculator"

            "Short Circuit" ->
                "ShortCircuitCalculator"

            "Breaker" ->
                "BreakerCalculator"

            "Transformer" ->
                "TransformerCalculator"

            "Generator" ->
                "GeneratorCalculator"

            "Motor" ->
                "MotorCalculator"

            "Pump" ->
                "PumpCalculator"

            "MDB" ->
                "MdbCalculator"

            "Protection" ->
                "ProtectionCalculator"

            "Network" ->
                "ElectricalNetworkCalculator"

            "Complete Design" ->
                "CompleteDesignCalculator"

            "Single Line Diagram" ->
                "SldGenerator"

            else ->
                ""
        }
    }

    private fun addTitle(
        title: String,
        subtitle: String
    ) {

        val titleView =
            TextView(this)

        titleView.text =
            title

        titleView.textSize =
            21f

        titleView.typeface =
            Typeface.DEFAULT_BOLD

        titleView.setTextColor(
            Color.rgb(20, 55, 90)
        )

        titleView.setPadding(
            dp(4),
            dp(4),
            dp(4),
            0
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
            Color.rgb(90, 105, 120)
        )

        subtitleView.setPadding(
            dp(4),
            dp(3),
            dp(4),
            dp(10)
        )

        content.addView(
            subtitleView
        )
    }

    private fun addCard(
        title: String,
        description: String
    ) {

        val card =
            LinearLayout(this)

        card.orientation =
            LinearLayout.VERTICAL

        card.setBackgroundColor(
            Color.WHITE
        )

        card.setPadding(
            dp(14),
            dp(12),
            dp(14),
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
            Color.rgb(20, 75, 125)
        )

        card.addView(
            titleView
        )

        val descriptionView =
            TextView(this)

        descriptionView.text =
            description

        descriptionView.textSize =
            11f

        descriptionView.setTextColor(
            Color.rgb(85, 100, 115)
        )

        descriptionView.setPadding(
            0,
            dp(5),
            0,
            0
        )

        card.addView(
            descriptionView
        )

        val params =
            LinearLayout.LayoutParams(
                -1,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        params.setMargins(
            0,
            dp(4),
            0,
            dp(7)
        )

        content.addView(
            card,
            params
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
